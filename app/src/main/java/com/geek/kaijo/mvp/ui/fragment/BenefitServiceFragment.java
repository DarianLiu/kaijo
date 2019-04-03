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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 便民服务
 * Created by LiuLi on 2019/1/18.
 */
public class BenefitServiceFragment extends Fragment {
    @BindView(R.id.tv_benefit_birth_control)
    TextView tvBenefitBirthControl;
    @BindView(R.id.tv_benefit_marry)
    TextView tvBenefitMarry;
    @BindView(R.id.tv_benefit_medical)
    TextView tvBenefitMedical;
    @BindView(R.id.tv_benefit_rescue)
    TextView tvBenefitRescue;
    @BindView(R.id.tv_benefit_security)
    TextView tvBenefitSecurity;
    @BindView(R.id.tv_benefit_death)
    TextView tvBenefitDeath;
    @BindView(R.id.tv_benefit_pension)
    TextView tvBenefitPension;
    @BindView(R.id.tv_benefit_military)
    TextView tvBenefitMilitary;
    @BindView(R.id.tv_benefit_land)
    TextView tvBenefitLand;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_benefit_service, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_benefit_birth_control, R.id.tv_benefit_marry, R.id.tv_benefit_medical, R.id.tv_benefit_rescue, R.id.tv_benefit_security, R.id.tv_benefit_death, R.id.tv_benefit_pension, R.id.tv_benefit_military, R.id.tv_benefit_land})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), BenefitServiceActivity.class);
        switch (view.getId()) {
            case R.id.tv_benefit_birth_control:
                intent.putExtra("entry_type", 1);
                break;
            case R.id.tv_benefit_marry:
                intent.putExtra("entry_type", 2);
                break;
            case R.id.tv_benefit_medical:
                intent.putExtra("entry_type", 3);
                break;
            case R.id.tv_benefit_rescue:
                intent.putExtra("entry_type", 4);
                break;
            case R.id.tv_benefit_security:
                intent.putExtra("entry_type", 5);
                break;
            case R.id.tv_benefit_death:
                intent.putExtra("entry_type", 6);
                break;
            case R.id.tv_benefit_pension:
                intent.putExtra("entry_type", 7);
                break;
            case R.id.tv_benefit_military:
                intent.putExtra("entry_type", 8);
                break;
            case R.id.tv_benefit_land:
                intent.putExtra("entry_type", 9);
                break;
        }
        startActivity(intent);
    }
}
