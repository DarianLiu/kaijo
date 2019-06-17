package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.mvp.model.entity.Nodes;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerNodesAddComponent;
import com.geek.kaijo.mvp.contract.NodesAddContract;
import com.geek.kaijo.mvp.presenter.NodesAddPresenter;

import com.geek.kaijo.R;
import com.jess.arms.utils.DataHelper;


import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/14/2019 14:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class NodesAddActivity extends BaseActivity<NodesAddPresenter> implements NodesAddContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView tv_toolbar_title_right;
    @BindView(R.id.btn_next)
    TextView btn_next;
    @BindView(R.id.btn_cancel)
    TextView btn_cancel;
    @BindView(R.id.et_content)
    EditText et_content;
    private LoadingProgressDialog loadingDialog;
    private Nodes nodes;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNodesAddComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_nodes_add; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText("添加");
        nodes = (Nodes) getIntent().getSerializableExtra("Nodes");
        if(nodes!=null){
            et_content.setText(nodes.getContent());
        }
        UserInfo userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_content.getText().toString().trim())){
                    return;
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", userInfo.getUserId());
                jsonObject.addProperty("content", et_content.getText().toString().trim());
                if(nodes!=null){
                    jsonObject.addProperty("notepadId", nodes.getNotepadId());
                }

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                        new Gson().toJson(jsonObject));
                mPresenter.httpSaveOrUpdateNotepad(requestBody);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    public void httpSaveOrUpdateNotepadSuccess(Nodes nodes) {
        this.finish();
    }
}
