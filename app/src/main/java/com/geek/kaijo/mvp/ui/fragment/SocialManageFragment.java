package com.geek.kaijo.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.geek.kaijo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 社会治理
 * Created by LiuLi on 2019/1/18.
 */

public class SocialManageFragment extends Fragment {


    @BindView(R.id.tab)
    TabLayout tabLayout;
    Unbinder unbinder;

    private SocialCulturalFragment culturalFragment;//文化娱乐
    private SocialEmergencyPreventFragment emergencyPreventFragment;//应急防御
    private SocialFoodDrugFragment foodDrugFragment;//食品药品
    private SocialProductFragment productFragment;//安全生产

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_manager, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData() {

        tabLayout.addTab(tabLayout.newTab().setText("安全生产"));
        tabLayout.addTab(tabLayout.newTab().setText("食品药品"));
        tabLayout.addTab(tabLayout.newTab().setText("应急防范"));
        tabLayout.addTab(tabLayout.newTab().setText("文化娱乐"));
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_line_vertical));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabSelection(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initTabView();
    }

    private void initTabView() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        productFragment = new SocialProductFragment();
        ft.add(R.id.container, productFragment);
        foodDrugFragment = new SocialFoodDrugFragment();
        ft.add(R.id.container, foodDrugFragment);
        emergencyPreventFragment = new SocialEmergencyPreventFragment();
        ft.add(R.id.container, emergencyPreventFragment);
        culturalFragment = new SocialCulturalFragment();
        ft.add(R.id.container, culturalFragment);

        hideFragment(ft);
        ft.show(productFragment);
        ft.commit();
    }

    private void setTabSelection(int index) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (index) {
            case 0:
                if (productFragment != null) {
                    ft.show(productFragment);
                }
                break;
            case 1:
                if (foodDrugFragment != null) {
                    ft.show(foodDrugFragment);
                }
                break;
            case 2:
                if (emergencyPreventFragment != null) {
                    ft.show(emergencyPreventFragment);
                }
                break;
            case 3:
                if (culturalFragment != null) {
                    ft.show(culturalFragment);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (productFragment != null)
            ft.hide(productFragment);
        if (foodDrugFragment != null)
            ft.hide(foodDrugFragment);
        if (emergencyPreventFragment != null)
            ft.hide(emergencyPreventFragment);
        if (culturalFragment != null)
            ft.hide(culturalFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
