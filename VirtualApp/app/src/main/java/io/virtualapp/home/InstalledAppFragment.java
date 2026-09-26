package io.virtualapp.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstalledAppInfo;

import java.util.ArrayList;
import java.util.List;

import io.virtualapp.R;
import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.glide.GlideUtils;
import io.virtualapp.settings.AppManageActivity;

/**
 * Displays all apps currently installed / cloned inside VirtualXposed
 */
public class InstalledAppFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private ProgressBar mProgressBar;
    private Button mBtnClone;
    private InstalledAppAdapter mAdapter;
    private final List<InstalledAppModel> mAppList = new ArrayList<>();

    public static InstalledAppFragment newInstance() {
        return new InstalledAppFragment();
    }

    public static class InstalledAppModel {
        public String packageName;
        public String name;
        public String apkPath;
        public int userId;
        public InstalledAppInfo info;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_installed_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.installed_recycler_view);
        mEmptyView = view.findViewById(R.id.installed_empty_view);
        mProgressBar = view.findViewById(R.id.installed_progress_bar);
        mBtnClone = view.findViewById(R.id.installed_btn_clone);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new InstalledAppAdapter();
        mRecyclerView.setAdapter(mAdapter);

        if (mBtnClone != null) {
            mBtnClone.setOnClickListener(v -> {
                if (getActivity() != null) {
                    ViewPager pager = getActivity().findViewById(R.id.clone_app_view_pager);
                    if (pager != null) {
                        pager.setCurrentItem(1, true);
                    }
                }
            });
        }

        loadInstalledApps();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInstalledApps();
    }

    public void loadInstalledApps() {
        if (mProgressBar != null && mAppList.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        VUiKit.defer().when(() -> {
            List<InstalledAppInfo> installedApps = VirtualCore.get().getInstalledApps(0);
            List<InstalledAppModel> models = new ArrayList<>();
            Context context = getContext();
            if (context == null) {
                return models;
            }
            PackageManager pm = context.getPackageManager();

            for (InstalledAppInfo installedApp : installedApps) {
                int[] userIds = installedApp.getInstalledUsers();
                for (int userId : userIds) {
                    InstalledAppModel model = new InstalledAppModel();
                    model.packageName = installedApp.packageName;
                    model.userId = userId;
                    model.info = installedApp;
                    model.apkPath = installedApp.apkPath;

                    try {
                        ApplicationInfo appInfo = installedApp.getApplicationInfo(userId);
                        if (appInfo != null) {
                            CharSequence label = appInfo.loadLabel(pm);
                            model.name = label != null ? label.toString() : installedApp.packageName;
                        } else {
                            model.name = installedApp.packageName;
                        }
                    } catch (Throwable e) {
                        model.name = installedApp.packageName;
                    }
                    models.add(model);
                }
            }
            return models;
        }).done(models -> {
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
            mAppList.clear();
            if (models != null) {
                mAppList.addAll(models);
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }

            if (mEmptyView != null && mRecyclerView != null) {
                if (mAppList.isEmpty()) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        }).fail(err -> {
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private class InstalledAppAdapter extends RecyclerView.Adapter<InstalledAppViewHolder> {

        @NonNull
        @Override
        public InstalledAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_installed_app, parent, false);
            return new InstalledAppViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InstalledAppViewHolder holder, int position) {
            InstalledAppModel item = mAppList.get(position);
            holder.nameView.setText(item.name);

            if (item.userId != 0) {
                holder.spaceTagView.setVisibility(View.VISIBLE);
                holder.spaceTagView.setText("Space " + (item.userId + 1));
            } else {
                holder.spaceTagView.setVisibility(View.GONE);
            }

            Context context = holder.itemView.getContext();
            if (VirtualCore.get().isOutsideInstalled(item.packageName)) {
                GlideUtils.loadInstalledPackageIcon(context, item.packageName, holder.iconView, android.R.drawable.sym_def_app_icon);
            } else {
                GlideUtils.loadPackageIconFromApkFile(context, item.apkPath, holder.iconView, android.R.drawable.sym_def_app_icon);
            }

            // Click to launch
            holder.itemView.setOnClickListener(v -> {
                Context ctx = getContext();
                if (ctx != null) {
                    LoadingActivity.launch(ctx, item.packageName, item.userId);
                }
            });

            // Long click for options
            holder.itemView.setOnLongClickListener(v -> {
                showAppOptionsDialog(item);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return mAppList.size();
        }
    }

    private static class InstalledAppViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView nameView;
        TextView spaceTagView;

        InstalledAppViewHolder(View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.app_icon);
            nameView = itemView.findViewById(R.id.app_name);
            spaceTagView = itemView.findViewById(R.id.app_space_tag);
        }
    }

    private void showAppOptionsDialog(InstalledAppModel item) {
        if (getContext() == null) {
            return;
        }

        String[] options = new String[]{
                "🚀 Open App",
                "📲 Create Desktop Shortcut",
                "🗑️ Uninstall",
                "⚙️ Manage App"
        };

        new AlertDialog.Builder(getContext())
                .setTitle(item.name)
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Open
                            LoadingActivity.launch(getContext(), item.packageName, item.userId);
                            break;
                        case 1: // Shortcut
                            try {
                                VirtualCore.get().createShortcut(item.userId, item.packageName, null);
                                Toast.makeText(getContext(), R.string.create_shortcut_success, Toast.LENGTH_SHORT).show();
                            } catch (Throwable e) {
                                Toast.makeText(getContext(), "Failed to create shortcut", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2: // Uninstall
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Uninstall " + item.name + "?")
                                    .setMessage("Are you sure you want to remove this app from VirtualXposed?")
                                    .setPositiveButton("Uninstall", (d, w) -> {
                                        VirtualCore.get().uninstallPackageAsUser(item.packageName, item.userId);
                                        loadInstalledApps();
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                            break;
                        case 3: // Manage
                            startActivity(new Intent(getContext(), AppManageActivity.class));
                            break;
                    }
                })
                .show();
    }
}
