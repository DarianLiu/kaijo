package com.geek.kaijo.mvp.ui.fragment.society.safety;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 防台防汛
 */
public class TyphoonFloodFragment extends Fragment{
    Unbinder unbinder;

    @BindView(R.id.spinner_category)
    public AppCompatSpinner spinner_category;  //类别
    @BindView(R.id.et_state)
    public EditText et_state; //状态
    @BindView(R.id.et_address)
    public EditText et_address; //位置
    @BindView(R.id.et_danweiName)
    public EditText et_danweiName; //产权单位（管理单位）
    @BindView(R.id.et_farenName)
    public EditText et_farenName; //产权单位（管理单位）责任人
    @BindView(R.id.et_leading_TlPhone)
    public EditText et_leading_TlPhone; //产权单位责任人联系电话
    @BindView(R.id.et_leading_phone)
    public EditText et_leading_phone; //产权单位责任人联系手机
    @BindView(R.id.et_jiedaozerenName)
    public EditText et_jiedaozerenName; //街道责任人
    @BindView(R.id.jiedaozerenMobile)
    public EditText jiedaozerenMobile; //街道责任人联系电话

    public List<String> checkList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_typhoon_flood, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkList = new ArrayList<>();
        checkList.add("边坡");
        checkList.add("挡土墙");
        checkList.add("低洼地");
        checkList.add("拱门");
        checkList.add("危房");
        checkList.add("围墙");
        MySpinnerAdapter<String> mStreetAdapter = new MySpinnerAdapter<>(getActivity(), checkList);
        spinner_category.setAdapter(mStreetAdapter);
    }


    public boolean checkParams(){
        if (TextUtils.isEmpty(et_address.getText().toString())) {
            Toast.makeText(getActivity(),"请输入位置",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
