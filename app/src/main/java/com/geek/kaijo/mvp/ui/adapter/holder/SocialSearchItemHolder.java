package com.geek.kaijo.mvp.ui.adapter.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.EventBusTags;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.geek.kaijo.mvp.model.event.ServiceEvent;
import com.geek.kaijo.mvp.ui.activity.SocialProductDangerActivity;
import com.jess.arms.base.BaseHolder;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关键字搜索ItemHolder
 * Created by LiuLi on 2019/1/24.
 */

public class SocialSearchItemHolder extends BaseHolder<SocialThing> {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;

    private int entryType;

    public SocialSearchItemHolder(View itemView, int entryType) {
        super(itemView);
        ButterKnife.bind(itemView);
        this.entryType = entryType;
    }

    @Override
    public void setData(SocialThing data, int position) {
        switch (entryType) {
            case 1://计划生育
            case 2://结婚登记
            case 3://医疗卫生
            case 4://社会救助
            case 5://社会保障
            case 6://死亡殡葬
            case 7://养老服务
            case 8://兵役
            case 9://土地房产
            case 13://扶持政策
                etSearch.setHint("请输入标题内容");
                break;

        }

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SocialProductDangerActivity.class);
//            intent.putExtra("entry_type", assortId);
//            intent.putExtra("title", ((ServiceBean) data).getTitle());
//            intent.putExtra("content", ((ServiceBean) data).getContent());
            v.getContext().startActivity(intent);
        });

        ivSearch.setOnClickListener(v -> {
            String key = etSearch.getText().toString();
            if (!TextUtils.isEmpty(key)) {
                EventBus.getDefault().post(new ServiceEvent(entryType, key), EventBusTags.TAG_SOCIAL_SEARCH);
            }
        });
    }
}
