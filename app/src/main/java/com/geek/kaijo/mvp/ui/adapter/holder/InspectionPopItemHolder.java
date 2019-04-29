package com.geek.kaijo.mvp.ui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 案件ItemHolder
 * Created by LiuLi on 2018/9/8.
 */

public class InspectionPopItemHolder extends BaseHolder<Inspection> {

    @BindView(R.id.tv_case_time)
    TextView tvCaseTime;
    @BindView(R.id.tv_case_source)
    TextView tvCaseSource;
    @BindView(R.id.tv_case_flag)
    TextView tvCaseFlag;
    @BindView(R.id.tv_case_describe)
    TextView tvCaseDescribe;
    @BindView(R.id.tv_case_address)
    TextView tvCaseAddress;

    public InspectionPopItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(Inspection data, int position) {
//        tvCaseTime.setText(TextUtils.isEmpty(data.getAcceptDate()) ? "" : data.getAcceptDate());
//        tvCaseSource.setText(TextUtils.isEmpty(data.getSource()) ? "" : data.getSource());
//        tvCaseFlag.setText(TextUtils.isEmpty(data.getCaseCode()) ? "暂无" : data.getCaseCode());
//        tvCaseDescribe.setText(TextUtils.isEmpty(data.getDescription()) ? "暂无" : data.getDescription());
//        tvCaseAddress.setText(TextUtils.isEmpty(data.getAddress()) ? "" : data.getAddress());
    }
}
