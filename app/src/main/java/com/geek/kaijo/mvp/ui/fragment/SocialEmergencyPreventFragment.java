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
 * 社会生产 - 应急防范
 * Created by LiuLi on 2019/1/18.
 */

public class SocialEmergencyPreventFragment extends Fragment {

    @BindView(R.id.tv_social_prevent_forest_fire)
    TextView tvForestFire;
    @BindView(R.id.tv_social_prevent_flood)
    TextView tvFlood;
    @BindView(R.id.tv_social_prevent_snow_removal)
    TextView tvSnowRemoval;
    @BindView(R.id.tv_social_prevent_sacrifice)
    TextView tvSacrifice;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_emergency_prevent, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_social_prevent_forest_fire, R.id.tv_social_prevent_flood, R.id.tv_social_prevent_snow_removal, R.id.tv_social_prevent_sacrifice})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), SocialManageActivity.class);
        intent.putExtra("assortId", 42);
        switch (view.getId()) {
            case R.id.tv_social_prevent_forest_fire://森林防火
                intent.putExtra("thingType", 8);
                break;
            case R.id.tv_social_prevent_flood://防台防洪
                intent.putExtra("thingType", 9);
                break;
            case R.id.tv_social_prevent_snow_removal://冬季除雪
                intent.putExtra("thingType", 10);
                break;
            case R.id.tv_social_prevent_sacrifice://文明祭祀
                intent.putExtra("thingType", 11);
                break;
        }
        startActivity(intent);
    }
}
