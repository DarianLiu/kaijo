package com.geek.kaijo.mvp.ui.adapter;

import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.ServiceBean;
import com.geek.kaijo.mvp.ui.adapter.holder.ServiceItemHolder;
import com.geek.kaijo.mvp.ui.adapter.holder.ServiceSearchItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 服务Adapter（营商、惠民）
 * Created by LiuLi on 2019/1/24.
 */

public class ServiceAdapter extends DefaultAdapter<ServiceBean> {

    private int entryType;

    public ServiceAdapter(List<ServiceBean> infos, int entryType) {
        super(infos);
        this.entryType = entryType;
    }

    @Override
    public int getItemCount() {
        return mInfos.size() + 1;
    }

    @Override
    public BaseHolder<ServiceBean> getHolder(View v, int viewType) {
        if (viewType == 0) {
            return new ServiceSearchItemHolder(v,entryType);
        } else {
            return new ServiceItemHolder(v,entryType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(BaseHolder<ServiceBean> holder, int position) {
        if (position == 0) {
            holder.setData(null, position);
        } else {
            holder.setData(mInfos.get(position - 1), position);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.item_search_key;
        } else {
            return R.layout.item_service_project;
        }
    }
}
