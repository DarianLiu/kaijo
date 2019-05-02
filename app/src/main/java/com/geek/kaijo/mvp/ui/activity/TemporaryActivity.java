package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.AppLifecyclesImpl;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.di.component.DaggerTemporaryComponent;
import com.geek.kaijo.mvp.contract.TemporaryContract;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.presenter.TemporaryPresenter;
import com.geek.kaijo.mvp.ui.adapter.CaseAdapter;
import com.geek.kaijo.mvp.ui.adapter.ServiceAdapter;
import com.geek.kaijo.mvp.ui.adapter.TemporaryAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
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
import dao.CaseInfoDao;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * 暂存
 * ================================================
 */
public class TemporaryActivity extends BaseActivity<TemporaryPresenter> implements TemporaryContract.View {

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
    private CaseInfo caseInfo;


    private TemporaryAdapter mAdapter;
    private List<CaseInfo> mCaseList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTemporaryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_temporary; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText("暂存列表");

        mCaseList = new ArrayList<>();
        initRefreshLayout();

    }

    /**
     * 初始化刷新
     */
    private void initRefreshLayout() {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null) {
                    mPresenter.dbGetCaseList();
                }
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new TemporaryAdapter(mCaseList);
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("caseInfo", mCaseList.get(position));
            intent.putExtra("pauseCase",true);
//            launchActivity(intent);
            startActivityForResult(intent,1);
        });
        recyclerView.setAdapter(mAdapter);

        smartRefresh.autoRefresh();
        smartRefresh.setEnableLoadMore(false);

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

    @Override
    public void dbDataSuccess(List<CaseInfo> caseInfos) {
        if(this.isFinishing())return;
        if(caseInfos!=null){
            mCaseList.clear();
            mCaseList.addAll(caseInfos);
            mAdapter.notifyDataSetChanged();
        }

        smartRefresh.finishLoadMore();
        smartRefresh.finishRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){
            smartRefresh.autoRefresh();
        }
    }
}
