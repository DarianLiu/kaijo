package com.geek.kaijo.mvp.ui.fragment.society.fooddrug;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.ui.activity.ComponentDetailActivity;
import com.jess.arms.widget.CustomPopupWindow;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 药品
 */
public class DrugSafteDetailFragment extends Fragment{
    Unbinder unbinder;

    @BindView(R.id.et_companyName)
    public TextView et_companyName;  //企业名称
    @BindView(R.id.et_register_name)
    public TextView et_register_name; //注册地址
    @BindView(R.id.et_farenName)
    public TextView et_farenName; //企业法定代表人
    @BindView(R.id.et_yaoshi)
    public TextView et_yaoshi; //住店药师
    @BindView(R.id.et_xukezhanghao)
    public TextView et_xukezhanghao; //许可证号

    @BindView(R.id.et_xukezhengTime)
    public TextView et_xukezhengTime; //许可证颁发时间
    @BindView(R.id.et_youxiaoqiTime)
    public TextView et_youxiaoqiTime; //有效期至

    @BindView(R.id.et_jingyinfanshi)
    public TextView et_jingyinfanshi; //经营方式
   @BindView(R.id.et_jingyinfanwei)
    public TextView et_jingyinfanwei; //经营范围
    @BindView(R.id.et_Telephone)
    public TextView et_Telephone; //联系电话
    @BindView(R.id.et_phone)
    public TextView et_phone; //手机号码

    private ThingPositionInfo thingPositionInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_drug_safety_detail, container, false);
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
            et_companyName.setText(thingPositionInfo.getName());
            et_register_name.setText(thingPositionInfo.getAddress());
            et_farenName.setText(thingPositionInfo.getFarenName());
            et_yaoshi.setText(thingPositionInfo.getZhudianyaoshiName());
            et_xukezhanghao.setText(thingPositionInfo.getXukezhengNo());
            et_jingyinfanshi.setText(thingPositionInfo.getJingyingfangshiName());
            et_jingyinfanwei.setText(thingPositionInfo.getJingyingfanweiName());
            et_Telephone.setText(thingPositionInfo.getContact());
            et_phone.setText(thingPositionInfo.getMobile());

            et_xukezhengTime.setText(DateUtils.getDateToString(thingPositionInfo.getXukezhengTime(), DateUtils.dateString3));
            et_youxiaoqiTime.setText(DateUtils.getDateToString(thingPositionInfo.getYouxiaoTime(), DateUtils.dateString3));
        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
