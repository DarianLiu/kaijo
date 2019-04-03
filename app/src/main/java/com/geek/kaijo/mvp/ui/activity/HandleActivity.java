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
import com.geek.kaijo.di.component.DaggerHandleComponent;
import com.geek.kaijo.di.module.HandleModule;
import com.geek.kaijo.mvp.contract.HandleContract;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.presenter.HandlePresenter;
import com.geek.kaijo.mvp.ui.adapter.CaseAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 处理列表（非自行）
 */
public class HandleActivity extends BaseActivity<HandlePresenter> implements HandleContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView tvToolbarTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private CaseAdapter mAdapter;
    private List<Case> mCaseList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHandleComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .handleModule(new HandleModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_handle; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        int entry_type = getIntent().getIntExtra("entry_type", 0);
        tvToolbarTitle.setText(entry_type == 0 ? "处理列表" : "处理列表");
        initRefreshLayout(entry_type);
    }

    /**
     * 初始化刷新
     */
    private void initRefreshLayout(int entry_type) {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null) {
                    mPresenter.findCaseInfoPageList(false, entry_type);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null) {
                    mPresenter.findCaseInfoPageList(true, entry_type);
                }
            }
        });
        smartRefresh.autoRefresh();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mCaseList = new ArrayList<>();
        mAdapter = new CaseAdapter(mCaseList);
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
            Intent intent = new Intent(HandleActivity.this, HandleDetailActivity.class);
            intent.putExtra("entry_type", entry_type);
            intent.putExtra("case_id", mCaseList.get(position).getCaseId());
            intent.putExtra("case_attribute", mCaseList.get(position).getCaseAttribute());
            launchActivity(intent);
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

    @Override
    public void finishRefresh() {
        smartRefresh.finishRefresh();
    }

    @Override
    public void finishLoadMore() {
        smartRefresh.finishLoadMore();
    }

    @Override
    public void refreshData(List<Case> caseList) {
        mCaseList.clear();
        mCaseList.addAll(caseList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreData(List<Case> caseList) {
        if (caseList != null && smartRefresh != null && caseList.size() == 0) {
            smartRefresh.finishLoadMoreWithNoMoreData();
            return;
        }

        if (caseList != null){
            int index = mCaseList.size();
            mCaseList.addAll(caseList);
            mAdapter.notifyItemRangeInserted(index + 1, caseList.size());
        }
    }
}
