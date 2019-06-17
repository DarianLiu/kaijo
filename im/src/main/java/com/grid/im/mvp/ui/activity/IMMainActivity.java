package com.grid.im.mvp.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.grid.im.R;
import com.grid.im.di.component.DaggerIMMainComponent;
import com.grid.im.mvp.contract.IMMainContract;
import com.grid.im.mvp.presenter.IMMainPresenter;
import com.grid.im.mvp.ui.adapter.TabContactsAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.kook.im.ui.home.ContactFragment;
import com.kook.im.ui.home.ConversationFragment;

import java.util.ArrayList;
import java.util.List;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class IMMainActivity extends BaseActivity<IMMainPresenter> implements IMMainContract.View {

    private TextView tvToolbarTitle;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private String[] tabTitle = new String[]{"沟通", "通讯录"};
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private TabContactsAdapter tabAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerIMMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_immain; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(getAppName());
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

//        ConnectFragment connectFragment = new ConnectFragment();
        ConversationFragment connectFragment = new ConversationFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("patientTab", patienting_tab);
//        patientingFragment.setArguments(bundle);
//        IMContactsFragment patientFragment = new IMContactsFragment();
        ContactFragment patientFragment = new ContactFragment();
//        GroupFragment groupFragment = new GroupFragment();
        fragments.add(connectFragment);
        fragments.add(patientFragment);
//        fragments.add(groupFragment);
//        fragments.add(patientFragment2);

        tabAdapter = new TabContactsAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(2);
        //TabLayout配合ViewPager有时会出现不显示Tab文字的Bug,需要按如下顺序
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[1]));
//        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[2]));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(tabTitle[0]);
        tabLayout.getTabAt(1).setText(tabTitle[1]);
//        tabLayout.getTabAt(2).setText(tabTitle[2]);
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
        finish();
    }


    /**
     * 获取应用程序名称
     */
    private String getAppName() {
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    this.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return this.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
