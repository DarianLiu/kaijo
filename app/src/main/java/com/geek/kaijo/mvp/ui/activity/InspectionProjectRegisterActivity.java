package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerInspectionProjectRegisterComponent;
import com.geek.kaijo.di.module.InspectionProjectRegisterModule;
import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.InspectionProjectRegisterPresenter;
import com.geek.kaijo.mvp.ui.adapter.IPRegisterAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 巡查项登记
 */
public class InspectionProjectRegisterActivity extends BaseActivity<InspectionProjectRegisterPresenter> implements InspectionProjectRegisterContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView tvToolbarTitleRight;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tvToolbarTitleRightText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.ipr_start)
    Button iprStart;
    @BindView(R.id.ipr_complete)
    Button iprComplete;
    @BindView(R.id.ipr_cancel)
    Button iprCancel;


    private List<IPRegisterBean> mList;
    private IPRegisterAdapter mAdapter;
    private UserInfo userInfo;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInspectionProjectRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inspectionProjectRegisterModule(new InspectionProjectRegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inspection_project_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText(R.string.inspection_register);

        iprStart.setEnabled(true);
        iprComplete.setEnabled(false);
        iprCancel.setEnabled(false);

        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                if (smartRefresh != null)
//                    smartRefresh.setNoMoreData(false);
//                for (int i = 0; i < 10; i++) {
//                    mList.add(new IPRegisterBean("电线干" + i, "正常" + i, false));
//                }
                if(userInfo!=null){
                    mPresenter.findThingPositionListBy(String.valueOf(userInfo.getStreetId()),String.valueOf(userInfo.getCommunityId()),String.valueOf(userInfo.getUserId()));
                }

            }
        });
        smartRefresh.autoRefresh();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();
        mAdapter = new IPRegisterAdapter(mList);
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {

        });
        recyclerView.setAdapter(mAdapter);
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


    @OnClick({R.id.ipr_start, R.id.ipr_complete, R.id.ipr_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ipr_start://开始巡查
                iprComplete.setEnabled(true);
                iprCancel.setEnabled(true);
                iprStart.setEnabled(false);
                break;
            case R.id.ipr_complete://完成巡查
                break;
            case R.id.ipr_cancel://取消巡查
                iprComplete.setEnabled(false);
                iprCancel.setEnabled(false);
                iprStart.setEnabled(true);
                break;
        }
    }

    @Override
    public void httpGetThingListSuccess(List<IPRegisterBean> inspectionList) {
        if(inspectionList!=null){
            mList.clear();
            mList.addAll(inspectionList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void finishRefresh() {
        smartRefresh.finishRefresh();
    }
}
