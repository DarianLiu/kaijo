package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.di.component.DaggerHandleDetailComponent;
import com.geek.kaijo.di.module.HandleDetailModule;
import com.geek.kaijo.mvp.contract.HandleDetailContract;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.presenter.HandleDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 处理详情
 */
public class HandleDetailActivity extends BaseActivity<HandleDetailPresenter> implements HandleDetailContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_case_attribute)
    TextView tvCaseAttribute;
    @BindView(R.id.tv_category_large)
    TextView tvCategoryLarge;
    @BindView(R.id.tv_category_small)
    TextView tvCategorySmall;
    @BindView(R.id.tv_category_sub)
    TextView tvCategorySub;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_view_location)
    TextView tvViewLocation;
    @BindView(R.id.tv_case_address)
    TextView tvCaseAddress;
    @BindView(R.id.tv_case_describe)
    TextView tvCaseDescribe;
    @BindView(R.id.tv_View_handle_process)
    TextView tvViewHandleProcess;
    @BindView(R.id.et_situation_description)
    EditText etSituationDescription;
    @BindView(R.id.tv_upload_image)
    TextView tvUploadImage;
    @BindView(R.id.ll_img_list)
    LinearLayout llImgList;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private TextView tvImageList;//图片列表提示文字
    int curNode;  //12: 案件处理13: 案件核实14: 案件核查

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHandleDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .handleDetailModule(new HandleDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_handle_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        curNode = getIntent().getIntExtra("curNode", 0);
        String caseId = getIntent().getStringExtra("case_id");
        String caseAttribute = getIntent().getStringExtra("case_attribute");
        if (mPresenter != null) {
            mPresenter.findCaseInfoByMap(caseId, caseAttribute);
        }
        if(curNode==12){
            tvToolbarTitle.setText("处理详情页");
        }else if(curNode==13){
            tvToolbarTitle.setText("核实详情页");
        }else if(curNode==14){
            tvToolbarTitle.setText("核查详情页");
        }

        tvImageList = new TextView(this);
        tvImageList.setText("照片列表");
        tvImageList.setTextSize(18);
        tvImageList.setTextColor(getResources().getColor(R.color.color_text_black));
        tvImageList.setGravity(Gravity.CENTER);
        llImgList.addView(tvImageList);
    }

    @Override
    public void updateView(Case data) {
        tvCaseAttribute.setText(Html.fromHtml("<b>案件属性：</b>" + data.getCasePrimaryCategory()));
        tvCategoryLarge.setText(Html.fromHtml("<b>大类：</b>" + data.getCasePrimaryCategory()));
        tvCategorySmall.setText(Html.fromHtml("<b>小类：</b>" + data.getCaseSecondaryCategory()));
        tvCategorySub.setText(Html.fromHtml("<b>子类：</b>" + data.getCaseChildCategory()));
        tvLocation.setText(Html.fromHtml("<b>位置：</b>地图查看"));

        tvCaseAddress.setText(Html.fromHtml("<b>地址：</b>" + data.getAddress()));
        tvCaseDescribe.setText(Html.fromHtml("<b>描述：</b>" + data.getDescription()));
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
    protected void onDestroy() {
        llImgList.removeAllViews();
        super.onDestroy();
        tvImageList = null;
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick({R.id.tv_view_location, R.id.tv_View_handle_process, R.id.tv_upload_image, R.id.tv_sign_in, R.id.tv_submit, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_view_location://查看坐标
                break;
            case R.id.tv_View_handle_process://查看处理过程
                break;
            case R.id.tv_upload_image://上传照片
                break;
            case R.id.tv_sign_in://签收
                break;
            case R.id.tv_submit://提交
                break;
            case R.id.tv_cancel://取消
                break;
        }
    }
}
