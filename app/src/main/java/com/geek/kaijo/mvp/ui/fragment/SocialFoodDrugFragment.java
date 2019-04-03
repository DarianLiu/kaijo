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
 * 社会治理 - 食品药品
 * Created by LiuLi on 2019/1/18.
 */

public class SocialFoodDrugFragment extends Fragment {

    @BindView(R.id.tv_social_fad_foot)
    TextView tvSocialFadFoot;
    @BindView(R.id.tv_social_fad_drug)
    TextView tvSocialFadDrug;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_food_and_drug, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_social_fad_foot, R.id.tv_social_fad_drug})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), SocialManageActivity.class);
        intent.putExtra("assortId", 41);
        switch (view.getId()) {
            case R.id.tv_social_fad_foot://食品
                intent.putExtra("thingType", 4);
                break;
            case R.id.tv_social_fad_drug://药品
                intent.putExtra("thingType", 3);
                break;
        }
        startActivity(intent);
    }
}
