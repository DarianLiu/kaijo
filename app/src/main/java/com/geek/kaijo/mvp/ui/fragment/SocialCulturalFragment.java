package com.geek.kaijo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.ui.activity.SocialManageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 社会治理 - 文化娱乐
 * Created by LiuLi on 2019/1/18.
 */

public class SocialCulturalFragment extends Fragment {

    @BindView(R.id.tv_social_cultura_internet_bar)
    TextView tvInternetBar;
    @BindView(R.id.tv_social_cultura_relic_protect)
    TextView tvRelicProtect;
    @BindView(R.id.tv_social_cultura_performing_place)
    TextView tvPerformingPlace;
    @BindView(R.id.tv_social_cultura_recreation)
    TextView tvRecreation;
    @BindView(R.id.tv_social_cultura_place)
    TextView tvPlace;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_cultura, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_social_cultura_internet_bar, R.id.tv_social_cultura_relic_protect,
            R.id.tv_social_cultura_performing_place, R.id.tv_social_cultura_recreation,
            R.id.tv_social_cultura_place})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), SocialManageActivity.class);
        intent.putExtra("assortId", 43);
        switch (view.getId()) {
            case R.id.tv_social_cultura_internet_bar://网吧
                intent.putExtra("thingType", 12);
                break;
            case R.id.tv_social_cultura_relic_protect://文物保护
                intent.putExtra("thingType", 13);
                break;
            case R.id.tv_social_cultura_performing_place://演出场所
                intent.putExtra("thingType", 14);
                break;
            case R.id.tv_social_cultura_recreation://游艺娱乐
                intent.putExtra("thingType", 15);
                break;
            case R.id.tv_social_cultura_place://娱乐场所
                intent.putExtra("thingType", 16);
                break;
        }
        startActivity(intent);
    }
}
