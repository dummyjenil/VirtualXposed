package io.virtualapp.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.lody.virtual.helper.compat.NativeLibraryHelperCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.virtualapp.R;
import io.virtualapp.XApp;
import io.virtualapp.abs.ui.VFragment;
import io.virtualapp.home.adapters.CloneAppListAdapter;
import io.virtualapp.home.models.AppInfo;
import io.virtualapp.home.models.AppInfoLite;
import io.virtualapp.sys.Installd;

/**
 * Modernized App Selection & Cloning Fragment
 */
public class ListAppFragment extends VFragment<ListAppContract.ListAppPresenter> implements ListAppContract.ListAppView {
    private static final String KEY_SELECT_FROM = "key_select_from";
    private static final int REQUEST_GET_FILE = 1;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Button mInstallButton;
    private CloneAppListAdapter mAdapter;
    private View mSelectFromExternal;

    public static ListAppFragment newInstance(File selectFrom) {
        Bundle args = new Bundle();
        if (selectFrom != null) {
            args.putString(KEY_SELECT_FROM, selectFrom.getPath());
        }
        ListAppFragment fragment = new ListAppFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private File getSelectFrom() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String selectFrom = bundle.getString(KEY_SELECT_FROM);
            if (selectFrom != null) {
                return new File(selectFrom);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_app, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.select_app_recycler_view);
        mProgressBar = view.findViewById(R.id.select_app_progress_bar);
        mInstallButton = view.findViewById(R.id.select_app_install_btn);
        mSelectFromExternal = view.findViewById(R.id.select_app_from_external);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(0x1f000000));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new CloneAppListAdapter(getActivity(), getSelectFrom());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CloneAppListAdapter.ItemEventListener() {
            @Override
            public void onItemClick(AppInfo info, int position) {
                if (!NativeLibraryHelperCompat.isApk64(info.path)) {
                    Toast.makeText(getContext(), R.string.unsupported_for_32bit_app, Toast.LENGTH_SHORT).show();
                    return;
                }
                mAdapter.toggleSelected(position);
            }

            @Override
            public boolean isSelectable(int position) {
                return true;
            }
        });

        mAdapter.setSelectionListener(count -> {
            mInstallButton.setEnabled(count > 0);
            mInstallButton.setText(String.format(Locale.ENGLISH, XApp.getApp().getResources().getString(R.string.install_d), count));
        });

        mInstallButton.setOnClickListener(v -> {
            Integer[] selectedIndices = mAdapter.getSelectedIndices();
            if (selectedIndices == null || selectedIndices.length == 0) {
                return;
            }
            ArrayList<AppInfoLite> dataList = new ArrayList<>(selectedIndices.length);
            for (int index : selectedIndices) {
                AppInfo info = mAdapter.getItem(index);
                dataList.add(new AppInfoLite(info.packageName, info.path, info.fastOpen, info.disableMultiVersion));
            }

            Activity activity = getActivity();
            if (activity != null) {
                Installd.startInstallerActivity(activity, dataList);
                activity.setResult(Activity.RESULT_OK);
            }
        });

        mSelectFromExternal.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.android.package-archive");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(intent, REQUEST_GET_FILE);
            } catch (Throwable t) {
                Toast.makeText(getActivity(), "Cannot open file picker", Toast.LENGTH_SHORT).show();
            }
        });

        new ListAppPresenterImpl(getActivity(), this, getSelectFrom()).start();
    }

    @Override
    public void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void loadFinish(List<AppInfo> infoList) {
        mAdapter.setList(infoList);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(ListAppContract.ListAppPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_GET_FILE || resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        Activity current = getActivity();
        if (current == null) {
            return;
        }

        Uri uri = data.getData();
        String path = getPath(current, uri);
        if (path == null) {
            return;
        }

        Installd.handleRequestFromFile(current, path);
        current.setResult(Activity.RESULT_OK);
    }

    public static String getPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow("_data");
                    return cursor.getString(columnIndex);
                }
            } catch (Throwable ignored) {
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
