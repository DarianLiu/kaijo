package com.geek.kaijo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.ui.activity.CaseSearchActivity;
import com.geek.kaijo.mvp.ui.activity.CheckActivity;
import com.geek.kaijo.mvp.ui.activity.HandleActivity;
import com.geek.kaijo.mvp.ui.activity.ReportActivity;
import com.geek.kaijo.mvp.ui.activity.VerifyActivity;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 案件管理/案件上报
 */
public class CaseReportFragment extends Fragment {

    @BindView(R.id.tv_report)
    TextView tvReport;
    @BindView(R.id.tv_handle)
    TextView tvHandle;
    @BindView(R.id.tv_verify)
    TextView tvVerify;
    @BindView(R.id.tv_check)
    TextView tvCheck;
    @BindView(R.id.tv_inquiry)
    TextView tvInquiry;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @OnClick({R.id.tv_report, R.id.tv_handle, R.id.tv_verify, R.id.tv_check, R.id.tv_inquiry})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_report://上报页面
                intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("entry_type", 1);
                launchActivity(intent);
                break;
            case R.id.tv_handle://处理页面
                intent = new Intent(getActivity(), HandleActivity.class);
                intent.putExtra("curNode", 12);
                launchActivity(intent);
                break;
            case R.id.tv_verify://案件核实
//                intent = new Intent(getActivity(), VerifyActivity.class);
//                launchActivity(intent);
                intent = new Intent(getActivity(), HandleActivity.class);
                intent.putExtra("curNode", 13);
                launchActivity(intent);
                break;
            case R.id.tv_check://案件核查
//                intent = new Intent(getActivity(), CheckActivity.class);
//                launchActivity(intent);
                intent = new Intent(getActivity(), HandleActivity.class);
                intent.putExtra("curNode", 14);
                launchActivity(intent);
                break;
            case R.id.tv_inquiry://案件查询
                intent = new Intent(getActivity(), CaseSearchActivity.class);
                intent.putExtra("handleType", 1);
                launchActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
