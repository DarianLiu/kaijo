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
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerHandleComponent;
import com.geek.kaijo.di.module.HandleModule;
import com.geek.kaijo.mvp.contract.HandleContract;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.HandlePresenter;
import com.geek.kaijo.mvp.ui.adapter.CaseAdapter;
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

    private boolean isCaseSearch; //是否是条件查询
    private String caseCode;  //案件编号
    private String caseAttribute;  //案件属性
    private String casePrimaryCategory;  //案件大类
    private String caseSecondaryCategory; //案件小类
    private String caseChildCategory;  //案件子类

    private UserInfo userInfo;
    int curNode;  //12: 案件处理13: 案件核实14: 案件核查
    int handleType = 2; //1： 自行处理 2：非自行处理


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
        curNode = getIntent().getIntExtra("curNode", 0);
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        isCaseSearch = getIntent().getBooleanExtra("isCaseSearch",false);
        if(isCaseSearch){ //条件查询
            caseCode = getIntent().getStringExtra("caseCode");
            caseAttribute = getIntent().getStringExtra("mCaseAttributeId");
            casePrimaryCategory = getIntent().getStringExtra("mCasePrimaryCategory");
            caseSecondaryCategory = getIntent().getStringExtra("mCaseSecondaryCategory");
            caseChildCategory = getIntent().getStringExtra("mCaseChildCategory");
            handleType = getIntent().getIntExtra("handleType",0);
            tvToolbarTitle.setText("案件列表");
        }



        if(curNode==12){
            tvToolbarTitle.setText("处理列表");
        }else if(curNode==13){
            tvToolbarTitle.setText("核实列表");
        }else if(curNode==14){
            tvToolbarTitle.setText("核查列表");
        }
        initRefreshLayout();
    }

    /**
     * 初始化刷新
     */
    private void initRefreshLayout() {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null) {
                    if(userInfo!=null){
                        mPresenter.findCaseInfoPageList(false,userInfo.getUserId(),handleType,curNode,caseCode,caseAttribute,casePrimaryCategory,caseSecondaryCategory,caseChildCategory);
                    }
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null) {
//                    mPresenter.findCaseInfoPageList(true, entry_type,caseCode,caseAttribute,casePrimaryCategory,caseSecondaryCategory,caseChildCategory);
                    if(userInfo!=null){
                        mPresenter.findCaseInfoPageList(true,userInfo.getUserId(),handleType,curNode,caseCode,caseAttribute,casePrimaryCategory,caseSecondaryCategory,caseChildCategory);
                    }
                }
            }
        });
        smartRefresh.autoRefresh();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mCaseList = new ArrayList<>();
        mAdapter = new CaseAdapter(mCaseList);
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
            if(isCaseSearch){  //案件查询
                Intent intent = new Intent(this,ProcessActivity.class);
                intent.putExtra("Case",mCaseList.get(position));
                startActivity(intent);
            }else {
                Intent intent = new Intent(HandleActivity.this, HandleDetailActivity.class);
                intent.putExtra("curNode", curNode);
                intent.putExtra("case_id", mCaseList.get(position).getCaseId());
                intent.putExtra("case_attribute", mCaseList.get(position).getCaseAttribute());
//                launchActivity(intent);
                startActivityForResult(intent,1);
            }

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
//            mAdapter.notifyItemRangeInserted(index + 1, caseList.size());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){ //处理 核实  核查 成功
            smartRefresh.autoRefresh();
        }
    }
}
