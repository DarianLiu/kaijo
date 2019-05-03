package com.geek.kaijo.mvp.ui.adapter;

import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.geek.kaijo.mvp.ui.adapter.holder.SubmenuHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 *
 */

public class SubMenuAdapter extends DefaultAdapter<GridItemContent> {

    public SubMenuAdapter(List<GridItemContent> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<GridItemContent> getHolder(View v, int viewType) {
        return new SubmenuHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.submenu_item;
    }

    @Override
    public void onBindViewHolder(BaseHolder<GridItemContent> holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
