package io.virtualapp.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.virtualapp.R;
import io.virtualapp.glide.GlideUtils;
import io.virtualapp.home.models.AppInfo;

/**
 * Clean & Modern Material Adapter for Cloning Apps
 */
public class CloneAppListAdapter extends RecyclerView.Adapter<CloneAppListAdapter.ViewHolder> {

    private final Context mContext;
    private final File mFrom;
    private final LayoutInflater mInflater;
    private List<AppInfo> mAppList = new ArrayList<>();
    private final Set<Integer> mSelectedIndices = new HashSet<>();
    private ItemEventListener mItemEventListener;
    private SelectionListener mSelectionListener;

    public interface ItemEventListener {
        void onItemClick(AppInfo info, int position);
        boolean isSelectable(int position);
    }

    public interface SelectionListener {
        void onSelectedCountChanged(int count);
    }

    public CloneAppListAdapter(Context context, @Nullable File from) {
        this.mContext = context;
        this.mFrom = from;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(ItemEventListener listener) {
        this.mItemEventListener = listener;
    }

    public void setSelectionListener(SelectionListener listener) {
        this.mSelectionListener = listener;
    }

    public void setList(List<AppInfo> models) {
        this.mAppList = models != null ? models : new ArrayList<>();
        this.mSelectedIndices.clear();
        notifyDataSetChanged();
        if (mSelectionListener != null) {
            mSelectionListener.onSelectedCountChanged(0);
        }
    }

    public AppInfo getItem(int position) {
        if (position >= 0 && position < mAppList.size()) {
            return mAppList.get(position);
        }
        return null;
    }

    public void toggleSelected(int position) {
        if (mSelectedIndices.contains(position)) {
            mSelectedIndices.remove(position);
        } else {
            mSelectedIndices.add(position);
        }
        notifyItemChanged(position);
        if (mSelectionListener != null) {
            mSelectionListener.onSelectedCountChanged(mSelectedIndices.size());
        }
    }

    public boolean isIndexSelected(int position) {
        return mSelectedIndices.contains(position);
    }

    public int getSelectedCount() {
        return mSelectedIndices.size();
    }

    public Integer[] getSelectedIndices() {
        return mSelectedIndices.toArray(new Integer[0]);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_clone_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo info = mAppList.get(position);
        holder.nameView.setText(info.name);

        String versionText = (info.version != null ? "v" + info.version : "") + (info.splitApk ? " [Split]" : "");
        holder.versionView.setText(versionText);

        if (mFrom == null) {
            GlideUtils.loadInstalledPackageIcon(mContext, info.packageName, holder.iconView, android.R.drawable.sym_def_app_icon);
        } else {
            GlideUtils.loadPackageIconFromApkFile(mContext, info.path, holder.iconView, android.R.drawable.sym_def_app_icon);
        }

        boolean isSelected = isIndexSelected(position);
        holder.checkView.setImageResource(isSelected ? R.drawable.ic_check : R.drawable.ic_no_check);

        if (info.cloneCount > 0) {
            holder.cloneCountView.setVisibility(View.VISIBLE);
            holder.cloneCountView.setText("Cloned: " + info.cloneCount);
        } else {
            holder.cloneCountView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && mItemEventListener != null) {
                mItemEventListener.onItemClick(mAppList.get(pos), pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconView;
        final TextView nameView;
        final TextView versionView;
        final TextView cloneCountView;
        final ImageView checkView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.item_app_icon);
            nameView = itemView.findViewById(R.id.item_app_name);
            versionView = itemView.findViewById(R.id.item_app_version);
            cloneCountView = itemView.findViewById(R.id.item_app_clone_count);
            checkView = itemView.findViewById(R.id.item_app_checked);
        }
    }
}
