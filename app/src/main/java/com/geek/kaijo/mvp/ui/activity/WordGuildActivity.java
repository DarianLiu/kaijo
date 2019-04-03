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
import com.geek.kaijo.di.component.DaggerWordGuildComponent;
import com.geek.kaijo.di.module.WordGuildModule;
import com.geek.kaijo.mvp.contract.WordGuildContract;
import com.geek.kaijo.mvp.model.entity.ServiceBean;
import com.geek.kaijo.mvp.presenter.WordGuildPresenter;
import com.geek.kaijo.mvp.ui.adapter.WordGuildAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
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
 * 办事指南
 */
public class WordGuildActivity extends BaseActivity<WordGuildPresenter> implements WordGuildContract.View {

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

    private LoadingProgressDialog loadingDialog;
    private int categoryId;
    private List<ServiceBean> mDatas;
    private WordGuildAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWordGuildComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .wordGuildModule(new WordGuildModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_word_guild; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        categoryId = getIntent().getIntExtra("entry_type", 0);
        tvToolbarTitle.setText(R.string.business_guide);
        if (mPresenter != null)
            mPresenter.findCmsArticlePage(true, "", String.valueOf(categoryId));

        initRecycleView();
        initRefreshLayout();
    }
    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatas = new ArrayList<>();
        mAdapter = new WordGuildAdapter(mDatas, categoryId);
//        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
//            Intent intent = new Intent(this, ServiceDetailActivity.class);
//            intent.putExtra("entry_type", categoryId);
//            intent.putExtra("title", mDatas.get(position - 1).getTitle());
//            intent.putExtra("content", mDatas.get(position - 1).getContent());
//            launchActivity(intent);
//        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.findCmsArticlePage(false, "", String.valueOf(categoryId));
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.findCmsArticlePage(true, "", String.valueOf(categoryId));
            }
        });
    }
    @Override
    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new LoadingProgressDialog.Builder(this)
                    .setCancelable(true)
                    .setCancelOutside(true).create();
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    @Override
    public void hideLoading() {
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
        finish();
    }
    @Override
    public void finishRefresh() {
        if (smartRefresh != null)
            smartRefresh.finishRefresh();
    }

    @Override
    public void finishLoadMore() {
        if (smartRefresh != null)
            smartRefresh.finishLoadMore();
    }

    @Override
    public void refreshData(List<ServiceBean> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreData(List<ServiceBean> datas) {
        if (datas != null && smartRefresh != null && datas.size() == 0) {
            smartRefresh.finishLoadMoreWithNoMoreData();
            return;
        }

        if (datas != null) {
            int index = datas.size();
            mDatas.addAll(datas);
            mAdapter.notifyItemRangeInserted(index + 1, datas.size());
        }
    }
}
