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
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.ui.activity.ComponentDetailActivity;
import com.geek.kaijo.mvp.ui.activity.society.safety.SpecialCollectionActivity;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 特种设备
 */
public class SpecialCollectionDetailFragment extends Fragment{
    Unbinder unbinder;
    /*特种设备*/
    @BindView(R.id.et_name)
    public TextView et_name;  //单位名称
    @BindView(R.id.et_farenName)
    public TextView et_farenName; //et_farenName
    @BindView(R.id.et_address)
    public TextView et_address; //使用地点

    public List<String> checkList;
    private ThingPositionInfo thingPositionInfo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_tezhongshebei_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ComponentDetailActivity activity = (ComponentDetailActivity)getActivity();
        if(activity!=null){
            this.thingPositionInfo = activity.thingPositionInfo;
        }
        if(thingPositionInfo!=null){
            et_name.setText(thingPositionInfo.getDanweiName());
            et_farenName.setText(thingPositionInfo.getFarenName());
            et_address.setText(thingPositionInfo.getAddress());
        }
    }

//    @OnClick({R.id.tv_add_equipment})
//    public void onViewClicked(View view) {
//        switch (view.getId()){
//            case R.id.tv_add_equipment:
//                addComponentView();
//                break;
//        }
//    }

    private void addComponentView() {
        if (checkList == null) {
            checkList = new ArrayList<>();
            checkList.add("否");
            checkList.add("是");
        }

        View view = getLayoutInflater().inflate(R.layout.component_include_add, null);
        AppCompatSpinner spinner_check = view.findViewById(R.id.spinner_check);
        MySpinnerAdapter<String> mStreetAdapter = new MySpinnerAdapter<>(getActivity(), checkList);
        spinner_check.setAdapter(mStreetAdapter);


        TextView tv_delete = view.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ly_addView.removeView((View) view.getParent().getParent());
            }
        });
//        ly_addView.addView(view);
    }

    public boolean checkParams(){
        if (TextUtils.isEmpty(et_name.getText().toString())) {
            Toast.makeText(getActivity(),"请输入单位名称",Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(et_address.getText().toString())) {
            Toast.makeText(getActivity(),"请输入地址",Toast.LENGTH_LONG).show();
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
