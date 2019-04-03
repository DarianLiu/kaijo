package com.geek.kaijo.mvp.ui.adapter.holder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.ServiceBean;
import com.geek.kaijo.mvp.ui.activity.ServiceDetailActivity;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 服务项目ItemHolder
 * Created by LiuLi on 2019/1/24.
 */

public class WordGuildContentItemHolder extends BaseHolder<ServiceBean> {

    @BindView(R.id.wg_content_title)
    TextView tvServiceTitle;

    private int entryType;

    public WordGuildContentItemHolder(View itemView, int entryType) {
        super(itemView);
        this.entryType = entryType;
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(ServiceBean data, int position) {
        tvServiceTitle.setText(data.getTitle());


        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ServiceDetailActivity.class);
            intent.putExtra("entry_type", entryType);
            intent.putExtra("title", data.getTitle());
            intent.putExtra("content", data.getContent());
            v.getContext().startActivity(intent);
        });
    }
}
