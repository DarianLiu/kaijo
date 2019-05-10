package com.geek.kaijo.mvp.ui.fragment.society.culture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.geek.kaijo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文物保护单位
 */
public class CulturalRelicFragment extends Fragment {
    Unbinder unbinder;

    @BindView(R.id.et_name)
    public EditText et_name;  //名称

    @BindView(R.id.et_danweiName)
    public EditText et_danweiName; //产权单位名称

    @BindView(R.id.et_farenName)
    public EditText et_farenName; //产权单位法定代表人

    @BindView(R.id.et_chanquanDanweiName)
    public EditText et_chanquanDanweiName; //产权单位联系人

    @BindView(R.id.et_phone)
    public EditText et_phone; //产权单位联系人电话

    @BindView(R.id.et_guanlishiyongDanweiName)
    public EditText et_guanlishiyongDanweiName; //管理使用单位名称

    @BindView(R.id.et_guanlishiyongFarenName)
    public EditText et_guanlishiyongFarenName; //管理使用单位法定代表人

    @BindView(R.id.et_guanlishiyongLianxiName)
    public EditText et_guanlishiyongLianxiName; //管理使用单位联系人

    @BindView(R.id.et_guanlishiyongContact)
    public EditText et_guanlishiyongContact; //管理使用单位联系人电话


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_cuturalrelic, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private interface TimePickerListener {
        void updateTime(String time);
    }

    private interface TimePickerListener_xukezheng {
        void updateTime(String time);
    }


    public boolean checkParams() {
        if (TextUtils.isEmpty(et_name.getText().toString())) {
            Toast.makeText(getActivity(), "请输入名称", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(et_danweiName.getText().toString())) {
            Toast.makeText(getActivity(), "请输入产权单位名称", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(et_guanlishiyongDanweiName.getText().toString())) {
            Toast.makeText(getActivity(), "请输入管理使用单位名称", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
