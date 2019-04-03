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
import com.geek.kaijo.mvp.ui.activity.InspectionProjectManagerActivity;
import com.geek.kaijo.mvp.ui.activity.InspectionProjectRegisterActivity;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 案件管理/日常巡查
 * Created by LiuLi on 2019/1/11.
 */

public class CaseInspectFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.tv_inspect_register)
    TextView tvInspectRegister;
    @BindView(R.id.tv_inspect_project_manager)
    TextView tvInspectProjectManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_inspect, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_inspect_register, R.id.tv_inspect_project_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_inspect_register:

                launchActivity(new Intent(getActivity(), InspectionProjectRegisterActivity.class));
                break;
            case R.id.tv_inspect_project_manager:
                launchActivity(new Intent(getActivity(), InspectionProjectManagerActivity.class));
                break;
        }
    }
}
