package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerInspectionProjectManagerComponent;
import com.geek.kaijo.di.module.InspectionProjectManagerModule;
import com.geek.kaijo.mvp.contract.InspectionProjectManagerContract;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.Thing;
import com.geek.kaijo.mvp.model.event.ThingEvent;
import com.geek.kaijo.mvp.presenter.InspectionProjectManagerPresenter;
import com.geek.kaijo.mvp.ui.adapter.ThingManagerAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class InspectionProjectManagerActivity extends BaseActivity<InspectionProjectManagerPresenter> implements InspectionProjectManagerContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private List<Inspection> mList;
    private ThingManagerAdapter mAdapter;
    private LoadingProgressDialog loadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInspectionProjectManagerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inspectionProjectManagerModule(new InspectionProjectManagerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inspection_project_manager; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
//            launchActivity(new Intent(this, InspectionAddActivity.class));
            startActivityForResult(new Intent(this, InspectionAddActivity.class),1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText(R.string.inspection_project_manager);
        EventBus.getDefault().register(this);//注册EventBus 那个窗口接收 在哪个窗口注册
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册 根据业务需要填写
    }

    /**
     * 初始化View
     */
    private void initView() {
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null) {
                    mPresenter.getInspectionProjectList(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (smartRefresh != null)
                    smartRefresh.setNoMoreData(false);
                if (mPresenter != null) {
                    mPresenter.getInspectionProjectList(true);
                }
            }
        });
        smartRefresh.autoRefresh();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();
        mAdapter = new ThingManagerAdapter(mList);
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {

        });
        recyclerView.setAdapter(mAdapter);
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

    @Subscriber
    public void receiveThingEvent(ThingEvent event) {
        Intent intent;
        switch (event.getEventType()) {
            case 1://编辑
                intent = new Intent(this, InspectionAddActivity.class);
                intent.putExtra("Inspection", mList.get(event.getPosition()));
                startActivityForResult(intent,1);
//                EventBus.getDefault().clear();
                break;
            case 2://删除
                if (mPresenter != null)
                    mPresenter.delThings(new int[]{event.getPosition()},
                            new String[]{mList.get(event.getPosition()).getThingId()+""});
//                EventBus.getDefault().clear();
                break;
            case 3://查看坐标
                intent = new Intent(this, MapActivity.class);
                intent.putExtra("lat", mList.get(event.getPosition()).getLat());
                intent.putExtra("lng", mList.get(event.getPosition()).getLng());
                intent.putExtra(Constant.MAP_LOOK,Constant.MAP_LOOK);
                startActivity(intent);
//                EventBus.getDefault().clear();
                break;
        }
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
    public void updateInspectionProjectList(boolean isRefresh, List<Inspection> list) {
        if (isRefresh) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        } else {
            int index = mList.size();
            mList.addAll(list);
            mAdapter.notifyItemRangeInserted(index, list.size());
        }

        if (list.size() < 10) {
            if (smartRefresh != null)
                smartRefresh.setNoMoreData(true);
        }
    }

    @Override
    public void delThings(int[] positions) {
        for (int i : positions) {
            mList.remove(i);
            mAdapter.notifyItemRemoved(i);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){
            smartRefresh.autoRefresh();
        }
    }
}
