package com.geek.kaijo.mvp.ui.activity.society.safety;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerSpecialCollectionComponent;
import com.geek.kaijo.mvp.contract.SpecialCollectionContract;
import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.SpecialCollectionPresenter;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 特种设备采集
 */
public class SpecialCollectionActivity extends BaseActivity<SpecialCollectionPresenter> implements SpecialCollectionContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.spinner_street)
    AppCompatSpinner spinner_street;//街道spinner
    private List<Street> mStreetList;//街道列表
    private MySpinnerAdapter<Street> mStreetAdapter;//街道adapter

    @BindView(R.id.spinner_community)
    AppCompatSpinner spinner_community;//社区spiner
    private List<Street> mCommunityList;//社区列表
    private MySpinnerAdapter<Street> mCommunityAdapter;//社区adapter
    private UserInfo userInfo;

    @BindView(R.id.btn_location_obtain)
    TextView btn_location_obtain;
    @BindView(R.id.ly_addView)
    LinearLayout ly_addView;

    private GridItemContent subMenu;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSpecialCollectionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_special_collection; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        subMenu = (GridItemContent) getIntent().getSerializableExtra("Submenu");
        if(subMenu!=null){
            tvToolbarTitle.setText(subMenu.getName()+"采集");
        }

        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        initSpinnerStreetGrid();
        if (mPresenter != null) {
            mPresenter.findAllStreetCommunity(0);
        }
    }

    /**
     * 初始化街道社区网格Spinner
     */
    private void initSpinnerStreetGrid() {
        mStreetList = new ArrayList<>();
        mStreetAdapter = new MySpinnerAdapter<>(this, mStreetList);
        spinner_street.setAdapter(mStreetAdapter);
        spinner_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long checkId) {
                spinner_community.setSelection(0);
                mCommunityList.clear();
                mCommunityList.addAll(mStreetList.get(position).getChildList());
                if(userInfo!=null && !TextUtils.isEmpty(userInfo.getStreetName())){
                    if(mCommunityList!=null){
                        for(int i=0;i<mCommunityList.size();i++){
                            if(userInfo.getCommunityName().equals(mCommunityList.get(i).getName())){
                                spinner_community.setSelection(i,true);
                                break;
                            }
                        }
                    }
                }
                mCommunityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCommunityList = new ArrayList<>();
        mCommunityAdapter = new MySpinnerAdapter<>(this, mCommunityList);
        spinner_community.setAdapter(mCommunityAdapter);
        spinner_community.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinnerCaseGrid.setSelection(0);
//                mGridList.clear();
//                mGridList.add(mGrid);
//                mGridAdapter.notifyDataSetChanged();
//                if (position > 0) {
//                    mCommunityId = mCommunityList.get(position).getId();
//                    if (mPresenter != null) {
//                        mPresenter.findGridListByCommunityId(mCommunityList.get(position).getId());
//                    }
//                } else {
//                    mCommunityId = "";
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    public void httpStreetCommunitySuccess(List<Street> result) {
        mStreetList.clear();
        mStreetList.addAll(result.get(0).getChildList());
        if(userInfo!=null && !TextUtils.isEmpty(userInfo.getStreetName())){
            if(mStreetList!=null){
                for(int i=0;i<mStreetList.size();i++){
                    if(userInfo.getStreetName().equals(mStreetList.get(i).getName())){
                        spinner_street.setSelection(i,true);
                        break;
                    }
                }
            }
        }
        mStreetAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_location_obtain,R.id.btn_submit_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_location_obtain:  //删除
                addComponentView();
                break;
            case R.id.btn_submit_pic:
                pictureSelector();
                break;
        }
    }

    private void addComponentView(){
        View view = getLayoutInflater().inflate(R.layout.component_include_add,null);
        AppCompatSpinner spinner_check = view.findViewById(R.id.spinner_check);
//        mStreetList = new ArrayList<>();
//        mStreetAdapter = new MySpinnerAdapter<>(this, mStreetList);
//        spinner_street.setAdapter(mStreetAdapter);
//        spinner_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long checkId) {
//                spinner_community.setSelection(0);
//                mCommunityList.clear();
//                mCommunityList.addAll(mStreetList.get(position).getChildList());
//                if(userInfo!=null && !TextUtils.isEmpty(userInfo.getStreetName())){
//                    if(mCommunityList!=null){
//                        for(int i=0;i<mCommunityList.size();i++){
//                            if(userInfo.getCommunityName().equals(mCommunityList.get(i).getName())){
//                                spinner_community.setSelection(i,true);
//                                break;
//                            }
//                        }
//                    }
//                }
//                mCommunityAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        TextView tv_delete = view.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly_addView.removeView((View) view.getParent());
            }
        });
        ly_addView.addView(view);
    }


    /**
     * 头像选择 PictureSelector
     */
    private void pictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .minimumCompressSize(100)
                .withAspectRatio(1, 1)
                .showCropFrame(true)
                .rotateEnabled(true)
                .isDragFrame(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
