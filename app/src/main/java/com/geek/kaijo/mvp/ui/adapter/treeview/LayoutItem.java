package com.geek.kaijo.mvp.ui.adapter.treeview;

/**
 * 节点布局
 *
 */
public interface LayoutItem {
    /**
     * 返回布局id
     * @return
     */
    int getLayoutId();

    /**
     * 返回展开收拢事件触发id
     * @return
     */
    int getToggleId();

    /**
     * 返回选中事件触发id
     * @return
     */
    int getCheckedId();

    /**
     * 返回单项点击事件触发id
     * @return
     */
    int getClickId();
}
