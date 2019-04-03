package com.geek.kaijo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.geek.kaijo.R;
import com.geek.kaijo.di.component.DaggerCaseManagerComponent;
import com.geek.kaijo.di.module.CaseManagerModule;
import com.geek.kaijo.mvp.contract.CaseManagerContract;
import com.geek.kaijo.mvp.presenter.CaseManagerPresenter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CaseManagerFragment extends BaseFragment<CaseManagerPresenter> implements CaseManagerContract.View {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private CaseHandleFragment caseHandleFragment;
    private CaseReportFragment caseReportFragment;
    private CaseInspectFragment dailyInspectFragment;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCaseManagerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .caseManagerModule(new CaseManagerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_case_manager, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        tabLayout.addTab(tabLayout.newTab().setText("直接处理"));
        tabLayout.addTab(tabLayout.newTab().setText("案件上报"));
        tabLayout.addTab(tabLayout.newTab().setText("日常巡查"));

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

        caseHandleFragment = new CaseHandleFragment();
        ft.add(R.id.fl_content, caseHandleFragment);
        caseReportFragment = new CaseReportFragment();
        ft.add(R.id.fl_content, caseReportFragment);
        dailyInspectFragment = new CaseInspectFragment();
        ft.add(R.id.fl_content, dailyInspectFragment);

        hideFragment(ft);
        ft.show(caseHandleFragment);
        ft.commit();
    }

    private void setTabSelection(int index) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (index) {
            case 0:
                if (caseHandleFragment != null) {
                    ft.show(caseHandleFragment);
                }
                break;
            case 1:
                if (caseReportFragment != null) {
                    ft.show(caseReportFragment);
                }
                break;
            case 2:
                if (dailyInspectFragment != null) {
                    ft.show(dailyInspectFragment);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (caseHandleFragment != null)
            ft.hide(caseHandleFragment);
        if (caseReportFragment != null)
            ft.hide(caseReportFragment);
        if (dailyInspectFragment != null)
            ft.hide(dailyInspectFragment);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

}
