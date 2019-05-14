package com.geek.kaijo.mvp.ui.fragment.society.fooddrug;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
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
import com.geek.kaijo.mvp.ui.activity.society.safety.SpecialCollectionActivity;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.jess.arms.widget.CustomPopupWindow;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 食品
 */
public class FoodSafteDetailFragment extends Fragment{
    Unbinder unbinder;
    /*特种设备*/
    @BindView(R.id.et_name)
    public TextView et_name;  //经营者名称
    @BindView(R.id.et_farenName)
    public TextView et_farenName; //法定代表负责人
    @BindView(R.id.et_jjxz)
    public TextView et_jjxz; //经济性质
    @BindView(R.id.et_jycs)
    public TextView et_jycs; //经营场所
    @BindView(R.id.et_xkzh)
    public TextView et_xkzh; //许可证号
    @BindView(R.id.et_ztyt)
    public TextView et_ztyt; //主体业态
    @BindView(R.id.spinner_street)
    public TextView spinner_street; //是否网络经营
    @BindView(R.id.tv_time)
    public TextView tv_time; //有效期
    @BindView(R.id.et_category)
    public TextView et_category; //主体类别
    @BindView(R.id.et_jgjg)
    public TextView et_jgjg; //监管机构
    @BindView(R.id.et_fxdj)
    public TextView et_fxdj; //风险等级
    @BindView(R.id.et_Telephone)
    public TextView et_Telephone; //联系电话
    @BindView(R.id.et_phone)
    public TextView et_phone; //手机号码


    private ThingPositionInfo thingPositionInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_food_safte_detail, container, false);
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
            et_name.setText(thingPositionInfo.getJingyingzheName());
            et_farenName.setText(thingPositionInfo.getFarenName());
            et_jjxz.setText(thingPositionInfo.getJingjixingzhiName());
            et_jycs.setText(thingPositionInfo.getAddress());
            et_xkzh.setText(thingPositionInfo.getXukezhengNo());
            et_ztyt.setText(thingPositionInfo.getZhutiyetai());
            if("是".equals(thingPositionInfo.getIsNetwork())){
                spinner_street.setText("是");
            }else {
                spinner_street.setText("否");
            }
            et_category.setText(thingPositionInfo.getType());
            et_jgjg.setText(thingPositionInfo.getJianguanjigouName());
            et_fxdj.setText(thingPositionInfo.getFengxiandengjiName());
            et_Telephone.setText(thingPositionInfo.getContact());
            et_phone.setText(thingPositionInfo.getMobile());

            tv_time.setText(DateUtils.getDateToString(thingPositionInfo.getYouxiaoTime(), DateUtils.dateString3));
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
