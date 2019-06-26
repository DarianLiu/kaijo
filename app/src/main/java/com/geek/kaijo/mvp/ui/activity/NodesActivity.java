package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.mvp.model.entity.Nodes;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.ui.adapter.NodesAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerNodesComponent;
import com.geek.kaijo.mvp.contract.NodesContract;
import com.geek.kaijo.mvp.presenter.NodesPresenter;

import com.geek.kaijo.R;
import com.jess.arms.utils.DataHelper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/13/2019 15:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class NodesActivity extends BaseActivity<NodesPresenter> implements NodesContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView tv_toolbar_title_right;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Nodes> nodesList;
    private UserInfo userInfo;
    private NodesAdapter adapter;
    private LoadingProgressDialog loadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNodesComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_nodes; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText("记事本列表");
        tv_toolbar_title_right.setVisibility(View.VISIBLE);
        tv_toolbar_title_right.setImageResource(R.mipmap.icon_node);
        tv_toolbar_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NodesActivity.this,NodesAddActivity.class);
                startActivityForResult(intent,1);
            }
        });

        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        initSmartRefresh();

        nodesList = new ArrayList<>();
        adapter = new NodesAdapter(this,nodesList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemOnClilcklisten(new NodesAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(NodesActivity.this,NodesAddActivity.class);
                intent.putExtra("Nodes",nodesList.get(position));
                startActivityForResult(intent,1);
            }

            @Override
            public void onEditeClick(View v, int position) {
                Intent intent = new Intent(NodesActivity.this,NodesAddActivity.class);
                intent.putExtra("Nodes",nodesList.get(position));
                startActivityForResult(intent,1);
            }

            @Override
            public void onDeleteClick(View v, int position) {
                if(mPresenter!=null){
//                    JsonObject jsonObject = new JsonObject();
//                    jsonObject.addProperty("notepadId", nodesList.get(position).getNotepadId());
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(jsonObject));
                    mPresenter.httpDelNotepad(nodesList.get(position).getNotepadId(),position);
                }
            }
        });

        refreshLayout.autoRefresh();
    }

    private void initSmartRefresh() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (mPresenter != null)
                    mPresenter.httpFindNotepadList(true,userInfo.getUserId());

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.httpFindNotepadList(false,userInfo.getUserId());
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
        if (refreshLayout != null)
            refreshLayout.finishRefresh();
    }

    @Override
    public void finishLoadMore() {
        if (refreshLayout != null)
            refreshLayout.finishLoadMore();
    }

    @Override
    public void refreshData(List<Nodes> datas) {
        nodesList.clear();
        nodesList.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreData(List<Nodes> datas) {
        if (datas != null) {
            int index = nodesList.size();
            nodesList.addAll(datas);
//            adapter.notifyItemRangeInserted(index + 1, datas.size());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void httpDelNotepadSuccess(Nodes nodes,int position) {
//        adapter.notifyItemRemoved(position);
      //  refreshLayout.autoRefresh();
    }

    @Override
    public void httpDelNotepadSuccess(int position) { //删除成功
        nodesList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position,nodesList.size());
//        refreshLayout.autoRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){
            refreshLayout.autoRefresh();
        }
    }
}
