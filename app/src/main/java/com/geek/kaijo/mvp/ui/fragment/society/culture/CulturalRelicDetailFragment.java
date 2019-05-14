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
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.ui.activity.ComponentDetailActivity;
import com.geek.kaijo.mvp.ui.activity.society.safety.SpecialCollectionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文物保护单位
 */
public class CulturalRelicDetailFragment extends Fragment {
    Unbinder unbinder;

    @BindView(R.id.et_name)
    public TextView et_name;  //名称

    @BindView(R.id.et_danweiName)
    public TextView et_danweiName; //产权单位名称

    @BindView(R.id.et_farenName)
    public TextView et_farenName; //产权单位法定代表人

    @BindView(R.id.et_chanquanDanweiName)
    public TextView et_chanquanDanweiName; //产权单位联系人

    @BindView(R.id.et_phone)
    public TextView et_phone; //产权单位联系人电话

    @BindView(R.id.et_guanlishiyongDanweiName)
    public TextView et_guanlishiyongDanweiName; //管理使用单位名称

    @BindView(R.id.et_guanlishiyongFarenName)
    public TextView et_guanlishiyongFarenName; //管理使用单位法定代表人

    @BindView(R.id.et_guanlishiyongLianxiName)
    public TextView et_guanlishiyongLianxiName; //管理使用单位联系人

    @BindView(R.id.et_guanlishiyongContact)
    public TextView et_guanlishiyongContact; //管理使用单位联系人电话

    private ThingPositionInfo thingPositionInfo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_cuturalrelic_detail, container, false);
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
            et_name.setText(thingPositionInfo.getName());
            et_danweiName.setText(thingPositionInfo.getDanweiName());
            et_farenName.setText(thingPositionInfo.getFarenName());
            et_chanquanDanweiName.setText(thingPositionInfo.getChanquanDanweiName());
            et_phone.setText(thingPositionInfo.getContact());
            et_guanlishiyongDanweiName.setText(thingPositionInfo.getGuanlishiyongDanweiName());
            et_guanlishiyongFarenName.setText(thingPositionInfo.getGuanlishiyongFarenName());
            et_guanlishiyongLianxiName.setText(thingPositionInfo.getGuanlishiyongLianxiName());
            et_guanlishiyongContact.setText(thingPositionInfo.getGuanlishiyongContact());
        }

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
