package com.geek.kaijo.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.EventBusTags;
import com.geek.kaijo.mvp.model.entity.ServiceBean;
import com.jess.arms.base.BaseHolder;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关键字搜索ItemHolder
 * Created by LiuLi on 2019/1/24.
 */

public class ServiceSearchItemHolder extends BaseHolder<ServiceBean> {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;

    private int entryType;

    public ServiceSearchItemHolder(View itemView, int entryType) {
        super(itemView);
        ButterKnife.bind(itemView);
        this.entryType = entryType;
    }

    @Override
    public void setData(ServiceBean data, int position) {
        switch (entryType) {
            case 1://计划生育
                etSearch.setHint("请输入标题内容");
                break;
            case 2://结婚登记
                etSearch.setHint("请输入标题内容");
                break;
            case 3://医疗卫生
                etSearch.setHint("请输入标题内容");
                break;
            case 4://社会救助
                etSearch.setHint("请输入标题内容");
                break;
            case 5://社会保障
                etSearch.setHint("请输入标题内容");
                break;
            case 6://死亡殡葬
                etSearch.setHint("请输入标题内容");
                break;
            case 7://养老服务
                etSearch.setHint("请输入标题内容");
                break;
            case 8://兵役
                etSearch.setHint("请输入标题内容");
                break;
            case 9://土地房产
                etSearch.setHint("请输入标题内容");
                break;
            case 13://扶持政策
                etSearch.setHint("请输入标题内容");
                break;

        }

        ivSearch.setOnClickListener(v -> {
            String key = etSearch.getText().toString();
            EventBus.getDefault().post(key, EventBusTags.TAG_SERVICE_SEARCH);
        });
    }
}
