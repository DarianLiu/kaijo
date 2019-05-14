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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.ui.activity.society.safety.SpecialCollectionActivity;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.geek.kaijo.mvp.ui.fragment.society.safety.ZaiJianGongDi;
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
public class FoodSafteFragment extends Fragment{
    Unbinder unbinder;
    /*特种设备*/
    @BindView(R.id.et_name)
    public EditText et_name;  //经营者名称
    @BindView(R.id.et_farenName)
    public EditText et_farenName; //法定代表负责人
    @BindView(R.id.et_jjxz)
    public EditText et_jjxz; //经济性质
    @BindView(R.id.et_jycs)
    public EditText et_jycs; //经营场所
    @BindView(R.id.et_xkzh)
    public EditText et_xkzh; //许可证号
    @BindView(R.id.et_ztyt)
    public EditText et_ztyt; //主体业态
    @BindView(R.id.spinner_street)
    public AppCompatSpinner spinner_street; //是否网络经营
    @BindView(R.id.tv_time)
    public TextView tv_time; //有效期
    @BindView(R.id.et_category)
    public EditText et_category; //主体类别
    @BindView(R.id.et_jgjg)
    public EditText et_jgjg; //监管机构
    @BindView(R.id.et_fxdj)
    public EditText et_fxdj; //风险等级
    @BindView(R.id.et_Telephone)
    public EditText et_Telephone; //联系电话
    @BindView(R.id.et_phone)
    public EditText et_phone; //手机号码


    public List<String> checkList;
    private TimePickerListener mTimePickerListener;
    private CustomPopupWindow mTimePickerPopupWindow;//时间选择弹出框

    private ThingPositionInfo thingPositionInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_food_safte, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkList = new ArrayList<>();
        checkList.add("否");
        checkList.add("是");
        MySpinnerAdapter<String> mStreetAdapter = new MySpinnerAdapter<>(getActivity(), checkList);
        spinner_street.setAdapter(mStreetAdapter);

        SpecialCollectionActivity activity = (SpecialCollectionActivity)getActivity();
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
                spinner_street.setSelection(1);
            }
            et_category.setText(thingPositionInfo.getType());
            et_jgjg.setText(thingPositionInfo.getJianguanjigouName());
            et_fxdj.setText(thingPositionInfo.getFengxiandengjiName());
            et_Telephone.setText(thingPositionInfo.getContact());
            et_phone.setText(thingPositionInfo.getMobile());

            tv_time.setText(DateUtils.getDateToString(thingPositionInfo.getYouxiaoTime(), DateUtils.dateString3));
        }else {
            tv_time.setText(DateUtils.getDateToString(DateUtils.getCurrentTimeMillis(), DateUtils.dateString3));
        }

        //更新开工时间
        mTimePickerListener = time -> tv_time.setText(time);
        initTimePopupWindow();
    }

    private interface TimePickerListener {
        void updateTime(String time);
    }

    @OnClick({R.id.tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_time:
                mTimePickerPopupWindow.show();
                break;
        }
    }

    /**
     * 初始化时间选择弹出框
     */
    private void initTimePopupWindow() {
//        tv_time.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
//        tv_time.setText(DateUtils.getDateToString(DateUtils.getCurrentTimeMillis(), DateUtils.dateString3));

        View timePickerView = View.inflate(getActivity(), R.layout.view_pop_time_picker, null);

        mTimePickerPopupWindow = CustomPopupWindow.builder()
                .parentView(tv_time).contentView(timePickerView)
                .isOutsideTouch(false)
                .isWrap(false)
                .customListener(contentView -> {
                    String[] mYear = new String[1];
                    String[] mMonth = new String[1];
                    String[] mDay = new String[1];
                    String[] mHour = new String[1];
                    String[] mMinute = new String[1];
                    mYear[0] = String.valueOf(DateUtils.getCurrentYear());
                    mMonth[0] = String.valueOf(DateUtils.getCurrentMonth());
                    mDay[0] = String.valueOf(DateUtils.getCurrentDate());
                    mHour[0] = String.valueOf(DateUtils.getCurrentDateHour());
                    mMinute[0] = String.valueOf(DateUtils.getCurrentDateMinute());

                    TextView tvCurrentDate = contentView.findViewById(R.id.tv_current_time);
//                    tvCurrentDate.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
                    tvCurrentDate.setText(DateUtils.getDateToString(DateUtils.getCurrentTimeMillis(), DateUtils.dateString3));
                    DatePicker datePicker = contentView.findViewById(R.id.datePicker);
                    datePicker.setMinDate(DateUtils.get1970YearTimeStamp());
                    datePicker.updateDate(Integer.parseInt(mYear[0]), Integer.parseInt(mMonth[0]), Integer.parseInt(mDay[0]));
                    datePicker.init(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(),
                            DateUtils.getCurrentDate(), (view, year, monthOfYear, dayOfMonth) -> {
                                mYear[0] = String.valueOf(year);
                                mMonth[0] = monthOfYear < 9 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                                mDay[0] = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

//                                tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}", mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
                                tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2}", mYear[0], mMonth[0], mDay[0]));
                            }
                    );

                    TimePicker timePicker = contentView.findViewById(R.id.timePicker);
                    timePicker.setIs24HourView(true);
                    timePicker.setCurrentHour(Integer.parseInt(mHour[0]));
                    timePicker.setCurrentMinute(Integer.parseInt(mMinute[0]));
                    timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                        mHour[0] = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                        mMinute[0] = minute < 10 ? "0" + minute : String.valueOf(minute);
                        tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2}", mYear[0], mMonth[0], mDay[0]));
                    });

                    TextView tvSure = contentView.findViewById(R.id.tv_sure);
                    TextView tvToday = contentView.findViewById(R.id.tv_today);
                    TextView tvCancel = contentView.findViewById(R.id.tv_cancel);

                    tvSure.setOnClickListener(v -> {
                        if (mTimePickerListener != null) {
                            mTimePickerListener.updateTime(tvCurrentDate.getText().toString());
                        }
                        mTimePickerPopupWindow.dismiss();
                    });

                    tvToday.setOnClickListener(v ->
                            datePicker.updateDate(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(), DateUtils.getCurrentDate()));

                    tvCancel.setOnClickListener(v -> mTimePickerPopupWindow.dismiss());

                })
                .isWrap(true).build();
    }


    public boolean checkParams(){
        if (TextUtils.isEmpty(et_name.getText().toString())) {
            Toast.makeText(getActivity(),"请输入经营者名称",Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(et_farenName.getText().toString())) {
            Toast.makeText(getActivity(),"请输入法定代表负责人",Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(et_jycs.getText().toString())) {
            Toast.makeText(getActivity(),"请输入经营场所",Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(et_xkzh.getText().toString())) {
            Toast.makeText(getActivity(),"请输入许可证号",Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(tv_time.getText().toString())) {
            Toast.makeText(getActivity(),"请输入有效期",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mTimePickerPopupWindow != null && mTimePickerPopupWindow.isShowing()) {
            mTimePickerPopupWindow.dismiss();
            mTimePickerPopupWindow = null;
        }
    }
}
