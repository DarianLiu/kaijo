package com.geek.kaijo.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IPRegisterItemHolder extends BaseHolder<IPRegisterBean> {
    @BindView(R.id.ipr_name)
    TextView iprName;
    @BindView(R.id.ipr_status)
    TextView iprStatus;
    @BindView(R.id.ipr_select)
    CheckBox iprSelect;

    public IPRegisterItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(IPRegisterBean data, int position) {
      iprName.setText(data.getName());
      iprStatus.setText(data.getStatus());
    }
}
