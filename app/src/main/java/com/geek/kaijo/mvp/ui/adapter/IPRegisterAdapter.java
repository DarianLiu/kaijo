package com.geek.kaijo.mvp.ui.adapter;

import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.ui.adapter.holder.IPRegisterItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

public class IPRegisterAdapter extends DefaultAdapter<IPRegisterBean> {


    public IPRegisterAdapter(List<IPRegisterBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<IPRegisterBean> getHolder(View v, int viewType) {
        return new IPRegisterItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_register_inspertion;
    }

    @Override
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
