package com.geek.kaijo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerMyMessageComponent;
import com.geek.kaijo.di.module.MyMessageModule;
import com.geek.kaijo.mvp.contract.MyMessageContract;
import com.geek.kaijo.mvp.presenter.MyMessagePresenter;
import com.geek.kaijo.mvp.ui.activity.ChangePasswordActivity;
import com.geek.kaijo.mvp.ui.activity.LoginActivity;
import com.geek.kaijo.mvp.ui.activity.PlanViewActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 我的消息
 */
public class MyMessageFragment extends BaseFragment<MyMessagePresenter> implements MyMessageContract.View {

    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    @BindView(R.id.tv_login_out)
    TextView tvLoginOut;
    @BindView(R.id.tv_view_plan)
    TextView tvPlanview;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMyMessageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myMessageModule(new MyMessageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_message, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

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

    }

    @OnClick({R.id.tv_change_password, R.id.tv_login_out,R.id.tv_view_plan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change_password://更改密码
                launchActivity(new Intent(getActivity(),ChangePasswordActivity.class));
                break;
                case R.id.tv_view_plan://预案查看
                launchActivity(new Intent(getActivity(),PlanViewActivity.class));
                break;
            case R.id.tv_login_out://退出登录
//                DataHelper.clearShareprefrence(getContext());
                DataHelper.removeSF(getContext(), Constant.SP_KEY_USER_INFO);
                DataHelper.removeSF(getContext(), Constant.SP_KEY_USER_TOKEN);
                DataHelper.removeSF(getContext(), Constant.SP_KEY_USER_ID);
                launchActivity(new Intent(getActivity(), LoginActivity.class));
                killMyself();
                break;
        }
    }
}
