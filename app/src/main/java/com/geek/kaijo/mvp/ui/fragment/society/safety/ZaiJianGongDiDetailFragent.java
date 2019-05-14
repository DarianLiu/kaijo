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

public class ZaiJianGongDiDetailFragent extends Fragment {
    Unbinder unbinder;

    //在建工地
    @BindView(R.id.gd_et_mark)
    public TextView gd_et_mark; //受理书标号
    @BindView(R.id.gd_et_name)
    public TextView gd_et_name; //工程名称
    @BindView(R.id.gd_et_address)
    public TextView gd_et_address; //工程地址
    @BindView(R.id.gd_et_shigongdanwei)
    public TextView gd_et_shigongdanwei; //施工单位
    @BindView(R.id.gd_spinner_check)
    public TextView gd_spinner_check; //是否有施工许可证
    @BindView(R.id.gd_et_price)
    public TextView gd_et_price; //工程总造价
    @BindView(R.id.gd_et_jingduzhuangtai)
    public TextView gd_et_jingduzhuangtai; //形象进度/状态
    @BindView(R.id.gd_jianzhumianji)
    public TextView gd_jianzhumianji; //建筑面积（万平）
    @BindView(R.id.gd_et_kaigongnriqi)
    public TextView gd_et_kaigongnriqi; //开工日期
    @BindView(R.id.gd_et_jungongriqi)
    public TextView gd_et_jungongriqi; //竣工日期
    @BindView(R.id.gd_et_jianshedanwei)
    public TextView gd_et_jianshedanwei; //建设单位
    @BindView(R.id.gd_et_jianlidanwei)
    public TextView gd_et_jianlidanwei; //监理单位


    private ThingPositionInfo thingPositionInfo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_zaijiangongdi_detail, container, false);
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
            gd_et_mark.setText(thingPositionInfo.getShoulishuNo());
            gd_et_name.setText(thingPositionInfo.getName());
            gd_et_address.setText(thingPositionInfo.getAddress());
            gd_et_shigongdanwei.setText(thingPositionInfo.getDanweiName());
            if("是".equals(thingPositionInfo.getIsXukezheng())){
                gd_spinner_check.setText("是");
            }else {
                gd_spinner_check.setText("否");
            }
            gd_et_price.setText(thingPositionInfo.getZongzaojiaSum());
            gd_et_jingduzhuangtai.setText(thingPositionInfo.getStatus());
            gd_jianzhumianji.setText(thingPositionInfo.getJianzhuSum());

            gd_et_jianshedanwei.setText(thingPositionInfo.getJianshedanweiName());
            gd_et_jianlidanwei.setText(thingPositionInfo.getJianlidanweiName());



//            gd_et_kaigongnriqi.setText(thingPositionInfo.getJianzhuSum());
//            gd_et_jungongriqi.setText(thingPositionInfo.getJianzhuSum());

            gd_et_kaigongnriqi.setText(DateUtils.getDateToString(thingPositionInfo.getStartTime(), DateUtils.dateString3));
            gd_et_jungongriqi.setText(DateUtils.getDateToString(thingPositionInfo.getEndTime(), DateUtils.dateString3));
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
