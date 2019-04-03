package com.geek.kaijo.mvp.ui.adapter.holder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.mvp.model.entity.ServiceBean;
import com.geek.kaijo.mvp.ui.activity.ServiceDetailActivity;
import com.jess.arms.base.BaseHolder;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 服务项目ItemHolder
 * Created by LiuLi on 2019/1/24.
 */

public class ServiceItemHolder extends BaseHolder<ServiceBean> {

    @BindView(R.id.tv_service_title)
    TextView tvServiceTitle;
    @BindView(R.id.tv_service_create_time)
    TextView tvServiceCreateTime;

    @BindString(R.string.create_time)
    String createTime;

    private int entryType;

    public ServiceItemHolder(View itemView, int entryType) {
        super(itemView);
        this.entryType = entryType;
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(ServiceBean data, int position) {
        tvServiceTitle.setText(data.getTitle());

        tvServiceCreateTime.setText(String.format("%s%s", createTime,
                DateUtils.timeStamp2Date(data.getCreateTime(), "yyy-MM-dd")));

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ServiceDetailActivity.class);
            intent.putExtra("entry_type", entryType);
            intent.putExtra("title", data.getTitle());
            intent.putExtra("content", data.getContent());
            v.getContext().startActivity(intent);
        });
    }
}
