package com.geek.kaijo.mvp.ui.adapter;

import android.media.MediaExtractor;
import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 案件适配器
 * Created by LiuLi on 2018/9/8.
 */

public class TemporaryAdapter extends DefaultAdapter<CaseInfo> {

    public TemporaryAdapter(List<CaseInfo> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CaseInfo> getHolder(View v, int viewType) {
        return new TemporaryItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_case_list;
    }

    @Override
    public void onBindViewHolder(BaseHolder<CaseInfo> holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
