package com.geek.kaijo.mvp.ui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.model.event.ThingEvent;
import com.jess.arms.base.BaseHolder;

import org.simple.eventbus.EventBus;

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
    @BindView(R.id.wg_right_edit_img)
    ImageView wg_right_edit_img;  //编辑
    @BindView(R.id.wg_right_delete_img)
    ImageView wg_right_delete_img; //删除

    @BindString(R.string.create_time)
    String createTime;

    public SocialItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(ThingPositionInfo data, int position) {
        if(!TextUtils.isEmpty(data.getName())){
            tvServiceTitle.setText(data.getName());
        }else if(!TextUtils.isEmpty(data.getDanweiName())){
            tvServiceTitle.setText(data.getDanweiName());
        }else if(!TextUtils.isEmpty(data.getJingyingzheName())){
            tvServiceTitle.setText(data.getJingyingzheName());  //经营者名称
        }else if(!TextUtils.isEmpty(data.getAddress())){
            tvServiceTitle.setText(data.getAddress());  //地址
        }


        tvServiceCreateTime.setText(String.format("%s%s", createTime,
                DateUtils.timeStamp2Date(data.getCreateTime(), "yyy-MM-dd")));

        wg_right_edit_img.setOnClickListener(v ->
                EventBus.getDefault().post(new ThingEvent(1, position)));

        wg_right_delete_img.setOnClickListener(v ->
                EventBus.getDefault().post(new ThingEvent(2, position)));

    }
}
