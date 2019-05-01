package com.geek.kaijo.mvp.ui.adapter;

import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.ui.adapter.holder.CaseItemHolder;
import com.geek.kaijo.mvp.ui.adapter.holder.InspectionPopItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 巡查点适配器
 * Created by LiuLi on 2018/9/8.
 */

public class InspectionPopAdapter extends DefaultAdapter<Inspection> {

    public InspectionPopAdapter(List<Inspection> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Inspection> getHolder(View v, int viewType) {
        return new InspectionPopItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.case_inspection_item;
    }

    @Override
    public void onBindViewHolder(BaseHolder<Inspection> holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
