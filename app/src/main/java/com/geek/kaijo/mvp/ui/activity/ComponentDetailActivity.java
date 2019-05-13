package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.kaijo.mvp.ui.fragment.society.culture.CulturalRelicFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.DrugSafteFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.FoodSafteFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.SpecialCollectionDetailFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.SpecialCollectionFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.TyphoonFloodFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.WHPFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.ZaiJianGongDi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerComponentDetailComponent;
import com.geek.kaijo.di.module.ComponentDetailModule;
import com.geek.kaijo.mvp.contract.ComponentDetailContract;
import com.geek.kaijo.mvp.presenter.ComponentDetailPresenter;

import com.geek.kaijo.R;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.http.PUT;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ComponentDetailActivity extends BaseActivity<ComponentDetailPresenter> implements ComponentDetailContract.View {

    //上传图片
    @BindView(R.id.pictureRecyclerView)
    RecyclerView pictureRecyclerView;
    private List<UploadFile> uploadPhotoList;
    private UploadPhotoAdapter photoAdapter;

    //上传检查记录
    @BindView(R.id.checkRecyclerView)
    RecyclerView checkRecyclerView;
    private List<UploadFile> fileList;
    private UploadPhotoAdapter fileAdapter;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    public ThingPositionInfo thingPositionInfo;
    private GridItemContent subMenu;
    private Fragment fragment;

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
        thingPositionInfo = (ThingPositionInfo)getIntent().getSerializableExtra("ThingPositionInfo");
        subMenu = (GridItemContent) getIntent().getSerializableExtra("Submenu");

        if(thingPositionInfo!=null && subMenu!=null){
            tvToolbarTitle.setText(subMenu.getName()+"详情");
            Gson gson = new Gson();
            uploadPhotoList = gson.fromJson(thingPositionInfo.getPhotos(),new TypeToken<List<UploadFile>>() {}.getType());
            fileList = gson.fromJson(thingPositionInfo.getCheckRecord(),new TypeToken<List<UploadFile>>() {}.getType());
            if("特种设备".equals(subMenu.getName())){
                fragment = new SpecialCollectionDetailFragment();
            }else if("在建工地".equals(subMenu.getName())){
                fragment = new ZaiJianGongDi();
            }else if("危化品".equals(subMenu.getName())){
                fragment = new WHPFragment();
            }else if("食品".equals(subMenu.getName()) || "餐饮".equals(subMenu.getName())){
                fragment = new FoodSafteFragment();
            }else if("药品".equals(subMenu.getName())){
                fragment = new DrugSafteFragment();
            }else if("森林防火".equals(subMenu.getName())){
//                ly_senlinfanhuo = findViewById(R.id.ly_senlinfanhuo);
//                ly_senlinfanhuo.setVisibility(View.VISIBLE);
//                senl_et_name = findViewById(R.id.senl_et_name);
            }else if("防台防汛".equals(subMenu.getName())){
                fragment = new TyphoonFloodFragment();
            }else if("冬季除雪".equals(subMenu.getName())){
//                ly_dongjichuxue = findViewById(R.id.ly_dongjichuxue);
//                ly_dongjichuxue.setVisibility(View.VISIBLE);
//                dongjichuxue_et_address = findViewById(R.id.dongjichuxue_et_address);
//                dongjichuxue_et_isPodao = findViewById(R.id.dongjichuxue_et_isPodao);
//                if (checkList == null) {
//                    checkList = new ArrayList<>();
//                    checkList.add("否");
//                    checkList.add("是");
//                }
//                MySpinnerAdapter<String> mStreetAdapter = new MySpinnerAdapter<>(this, checkList);
//                dongjichuxue_et_isPodao.setAdapter(mStreetAdapter);
            }else if("文明祭祀".equals(subMenu.getName())){
//                ly_wenmingjisi = findViewById(R.id.ly_wenmingjisi);
//                ly_wenmingjisi.setVisibility(View.VISIBLE);
//                jisi_et_address = findViewById(R.id.jisi_et_address);
//                jisi_farenName = findViewById(R.id.jisi_farenName);
//                jisi_phone = findViewById(R.id.jisi_phone);
//                jisi_zerenquRemark = findViewById(R.id.jisi_zerenquRemark);
            }else if("网吧".equals(subMenu.getName())){
//                ly_wangba = findViewById(R.id.ly_wangba);
//                ly_wangba.setVisibility(View.VISIBLE);
//                et_wangba_name = findViewById(R.id.et_wangba_name);
//                et_wangba_adress = findViewById(R.id.et_wangba_adress);
//                wangba_farenName = findViewById(R.id.wangba_farenName);
//                wangba__phone = findViewById(R.id.wangba__phone);
            }else if("文物保护单位".equals(subMenu.getName())){
                fragment = new CulturalRelicFragment();
            }else if("演出场所".equals(subMenu.getName()) || "游艺娱乐".equals(subMenu.getName()) || "演艺娱乐".equals(subMenu.getName())|| "娱乐场所".equals(subMenu.getName())){
//                ly_yanchu = findViewById(R.id.ly_yanchu);
//                ly_yanchu.setVisibility(View.VISIBLE);
//                et_yanchu_name = findViewById(R.id.et_yanchu_name);
//                et_yanchu_adress = findViewById(R.id.et_yanchu_adress);
//                jingyingzheName = findViewById(R.id.jingyingzheName);
//                yanchu__phone = findViewById(R.id.yanchu__phone);
            }
            if(fragment!=null){

                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }

            initRecyclerView();
        }
    }

    private void initRecyclerView(){
        //照片列表
        pictureRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pictureRecyclerView.setHasFixedSize(true);


        //视频列表
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        LinearLayoutManager layoutManager_video_later = new LinearLayoutManager(this);
        checkRecyclerView.addItemDecoration(divider);
        checkRecyclerView.setLayoutManager(layoutManager_video_later);

        photoAdapter = new UploadPhotoAdapter(this,uploadPhotoList);
        pictureRecyclerView.setAdapter(photoAdapter);

        fileAdapter = new UploadPhotoAdapter(this,fileList);
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
