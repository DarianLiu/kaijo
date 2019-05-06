package com.geek.kaijo.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.jess.arms.base.BaseHolder;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 服务项目ItemHolder
 * Created by LiuLi on 2019/1/24.
 */

public class SocialItemHolder extends BaseHolder<ThingPositionInfo> {

    @BindView(R.id.tv_service_title)
    TextView tvServiceTitle;
    @BindView(R.id.tv_service_create_time)
    TextView tvServiceCreateTime;

    @BindString(R.string.create_time)
    String createTime;

    public SocialItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(ThingPositionInfo data, int position) {
        tvServiceTitle.setText(data.getName());

        tvServiceCreateTime.setText(String.format("%s%s", createTime,
                DateUtils.timeStamp2Date(data.getCreateTime(), "yyy-MM-dd")));
    }
}
