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
import com.geek.kaijo.mvp.model.event.ThingEvent;
import com.geek.kaijo.mvp.presenter.SocialManagePresenter;
import com.geek.kaijo.mvp.ui.activity.society.culture.CulturalRelicActivity;
import com.geek.kaijo.mvp.ui.activity.society.culture.EntertainmentActivity;
import com.geek.kaijo.mvp.ui.activity.society.culture.InternetBarActivity;
import com.geek.kaijo.mvp.ui.activity.society.culture.PerformanceActivity;
import com.geek.kaijo.mvp.ui.activity.society.culture.RecreationalActivity;
import com.geek.kaijo.mvp.ui.activity.society.emergency.ForestFireActivity;
import com.geek.kaijo.mvp.ui.activity.society.emergency.SacrificeActivity;
import com.geek.kaijo.mvp.ui.activity.society.emergency.TyphoonFloodActivity;
import com.geek.kaijo.mvp.ui.activity.society.emergency.WinterSnowActivity;
import com.geek.kaijo.mvp.ui.activity.society.foot.DrugSafetyActivity;
import com.geek.kaijo.mvp.ui.activity.society.foot.FoodSafteActivity;
import com.geek.kaijo.mvp.ui.activity.society.safety.BuilderSiteActivity;
import com.geek.kaijo.mvp.ui.activity.society.safety.SocialProductDangerActivity;
import com.geek.kaijo.mvp.ui.activity.society.safety.SpecialCollectionActivity;
import com.geek.kaijo.mvp.ui.adapter.SocialThingAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
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
            switch (thingType) {
                case 3://药品
                    intents.setClass(this, DrugSafetyActivity.class);
                    intents.putExtra("title", "药品");
                    break;
                case 4://食品
                    intents.setClass(this, FoodSafteActivity.class);
                    intents.putExtra("title", "食品");
                    break;
                case 5://特种设备
                    intents.setClass(this, SpecialCollectionActivity.class);
                    intents.putExtra("title", "特种设备");
                    break;
                case 6://在建工地
                    intents.setClass(this, BuilderSiteActivity.class);
                    intents.putExtra("title", "在建工地");
                    break;
                case 7://危化品
                    intents.setClass(this, SocialProductDangerActivity.class);
                    intents.putExtra("title", "危化品");
                    break;
                case 8://森森防火
                    intents.setClass(this, ForestFireActivity.class);
                    intents.putExtra("title", "危化品");
                    break;
                case 9://防台防汛
                    intents.setClass(this, TyphoonFloodActivity.class);
                    intents.putExtra("title", "森森防火");
                    break;
                case 10://文明祭祀
                    intents.setClass(this, SacrificeActivity.class);
                    intents.putExtra("title", "防台防汛");
                    break;
                case 11://冬季除雪
                    intents.setClass(this, WinterSnowActivity.class);
                    intents.putExtra("title", "文明祭祀");
                    break;
                case 12://网吧
                    intents.setClass(this, InternetBarActivity.class);
                    intents.putExtra("title", "网吧");
                    break;
                case 13://文物保护单位
                    intents.setClass(this, CulturalRelicActivity.class);
                    intents.putExtra("title", "文物保护单位");
                    break;
                case 14://演出场所
                    intents.setClass(this, PerformanceActivity.class);
                    intents.putExtra("title", "演出场所");
                    break;
                case 15://游艺娱乐
                    intents.setClass(this, RecreationalActivity.class);
                    intents.putExtra("title", "游艺娱乐");
                    break;
                case 16://娱乐场所
                    intents.setClass(this, EntertainmentActivity.class);
                    intents.putExtra("title", "娱乐场所");
                    break;
                default:
                    intents.setClass(this, SpecialCollectionActivity.class);
                    intents.putExtra("Submenu", subMenu);
                    break;
            }
//            Intent intent = new Intent(SocialManageActivity.this, SocialProductDangerActivity.class);
//            intent.putExtra("entry_type", assortId);
//            intent.putExtra("title", ((ServiceBean) data).getTitle());
//            intent.putExtra("content", ((ServiceBean) data).getContent());
            startActivityForResult(intents,1);
//            launchActivity(intents);
        });

        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);

        initTitle();
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
                    mPresenter.findThingPositionList(false, subMenu.getName(), "","",
                            "", "");
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null)
                    mPresenter.findThingPositionList(true, subMenu.getName(), "","",
                            "", "");
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

        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent(SocialManageActivity.this,ComponentDetailActivity.class);
                intent.putExtra("ThingPositionInfo",mDatas.get(position));
                intent.putExtra("Submenu",subMenu);
                startActivityForResult(intent,11);
            }
        });
    }

    /**
     * 初始化标题
     * 药品 3
     * 餐饮 4
     * 特种设备 5
     * 在建工地 6
     * 危化品 7
     * 森森防火 8
     * 防台防汛 9
     * 文明祭祀 10
     * 冬季除雪 11
     * 网吧 12
     * 文物保护单位 13
     * 演出场所 14
     * 游艺娱乐 15
     * 娱乐场所 16
     */
    private void initTitle() {
        switch (thingType) {
            case 3://药品
                tvToolbarTitle.setText(R.string.social_fad_drug);
                break;
            case 4://餐饮
                tvToolbarTitle.setText(R.string.social_fad_foot);
                break;
            case 5://特种设备
                tvToolbarTitle.setText(R.string.social_product_device);
                break;
            case 6://在建工地
                tvToolbarTitle.setText(R.string.social_product_work_site);
                break;
            case 7://危化品
                tvToolbarTitle.setText(R.string.social_product_danger);
                break;
            case 8://森森防火
                tvToolbarTitle.setText(R.string.social_prevent_forest_fire);
                break;
            case 9://防台防汛
                tvToolbarTitle.setText(R.string.social_prevent_flood);
                break;
            case 10://文明祭祀
                tvToolbarTitle.setText(R.string.social_prevent_sacrifice);
                break;
            case 11://冬季除雪
                tvToolbarTitle.setText(R.string.social_prevent_snow_removal);
                break;
            case 12://网吧
                tvToolbarTitle.setText(R.string.social_cultura_internet_bar);
                break;
            case 13://文物保护单位
                tvToolbarTitle.setText(R.string.social_cultura_relic_protect);
                break;
            case 14://演出场所
                tvToolbarTitle.setText(R.string.social_cultura_performing_place);
                break;
            case 15://游艺娱乐
                tvToolbarTitle.setText(R.string.social_cultura_recreation);
                break;
            case 16://娱乐场所
                tvToolbarTitle.setText(R.string.social_cultura_place);
                break;
        }
    }


    @Subscriber
    public void receiveThingEvent(ThingEvent event) {
        Log.i(this.getClass().getName(), "111111111111111111111111111111event.getPosition()===" + event.getPosition());
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
