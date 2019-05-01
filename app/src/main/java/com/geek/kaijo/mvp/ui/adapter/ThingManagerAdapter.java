package com.geek.kaijo.mvp.ui.adapter;

import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.Thing;
import com.geek.kaijo.mvp.ui.adapter.holder.ThingManagerItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 巡查项管理列表适配器
 * Created by LiuLi on 2018/11/6.
 */

public class ThingManagerAdapter extends DefaultAdapter<Inspection> {

    public ThingManagerAdapter(List<Inspection> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Inspection> getHolder(View v, int viewType) {
        return new ThingManagerItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_thing_manager_list;
    }

    @Override
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

}
