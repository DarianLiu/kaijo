package com.geek.kaijo.mvp.model.entity.treebean;

import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.ui.adapter.treeview.TreeNode;
import com.geek.kaijo.mvp.ui.adapter.treeview.TreeViewBinder;


public class BranchViewBinder extends TreeViewBinder<BranchViewBinder.ViewHolder> {

    @Override
    public int getLayoutId() {
        return R.layout.item_branch;
    }

    @Override
    public int getToggleId() {
        return R.id.ivNode;
    }

    @Override
    public int getCheckedId() {
        return 0;
    }

    @Override
    public int getClickId() {
        return R.id.tvName;
    }

    @Override
    public ViewHolder creatViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position, TreeNode treeNode) {
        ((TextView) holder.findViewById(R.id.tvName)).setText(((BranchNode) treeNode.getValue()).getName());
        ((TextView) holder.findViewById(R.id.tvName)).setSelected(treeNode.isChecked());
    }

    class ViewHolder extends TreeViewBinder.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
