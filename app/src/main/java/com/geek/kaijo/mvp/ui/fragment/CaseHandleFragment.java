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
import com.geek.kaijo.mvp.ui.activity.ReportActivity;
import com.geek.kaijo.mvp.ui.activity.TemporaryActivity;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 案件管理/直接处理
 */
public class CaseHandleFragment extends Fragment {

    @BindView(R.id.tv_report)
    TextView tvReport;
    @BindView(R.id.tv_temporary_save)
    TextView tvTemporarySave;
    @BindView(R.id.tv_case_query)
    TextView tvCaseQuery;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_handle, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @OnClick({R.id.tv_report, R.id.tv_temporary_save, R.id.tv_case_query})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_report://上报页面
                intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("entry_type", 0);
                launchActivity(intent);
                break;
//            case R.id.tv_rehandle://处理页面
//                intent = new Intent(getActivity(), HandleActivity.class);
//                intent.putExtra("entry_type", 0);
//                launchActivity(intent);
//                break;
            case R.id.tv_temporary_save://暂存
                intent = new Intent(getActivity(), TemporaryActivity.class);
                launchActivity(intent);
                break;
            case R.id.tv_case_query://案件查询
                intent = new Intent(getActivity(), CaseSearchActivity.class);
                intent.putExtra("entry_type", 0);
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
