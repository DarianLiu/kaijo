package com.geek.kaijo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.ui.activity.SocialManageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 社会治理 - 安全生产
 * Created by LiuLi on 2019/1/18.
 */

public class SocialProductFragment extends Fragment {

    @BindView(R.id.tv_social_product_device)
    TextView tvDevice;
    @BindView(R.id.tv_social_product_work_site)
    TextView tvWorkSite;
    @BindView(R.id.tv_social_product_danger)
    TextView tvDanger;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_product, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_social_product_device, R.id.tv_social_product_work_site, R.id.tv_social_product_danger})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), SocialManageActivity.class);
        intent.putExtra("assortId", 40);
        switch (view.getId()) {
            case R.id.tv_social_product_device://特种设备
                intent.putExtra("thingType", 5);
                break;
            case R.id.tv_social_product_work_site://在建工地
                intent.putExtra("thingType", 6);
                break;
            case R.id.tv_social_product_danger://危化品
                intent.putExtra("thingType", 7);
                break;
        }
        startActivity(intent);
    }
}
