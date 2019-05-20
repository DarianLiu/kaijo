package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.di.component.DaggerComponentDetailComponent;
import com.geek.kaijo.di.module.ComponentDetailModule;
import com.geek.kaijo.mvp.contract.ComponentDetailContract;
import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.presenter.ComponentDetailPresenter;
import com.geek.kaijo.mvp.ui.adapter.PhotoAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.kaijo.mvp.ui.fragment.society.culture.CulturalRelicDetailFragment;
import com.geek.kaijo.mvp.ui.fragment.society.culture.CulturalRelicFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.DrugSafteDetailFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.DrugSafteFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.FoodSafteDetailFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.FoodSafteFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.SpecialCollectionDetailFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.TyphoonFloodDetailFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.TyphoonFloodFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.WHPDetailFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.WHPFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.ZaiJianGongDi;
import com.geek.kaijo.mvp.ui.fragment.society.safety.ZaiJianGongDiDetailFragent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ComponentDetailActivity extends BaseActivity<ComponentDetailPresenter> implements ComponentDetailContract.View {

    //上传图片
    @BindView(R.id.pictureRecyclerView)
    RecyclerView pictureRecyclerView;
    private List<UploadFile> uploadPhotoList;
    private PhotoAdapter photoAdapter;

    //上传检查记录
    @BindView(R.id.checkRecyclerView)
    RecyclerView checkRecyclerView;
    private List<UploadFile> fileList;
    private PhotoAdapter fileAdapter;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_location_longitude)
    TextView tv_location_longitude;
    @BindView(R.id.tv_location_latitude)
    TextView tv_location_latitude;
    @BindView(R.id.la_check_record)
    LinearLayout la_check_record;
