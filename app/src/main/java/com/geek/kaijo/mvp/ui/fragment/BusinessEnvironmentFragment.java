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
import com.geek.kaijo.mvp.ui.activity.BenefitServiceActivity;
import com.geek.kaijo.mvp.ui.activity.LegalServiceActivity;
import com.geek.kaijo.mvp.ui.activity.WordGuildActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 营商环境
 * Created by LiuLi on 2019/1/18.
 */

public class BusinessEnvironmentFragment extends Fragment {
    @BindView(R.id.tv_guide)
    TextView tvGuide;
    @BindView(R.id.tv_policy)
    TextView tvPolicy;
    @BindView(R.id.tv_legal_service)
    TextView tvLegalService;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_environment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({
            R.id.tv_guide,
            R.id.tv_policy, R.id.tv_legal_service})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_legal_service://公益法律服务 legal Service
                intent.setClass(getActivity(), LegalServiceActivity.class);
                intent.putExtra("entry_type", 11);
                break;
            case R.id.tv_guide://办事指南  work Guild
                intent.setClass(getActivity(), WordGuildActivity.class);
                intent.putExtra("entry_type", 12);
                break;
            case R.id.tv_policy://扶持政策
                intent.setClass(getActivity(), BenefitServiceActivity.class);
                intent.putExtra("entry_type", 13);
                break;

        }
        startActivity(intent);
    }
}
