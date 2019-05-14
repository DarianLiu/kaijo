package com.geek.kaijo.mvp.ui.fragment.society.safety;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 危化品
 */
public class WHPDetailFragment extends Fragment{
    Unbinder unbinder;
    /*特种设备*/
    @BindView(R.id.et_name)
    public TextView et_name;  //单位名称
    @BindView(R.id.et_address)
    public TextView et_address; //使用地点
    @BindView(R.id.et_other)
    public TextView et_other;
    @BindView(R.id.et_category)
    public TextView et_category; //分类
    private ThingPositionInfo thingPositionInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_weihuapin_detail, container, false);
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
            et_address.setText(thingPositionInfo.getAddress());
            et_other.setText(thingPositionInfo.getQita());
            et_category.setText(thingPositionInfo.getType());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
