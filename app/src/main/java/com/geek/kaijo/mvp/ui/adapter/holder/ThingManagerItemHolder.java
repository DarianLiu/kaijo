package com.geek.kaijo.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.Thing;
import com.geek.kaijo.mvp.model.event.ThingEvent;
import com.jess.arms.base.BaseHolder;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 巡查项管理列表
 * Created by LiuLi on 2018/11/6.
 */

public class ThingManagerItemHolder extends BaseHolder<Inspection> {

    @BindView(R.id.tv_thing_name)
    TextView tvThingName;
    @BindView(R.id.tv_thing_remark)
    TextView tvThingRemark;
    @BindView(R.id.tv_thing_lng)
    TextView tvThingLng;
    @BindView(R.id.tv_thing_lat)
    TextView tvThingLat;
    @BindView(R.id.tv_thing_edit)
    TextView tvThingEdit;
    @BindView(R.id.tv_thing_delete)
    TextView tvThingDelete;
    @BindView(R.id.tv_view_location)
    TextView tvViewLocation;

    public ThingManagerItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }


    @Override
    public void setData(Inspection data, int position) {
        tvThingName.setText(data.getName());
        tvThingRemark.setText(data.getRemark());
        tvThingLng.setText(String.valueOf(data.getLng()));
        tvThingLat.setText(String.valueOf(data.getLat()));

        tvThingEdit.setOnClickListener(v ->
                EventBus.getDefault().post(new ThingEvent(1, position)));
        tvThingDelete.setOnClickListener(v ->
                EventBus.getDefault().post(new ThingEvent(2, position)));
        tvViewLocation.setOnClickListener(v ->
                EventBus.getDefault().post(new ThingEvent(3, position)));

    }

}
