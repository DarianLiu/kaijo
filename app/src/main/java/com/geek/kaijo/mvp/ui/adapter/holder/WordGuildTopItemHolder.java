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

public class WordGuildTopItemHolder extends BaseHolder<ServiceBean> {

    @BindView(R.id.wg_et_search)
    EditText etSearch;
    @BindView(R.id.wg_iv_search)
    ImageView ivSearch;

    private int entryType;

    public WordGuildTopItemHolder(View itemView, int entryType) {
        super(itemView);
        ButterKnife.bind(itemView);
        this.entryType = entryType;
    }

    @Override
    public void setData(ServiceBean data, int position) {


        ivSearch.setOnClickListener(v -> {
            String key = etSearch.getText().toString();
            EventBus.getDefault().post(key, EventBusTags.TAG_SERVICE_SEARCH);
        });
    }
}