//    @BindView(R.id.spinner_street)
//    TextView spinner_street;  //街道
//    @BindView(R.id.spinner_community)
//    TextView spinner_community;  //社区


    public ThingPositionInfo thingPositionInfo;
    private GridItemContent subMenu;
    private Fragment fragment;

    /*森林防火*/
    private LinearLayout ly_senlinfanhuo;
    private TextView senl_et_name;

    /*冬季除雪*/
    private LinearLayout ly_dongjichuxue;
    private TextView dongjichuxue_et_address; //具体地址
    private TextView dongjichuxue_et_isPodao; //是否为破路

    /*文明祭祀*/
    private LinearLayout ly_wenmingjisi;
    private TextView jisi_et_address; //祭扫地点
    private TextView jisi_farenName; //责任人
    private TextView jisi_phone; //联系电话
    private TextView jisi_zerenquRemark; //请输入责任区扫描

    /*网吧*/
    private LinearLayout ly_wangba;
    private TextView et_wangba_name; //名称
    private TextView et_wangba_adress; //地址
    private TextView wangba_farenName; //法定代表人
    private TextView wangba__phone; //联系电话

    /*演出场所*/
    private LinearLayout ly_yanchu;
    private TextView et_yanchu_name; //名称
    private TextView et_yanchu_adress; //地址
    private TextView jingyingzheName; //经营者名称
    private TextView yanchu__phone; //联系电话


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerComponentDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .componentDetailModule(new ComponentDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_component_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        thingPositionInfo = (ThingPositionInfo) getIntent().getSerializableExtra("ThingPositionInfo");
        subMenu = (GridItemContent) getIntent().getSerializableExtra("Submenu");

        if (thingPositionInfo != null && subMenu != null) {
            tvToolbarTitle.setText(subMenu.getName() + "详情");
            tv_location_longitude.setText(thingPositionInfo.getLng()+"");
            tv_location_latitude.setText(thingPositionInfo.getLat()+"");
            if(!TextUtils.isEmpty(thingPositionInfo.getName())){
                tv_name.setText(thingPositionInfo.getName());
            }else if(!TextUtils.isEmpty(thingPositionInfo.getDanweiName())){
                tv_name.setText(thingPositionInfo.getDanweiName());
            }else if(!TextUtils.isEmpty(thingPositionInfo.getJingyingzheName())){
                tv_name.setText(thingPositionInfo.getJingyingzheName());  //经营者名称
            }else if(!TextUtils.isEmpty(thingPositionInfo.getAddress())){
                tv_name.setText(thingPositionInfo.getAddress());  //地址
            }
            tv_time.setText(DateUtils.getDateToString(thingPositionInfo.getCreateTime(),DateUtils.dateString3));
            Gson gson = new Gson();
            uploadPhotoList = gson.fromJson(thingPositionInfo.getPhotos(), new TypeToken<List<UploadFile>>() {}.getType());
            fileList = gson.fromJson(thingPositionInfo.getCheckRecord(), new TypeToken<List<UploadFile>>() {}.getType());

            if ("特种设备".equals(subMenu.getName())) {
                fragment = new SpecialCollectionDetailFragment();
            } else if ("在建工地".equals(subMenu.getName())) {
                fragment = new ZaiJianGongDiDetailFragent();
            } else if ("危化品".equals(subMenu.getName())) {
                fragment = new WHPDetailFragment();
            } else if ("食品".equals(subMenu.getName()) || "餐饮".equals(subMenu.getName())) {
                fragment = new FoodSafteDetailFragment();
            } else if ("药品".equals(subMenu.getName())) {
                fragment = new DrugSafteDetailFragment();
            } else if ("森林防火".equals(subMenu.getName())) {
                ly_senlinfanhuo = findViewById(R.id.ly_senlinfanhuo);
                ly_senlinfanhuo.setVisibility(View.VISIBLE);
                senl_et_name = findViewById(R.id.senl_et_name);
                if(thingPositionInfo!=null){
                    senl_et_name.setText(thingPositionInfo.getName());
                }
                la_check_record.setVisibility(View.GONE);
            } else if ("防台防汛".equals(subMenu.getName())) {
                fragment = new TyphoonFloodDetailFragment();
                la_check_record.setVisibility(View.GONE);
            } else if ("冬季除雪".equals(subMenu.getName())) {
                ly_dongjichuxue = findViewById(R.id.ly_dongjichuxue);
                ly_dongjichuxue.setVisibility(View.VISIBLE);
                dongjichuxue_et_address = findViewById(R.id.dongjichuxue_et_address);
                dongjichuxue_et_isPodao = findViewById(R.id.dongjichuxue_et_isPodao);

                if(thingPositionInfo!=null){
                    dongjichuxue_et_address.setText(thingPositionInfo.getAddress());
                    if("是".equals(thingPositionInfo.getIsPodao())){
                        dongjichuxue_et_isPodao.setText("是");
                    }else {
                        dongjichuxue_et_isPodao.setText("否");
                    }
                }
                la_check_record.setVisibility(View.GONE);
            } else if ("文明祭祀".equals(subMenu.getName())) {
                ly_wenmingjisi = findViewById(R.id.ly_wenmingjisi);
                ly_wenmingjisi.setVisibility(View.VISIBLE);
                jisi_et_address = findViewById(R.id.jisi_et_address);
                jisi_farenName = findViewById(R.id.jisi_farenName);
                jisi_phone = findViewById(R.id.jisi_phone);
                jisi_zerenquRemark = findViewById(R.id.jisi_zerenquRemark);
                if(thingPositionInfo!=null){
                    jisi_et_address.setText(thingPositionInfo.getAddress());
                    jisi_farenName.setText(thingPositionInfo.getFarenName());
                    jisi_phone.setText(thingPositionInfo.getContact());
                    jisi_zerenquRemark.setText(thingPositionInfo.getZerenquRemark());

                }
                la_check_record.setVisibility(View.GONE);
            } else if ("网吧".equals(subMenu.getName())) {
                ly_wangba = findViewById(R.id.ly_wangba);
                ly_wangba.setVisibility(View.VISIBLE);
                et_wangba_name = findViewById(R.id.et_wangba_name);
                et_wangba_adress = findViewById(R.id.et_wangba_adress);
                wangba_farenName = findViewById(R.id.wangba_farenName);
                wangba__phone = findViewById(R.id.wangba__phone);
                if(thingPositionInfo!=null){
                    et_wangba_name.setText(thingPositionInfo.getName());
                    et_wangba_adress.setText(thingPositionInfo.getAddress());
                    wangba_farenName.setText(thingPositionInfo.getFarenName());
                    wangba__phone.setText(thingPositionInfo.getContact());

                }
            } else if ("文物保护单位".equals(subMenu.getName())) {
                fragment = new CulturalRelicDetailFragment();
            } else if ("演出场所".equals(subMenu.getName()) || "游艺娱乐".equals(subMenu.getName()) || "演艺娱乐".equals(subMenu.getName()) || "娱乐场所".equals(subMenu.getName())) {
                ly_yanchu = findViewById(R.id.ly_yanchu);
                ly_yanchu.setVisibility(View.VISIBLE);
                et_yanchu_name = findViewById(R.id.et_yanchu_name);
                et_yanchu_adress = findViewById(R.id.et_yanchu_adress);
                jingyingzheName = findViewById(R.id.jingyingzheName);
                yanchu__phone = findViewById(R.id.yanchu__phone);
                if(thingPositionInfo!=null){
                    et_yanchu_name.setText(thingPositionInfo.getName());
                    et_yanchu_adress.setText(thingPositionInfo.getAddress());
                    jingyingzheName.setText(thingPositionInfo.getJingyingzheName());
                    yanchu__phone.setText(thingPositionInfo.getContact());

                }
            }
            if (fragment != null) {

                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        //照片列表
        pictureRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //视频列表
//        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        LinearLayoutManager layoutManager_video_later = new GridLayoutManager(this,2);
//        checkRecyclerView.addItemDecoration(divider);
        checkRecyclerView.setLayoutManager(layoutManager_video_later);

        photoAdapter = new PhotoAdapter(this, uploadPhotoList);
        pictureRecyclerView.setAdapter(photoAdapter);

        fileAdapter = new PhotoAdapter(this, fileList);
        checkRecyclerView.setAdapter(fileAdapter);

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
}
