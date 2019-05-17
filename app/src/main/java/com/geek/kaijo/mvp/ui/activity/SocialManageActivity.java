package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.EventBusTags;
import com.geek.kaijo.di.component.DaggerSocialManageComponent;
import com.geek.kaijo.di.module.SocialManageModule;
import com.geek.kaijo.mvp.contract.SocialManageContract;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.model.event.ServiceEvent;
import com.geek.kaijo.mvp.model.event.ThingEvent;
import com.geek.kaijo.mvp.presenter.SocialManagePresenter;
import com.geek.kaijo.mvp.ui.activity.society.safety.SpecialCollectionActivity;
import com.geek.kaijo.mvp.ui.adapter.SocialThingAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 配件
 */
public class SocialManageActivity extends BaseActivity<SocialManagePresenter> implements SocialManageContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView ivAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private LoadingProgressDialog loadingDialog;
    private long assortId;
    private int thingType;
    private UserInfo userInfo;
    private List<ThingPositionInfo> mDatas;
    private SocialThingAdapter mAdapter;
    private GridItemContent subMenu;

    String name = "";
    String danweiName = "";
    String jingyingzheName = "";
    String address = "";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSocialManageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .socialManageModule(new SocialManageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_social_manage; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        assortId = getIntent().getIntExtra("assortId", 0);
//        thingType = getIntent().getIntExtra("thingType", 0);
        EventBus.getDefault().register(this);//注册EventBus 那个窗口接收 在哪个窗口注册
        subMenu = (GridItemContent) getIntent().getSerializableExtra("Submenu");
        if(subMenu!=null){
            tvToolbarTitle.setText(subMenu.getName());
        }

        ivAdd.setImageDrawable(getResources().getDrawable(R.drawable.icon_add));
        ivAdd.setVisibility(View.VISIBLE);
        ivAdd.setOnClickListener(v -> {
            Intent intents = new Intent();
            intents.setClass(this, SpecialCollectionActivity.class);
            intents.putExtra("Submenu", subMenu);
            startActivityForResult(intents,1);
        });

        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);

        initRecycleView();
        initRefreshLayout();
        smartRefresh.autoRefresh();

    }

    /**
     * 初始化刷新
     */
    private void initRefreshLayout() {
        smartRefresh.setEnableLoadMoreWhenContentNotFull(false);
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.findThingPositionList(false, subMenu.getName(), name,danweiName,
                            jingyingzheName, address);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.findThingPositionList(true, subMenu.getName(), name,danweiName,
                            jingyingzheName, address);
            }
        });
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatas = new ArrayList<>();
        mAdapter = new SocialThingAdapter(mDatas, thingType);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new SocialThingAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position>0){
                    Intent intent = new Intent(SocialManageActivity.this,ComponentDetailActivity.class);
                    intent.putExtra("ThingPositionInfo",mDatas.get(position-1));
                    intent.putExtra("Submenu",subMenu);
                    SocialManageActivity.this.startActivityForResult(intent,1);
                }
            }
        });
    }

    @Subscriber
    public void receiveThingEvent(ThingEvent event) {
        if(event.getPosition()==0)return;
        Intent intent;
        switch (event.getEventType()) {
            case 1://编辑
                intent = new Intent(this, SpecialCollectionActivity.class);
                intent.putExtra("ThingPositionInfo", mDatas.get(event.getPosition()-1));
                intent.putExtra("Submenu", subMenu);
                startActivityForResult(intent, 1);
//                EventBus.getDefault().clear();
                break;
            case 2://删除
                if (mPresenter != null){
                    mPresenter.httpDeleteInfo(String.valueOf(mDatas.get(event.getPosition()-1).getThingPositionId()));
                }
                break;
        }
    }

    /**
     * 搜索
     * @param event
     */
    @Subscriber
    public void receiveServiceEvent(ServiceEvent event) {
        if(event.getCategoryId()==3){

            String menu = subMenu.getName();
            if ("特种设备".equals(menu)) {
                danweiName = event.getKey();
            } else if ("在建工地".equals(menu)||"危化品".equals(menu)||"药品".equals(menu)||"森林防火".equals(menu)||"网吧".equals(menu)||"文物保护单位".equals(menu)||"演出场所".equals(menu)
                    ||"游艺娱乐".equals(menu)||"演艺娱乐".equals(menu)||"娱乐场所".equals(menu)   ) {
                name = event.getKey();
            } else if ("食品".equals(subMenu.getName()) || "餐饮".equals(subMenu.getName())) {
                jingyingzheName = event.getKey();
            }else if ("防台防汛".equals(subMenu.getName()) || "冬季除雪".equals(subMenu.getName()) || "文明祭祀".equals(subMenu.getName())) {
                address  = event.getKey();
            }
            if (mPresenter != null)
                mPresenter.findThingPositionList(true, subMenu.getName(), name,danweiName,
                        jingyingzheName, address);
        }

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
    public void refreshData(List<ThingPositionInfo> datas) {
        smartRefresh.setNoMoreData(false);
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreData(List<ThingPositionInfo> datas) {
        if (datas != null && smartRefresh != null && datas.size() == 0) {
            smartRefresh.finishLoadMoreWithNoMoreData();
            return;
        }

        if (datas != null) {
            int index = mDatas.size();
            mDatas.addAll(datas);
            mAdapter.notifyItemRangeInserted(index + 1, datas.size());
        }
    }

    /**
     * 删除成功
     */
    @Override
    public void httpDeleteInfoSuccess() {
        smartRefresh.autoRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){
            smartRefresh.autoRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册 根据业务需要填写
    }
}
