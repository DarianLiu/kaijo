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
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.ui.activity.ComponentDetailActivity;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 防台防汛
 */
public class TyphoonFloodDetailFragment extends Fragment{
    Unbinder unbinder;
    @BindView(R.id.spinner_category)
    public TextView spinner_category;  //类别
    @BindView(R.id.et_state)
    public TextView et_state; //状态
    @BindView(R.id.et_address)
    public TextView et_address; //位置
    @BindView(R.id.et_danweiName)
    public TextView et_danweiName; //产权单位（管理单位）
    @BindView(R.id.et_farenName)
    public TextView et_farenName; //产权单位（管理单位）责任人
    @BindView(R.id.et_leading_TlPhone)
    public TextView et_leading_TlPhone; //产权单位责任人联系电话
    @BindView(R.id.et_leading_phone)
    public TextView et_leading_phone; //产权单位责任人联系手机
    @BindView(R.id.et_jiedaozerenName)
    public TextView et_jiedaozerenName; //街道责任人
    @BindView(R.id.jiedaozerenMobile)
    public TextView jiedaozerenMobile; //街道责任人联系电话

    public List<String> checkList;

    private ThingPositionInfo thingPositionInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_typhoon_flood_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ComponentDetailActivity activity = (ComponentDetailActivity)getActivity();
        if(activity!=null){
            this.thingPositionInfo = activity.thingPositionInfo;
        }
        if(thingPositionInfo!=null){
            spinner_category.setText(thingPositionInfo.getType());
            et_state.setText(thingPositionInfo.getStatus());
            et_address.setText(thingPositionInfo.getAddress());
            et_danweiName.setText(thingPositionInfo.getDanweiName());
            et_farenName.setText(thingPositionInfo.getFarenName());
            et_leading_TlPhone.setText(thingPositionInfo.getContact());
            et_leading_phone.setText(thingPositionInfo.getMobile());
            et_jiedaozerenName.setText(thingPositionInfo.getJiedaozerenName());
            jiedaozerenMobile.setText(thingPositionInfo.getJiedaozerenMobile());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
