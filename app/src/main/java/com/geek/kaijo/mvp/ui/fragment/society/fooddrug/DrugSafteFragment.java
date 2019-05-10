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
 * 药品
 */
public class DrugSafteFragment extends Fragment{
    Unbinder unbinder;

    @BindView(R.id.et_companyName)
    public EditText et_companyName;  //企业名称
    @BindView(R.id.et_register_name)
    public EditText et_register_name; //注册地址
    @BindView(R.id.et_farenName)
    public EditText et_farenName; //企业法定代表人
    @BindView(R.id.et_yaoshi)
    public EditText et_yaoshi; //住店药师
    @BindView(R.id.et_xukezhanghao)
    public EditText et_xukezhanghao; //许可证号

    @BindView(R.id.et_xukezhengTime)
    public TextView et_xukezhengTime; //许可证颁发时间
    @BindView(R.id.et_youxiaoqiTime)
    public TextView et_youxiaoqiTime; //有效期至

    @BindView(R.id.et_jingyinfanshi)
    public EditText et_jingyinfanshi; //经营方式
   @BindView(R.id.et_jingyinfanwei)
    public EditText et_jingyinfanwei; //经营范围
    @BindView(R.id.et_Telephone)
    public EditText et_Telephone; //联系电话
    @BindView(R.id.et_phone)
    public EditText et_phone; //手机号码



    private TimePickerListener mTimePickerListener;
    private TimePickerListener_xukezheng mTimePickerListener_xukezheng;
    private CustomPopupWindow mTimePickerPopupWindow_xukezheng;//时间选择弹出框,许可证颁发时间
    private CustomPopupWindow mTimePickerPopupWindow;//时间选择弹出框,有效期至

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thing_drug_safety, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //许可证颁发时间
        mTimePickerListener_xukezheng = time -> et_xukezhengTime.setText(time);
        initTimePopupWindow_xukezheng();

        //y有效期
        mTimePickerListener = time -> et_youxiaoqiTime.setText(time);
        initTimePopupWindow();
    }

    private interface TimePickerListener {
        void updateTime(String time);
    }
    private interface TimePickerListener_xukezheng {
        void updateTime(String time);
    }

    @OnClick({R.id.et_xukezhengTime,R.id.et_youxiaoqiTime})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.et_xukezhengTime:
                mTimePickerPopupWindow_xukezheng.show();
                break;
                case R.id.et_youxiaoqiTime:
                mTimePickerPopupWindow.show();
                break;
        }
    }

    /**
     * 初始化时间选择弹出框
     */
    private void initTimePopupWindow() {
//        et_youxiaoqiTime.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
        et_youxiaoqiTime.setText(DateUtils.getDateToString(DateUtils.getCurrentTimeMillis(), DateUtils.dateString3));

        View timePickerView = View.inflate(getActivity(), R.layout.view_pop_time_picker, null);

        mTimePickerPopupWindow = CustomPopupWindow.builder()
                .parentView(et_youxiaoqiTime).contentView(timePickerView)
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


    /**
     * 许可证颁发时间
     */
    private void initTimePopupWindow_xukezheng() {
//        et_xukezhengTime.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
        et_xukezhengTime.setText(DateUtils.getDateToString(DateUtils.getCurrentTimeMillis(), DateUtils.dateString3));

        View timePickerView = View.inflate(getActivity(), R.layout.view_pop_time_picker, null);

        mTimePickerPopupWindow_xukezheng = CustomPopupWindow.builder()
                .parentView(et_xukezhengTime).contentView(timePickerView)
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
                        if (mTimePickerListener_xukezheng != null) {
                            mTimePickerListener_xukezheng.updateTime(tvCurrentDate.getText().toString());
                        }
                        mTimePickerPopupWindow_xukezheng.dismiss();
                    });

                    tvToday.setOnClickListener(v ->
                            datePicker.updateDate(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(), DateUtils.getCurrentDate()));

                    tvCancel.setOnClickListener(v -> mTimePickerPopupWindow_xukezheng.dismiss());

                })
                .isWrap(true).build();
    }


    public boolean checkParams(){
        if (TextUtils.isEmpty(et_companyName.getText().toString())) {
            Toast.makeText(getActivity(),"请输入企业名称",Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(et_register_name.getText().toString())) {
            Toast.makeText(getActivity(),"请输入注册地址",Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(et_farenName.getText().toString())) {
            Toast.makeText(getActivity(),"请输入企业法定代表人",Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(et_xukezhanghao.getText().toString())) {
            Toast.makeText(getActivity(),"请输入许可证号",Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(et_xukezhengTime.getText().toString())) {
            Toast.makeText(getActivity(),"请输入et_xukezhengTime",Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(et_youxiaoqiTime.getText().toString())) {
            Toast.makeText(getActivity(),"请输入有效期时间",Toast.LENGTH_LONG).show();
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
        if (mTimePickerPopupWindow_xukezheng != null && mTimePickerPopupWindow_xukezheng.isShowing()) {
            mTimePickerPopupWindow_xukezheng.dismiss();
            mTimePickerPopupWindow_xukezheng = null;
        }
    }
}
