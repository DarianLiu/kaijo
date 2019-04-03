package com.geek.kaijo.mvp.model.entity.treebean;


import com.geek.kaijo.R;
import com.geek.kaijo.mvp.ui.adapter.treeview.LayoutItem;

/**
 * 节点2
 */

public class BranchNode implements LayoutItem {
    private String name;

    public BranchNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
