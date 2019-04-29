package com.geek.kaijo.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.di.component.DaggerInspectionAddComponent;
import com.geek.kaijo.di.module.InspectionAddModule;
import com.geek.kaijo.mvp.contract.InspectionAddContract;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.presenter.InspectionAddPresenter;
import com.geek.kaijo.mvp.ui.adapter.CaseAdapter;
import com.geek.kaijo.mvp.ui.adapter.InspectionPopAdapter;
import com.geek.kaijo.view.FlowRadioGroup;
import com.geek.kaijo.view.PopupUtils;
import com.geek.kaijo.view.RadioButtonVertical;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class InspectionAddActivity extends BaseActivity<InspectionAddPresenter> implements InspectionAddContract.View {

    @BindView(R.id.tv_thing_name)
    TextView tv_thing_name;
    View popView;
    private PopupUtils popupUtils;
    private List<Inspection> inspectionList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInspectionAddComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inspectionAddModule(new InspectionAddModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inspection_add; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        popupUtils = new PopupUtils();
        inspectionList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Inspection inspection = new Inspection();
            if(i%2==0){
                inspection.setName("噶噶噶噶噶刚按个啊个"+i);
            }else {
                inspection.setName("哈"+i);
            }

            inspectionList.add(inspection);
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @OnClick({R.id.tv_thing_name})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_thing_name:
                if (popView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popView = layoutInflater.inflate(R.layout.case_inspection_managet_itempop, null);
                }
                popupUtils.showPopWindowFromViewToUp(this, view, popView, inspectionList, new OnPopupWindowListener(),true);

                break;
        }
    }
    FlowRadioGroup flowRadioGroup;
    class OnPopupWindowListener implements PopupUtils.PopupWindowListener{


        @Override
        public <T> void onInitView(View view, final T t) {
            TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
            tv_toolbar_title.setText("巡查项选择");
//            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//            recyclerView.setLayoutManager(new LinearLayoutManager(InspectionAddActivity.this));
//            recyclerView.setHasFixedSize(true);

//            mAdapter = new InspectionPopAdapter(mCaseList);
//            mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
//                if(isCaseSearch){  //案件查询
//
//                }else {
//
//                }
//
//            });
//            recyclerView.setAdapter(mAdapter);
            if(flowRadioGroup==null){
                flowRadioGroup = view.findViewById(R.id.flowRadioGroup);
            }else {
                flowRadioGroup.removeAllViews();
            }
            for(int i=0;i<inspectionList.size();i++){
//                RadioButtonVertical radioButton = new RadioButtonVertical(InspectionAddActivity.this);
                RadioButton radioButton = new RadioButton(InspectionAddActivity.this);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 10, 20, 10);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setText(inspectionList.get(i).getName());
                radioButton.setTextColor(getResources().getColor(R.color.tab_text_color));
                radioButton.setButtonDrawable(null);
                radioButton.setEms(1);
                radioButton.setGravity(Gravity.CENTER);
//                radioButton.setTextSize(getResources().getDimension(R.dimen.t10));
                radioButton.setTextSize(12);
//                radioButton.setPadding(10,10,10,10);
//@android:drawable/btn_radio
//                radioButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.book),null,null);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(android.R.drawable.btn_radio),null,null);
//                radioButton.setCompoundDrawablePadding(10);
//                radioButton.setLayoutDirection(RadioButton.LAYOUT_DIRECTION_INHERIT);
//                radioButton.setTextDirection(RadioButton.TEXT_DIRECTION_INHERIT);
//                android:layoutDirection="rtl"
//                android:textDirection="ltr"
//                radioButton.setTextDirection(RadioButton.TEXT_DIRECTION_LOCALE);

                flowRadioGroup.addView(radioButton);
            }

        }
    }


    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
