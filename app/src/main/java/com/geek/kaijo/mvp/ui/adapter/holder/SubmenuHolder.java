package com.geek.kaijo.mvp.ui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.geek.kaijo.R;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 案件ItemHolder
 * Created by LiuLi on 2018/9/8.
 */

public class SubmenuHolder extends BaseHolder<GridItemContent> {

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.img_icon)
    ImageView img_icon;

    RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.icon_social_product_device)//图片加载出来前，显示的图片
            .fallback( R.drawable.icon_social_product_device) //url为空的时候,显示的图片
            .error(R.drawable.icon_social_product_device);//图片加载失败后，显示的图片

    public SubmenuHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }

    @Override
    public void setData(GridItemContent data, int position) {
        tv_name.setText(TextUtils.isEmpty(data.getName()) ? "" : data.getName());
        Glide.with(MyApplication.get())
                .load(data.getIconUrl()) //图片地址
                .apply(options)
                .into(img_icon);
//        Glide.with(MyApplication.get()).load(data.getIconUrl()).into(img_icon);

//        R.drawable.icon_social_product_device
    }
}
