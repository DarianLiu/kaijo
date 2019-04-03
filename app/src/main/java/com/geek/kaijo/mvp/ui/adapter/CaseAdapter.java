package com.geek.kaijo.mvp.ui.adapter;

import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.ui.adapter.holder.CaseItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 案件适配器
 * Created by LiuLi on 2018/9/8.
 */

public class CaseAdapter extends DefaultAdapter<Case> {

    public CaseAdapter(List<Case> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Case> getHolder(View v, int viewType) {
        return new CaseItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_case_list;
    }

    @Override
    public void onBindViewHolder(BaseHolder<Case> holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
