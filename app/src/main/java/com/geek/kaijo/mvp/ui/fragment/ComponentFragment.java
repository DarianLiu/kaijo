package com.geek.kaijo.mvp.ui.fragment;

import android.content.Intent;
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
import com.geek.kaijo.di.component.DaggerComponentComponent;
import com.geek.kaijo.di.module.ConponentModule;
import com.geek.kaijo.mvp.contract.ComponentContract;
import com.geek.kaijo.mvp.model.entity.Menu;
import com.geek.kaijo.mvp.presenter.ComponentPresenter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 部件采集
 */
public class ComponentFragment extends BaseFragment<ComponentPresenter> implements ComponentContract.View {

    @BindView(R.id.tab)
    TabLayout tabLayout;
    private LoadingProgressDialog loadingDialog;
    private List<Menu> menuList;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerComponentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .conponentModule(new ConponentModule(this))
                .build()
                .inject(this);

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_social_manager, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.preListInfoMenu(); //存储文件里获取菜单数据
        }
    }

    public void refreshMenu(){
        if(tabLayout.getTabCount()>0){
            return;
        }
        if (mPresenter != null) {
            mPresenter.preListInfoMenu(); //存储文件里获取菜单数据
        }
    }

    private void showDataView() {
        if (menuList != null) {
            tabLayout.removeAllTabs();
            fragmentList.clear();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            for (int i = 0; i < menuList.size(); i++) {
                tabLayout.addTab(tabLayout.newTab().setText(menuList.get(i).getMenuName()));
                TabItemFragment tabItemFragment = new TabItemFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("menus", (Serializable) menuList.get(i).getMenus());
                tabItemFragment.setArguments(bundle);
                ft.add(R.id.container, tabItemFragment);
                fragmentList.add(tabItemFragment);
            }
            hideFragment(ft);
            ft.show(fragmentList.get(0));
            ft.commit();
        }
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_line_vertical));

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
    }


    private void setTabSelection(int index) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        hideFragment(ft);
        ft.show(fragmentList.get(index));
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        for(int i=0;i<fragmentList.size();i++){
            ft.hide(fragmentList.get(i));
        }
    }


    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        if(menuList!=null && menuList.size()>0){  //TAB已经显示  不显示加载进度条
            return;
        }
        if (loadingDialog == null)
            loadingDialog = new LoadingProgressDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setCancelOutside(true).create();
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (getActivity().isFinishing()) return;
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
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



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void httpListInfoMenuSuccess(List<Menu> menuList) {
        if (getActivity().isFinishing()) return;
        if(this.menuList!=null && this.menuList.size()>0){ //界面已显示TAB 不需要刷新

        }else {
            if (menuList != null && menuList.size() > 0) {
                this.menuList = menuList;
                showDataView();
            }
        }
    }

    @Override
    public void preListInfoMenuSuccess(List<Menu> menuList) {  //文件获取数据的返回
        if (getActivity().isFinishing()) return;
        if (menuList != null && menuList.size() > 0) {
            this.menuList = menuList;
            showDataView();
        }
        if (mPresenter != null) {
            mPresenter.httpListInfoMenu();
        }

    }

    @Override
    public void preError() {
        if (getActivity().isFinishing()) return;
        if (mPresenter != null) {
            mPresenter.httpListInfoMenu();
        }
    }
}
