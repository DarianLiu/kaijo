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

public class ZaiJianGongDi extends Fragment {
    Unbinder unbinder;

    //在建工地
    @BindView(R.id.gd_et_mark)
    public EditText gd_et_mark; //受理书标号
    @BindView(R.id.gd_et_name)
    public EditText gd_et_name; //工程名称
    @BindView(R.id.gd_et_address)
    public EditText gd_et_address; //工程地址
    @BindView(R.id.gd_et_shigongdanwei)
    public EditText gd_et_shigongdanwei; //施工单位
    @BindView(R.id.gd_spinner_check)
    public AppCompatSpinner gd_spinner_check; //是否有施工许可证
    @BindView(R.id.gd_et_price)
    public EditText gd_et_price; //工程总造价
    @BindView(R.id.gd_et_jingduzhuangtai)
    public EditText gd_et_jingduzhuangtai; //形象进度/状态
    @BindView(R.id.gd_jianzhumianji)
    public EditText gd_jianzhumianji; //建筑面积（万平）
    @BindView(R.id.gd_et_kaigongnriqi)
    public TextView gd_et_kaigongnriqi; //开工日期
    @BindView(R.id.gd_et_jungongriqi)
    public TextView gd_et_jungongriqi; //竣工日期
    @BindView(R.id.gd_et_jianshedanwei)
    public EditText gd_et_jianshedanwei; //建设单位
    @BindView(R.id.gd_et_jianlidanwei)
    public EditText gd_et_jianlidanwei; //监理单位


    private CustomPopupWindow mTimePickerPopupWindow;//时间选择弹出框
    private CustomPopupWindow mTimePickerPopupWindow_jungong;//时间选择弹出框
    private TimePickerListener mTimePickerListener;
    private TimePickerListener mTimePickerListener_jungong;
    public List<String> checkList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_zaijiangongdi, container, false);
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
        gd_spinner_check.setAdapter(mStreetAdapter);

        //更新开工时间
        mTimePickerListener = time -> gd_et_kaigongnriqi.setText(time);
        initTimePopupWindow();
        //更新开工时间
        mTimePickerListener_jungong = time -> gd_et_jungongriqi.setText(time);
        initTimePopupWindow_gungong();
    }

    private interface TimePickerListener {
        void updateTime(String time);
    }

    @OnClick({R.id.gd_et_kaigongnriqi,R.id.gd_et_jungongriqi})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.gd_et_kaigongnriqi:
                mTimePickerPopupWindow.show();
                break;
            case R.id.gd_et_jungongriqi:
                mTimePickerPopupWindow_jungong.show();
                break;
        }
    }

    /**
     * 初始化时间选择弹出框
     * 开工日期
     */
    private void initTimePopupWindow() {
//        gd_et_kaigongnriqi.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
        gd_et_kaigongnriqi.setText(DateUtils.getDateToString(DateUtils.getCurrentTimeMillis(), DateUtils.dateString3));

        View timePickerView = View.inflate(getActivity(), R.layout.view_pop_time_picker, null);

        mTimePickerPopupWindow = CustomPopupWindow.builder()
                .parentView(gd_et_kaigongnriqi).contentView(timePickerView)
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

//                        tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}", mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
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
     * 初始化时间选择弹出框
     * 开工日期
     */
    private void initTimePopupWindow_gungong() {
//        gd_et_jungongriqi.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
        gd_et_jungongriqi.setText(DateUtils.getDateToString(DateUtils.getCurrentTimeMillis(), DateUtils.dateString3));

        View timePickerView = View.inflate(getActivity(), R.layout.view_pop_time_picker, null);

        mTimePickerPopupWindow_jungong = CustomPopupWindow.builder()
                .parentView(gd_et_kaigongnriqi).contentView(timePickerView)
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

//                        tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}", mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
                        tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2}", mYear[0], mMonth[0], mDay[0]));
                    });

                    TextView tvSure = contentView.findViewById(R.id.tv_sure);
                    TextView tvToday = contentView.findViewById(R.id.tv_today);
                    TextView tvCancel = contentView.findViewById(R.id.tv_cancel);

                    tvSure.setOnClickListener(v -> {
                        if (mTimePickerListener_jungong != null) {
                            mTimePickerListener_jungong.updateTime(tvCurrentDate.getText().toString());
                        }
                        mTimePickerPopupWindow_jungong.dismiss();
                    });

                    tvToday.setOnClickListener(v ->
                            datePicker.updateDate(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(), DateUtils.getCurrentDate()));

                    tvCancel.setOnClickListener(v -> mTimePickerPopupWindow_jungong.dismiss());

                })
                .isWrap(true).build();
    }

    public boolean checkParams(){
        if (TextUtils.isEmpty(gd_et_name.getText().toString())) {
            Toast.makeText(getActivity(),"请输入工程名称",Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(gd_et_address.getText().toString())) {
            Toast.makeText(getActivity(),"请输入工程地址",Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(gd_et_shigongdanwei.getText().toString())) {
            Toast.makeText(getActivity(),"请输入施工单位",Toast.LENGTH_LONG).show();
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
        if (mTimePickerPopupWindow_jungong != null && mTimePickerPopupWindow_jungong.isShowing()) {
            mTimePickerPopupWindow_jungong.dismiss();
            mTimePickerPopupWindow_jungong = null;
        }
    }
}
