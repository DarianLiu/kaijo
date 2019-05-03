package com.geek.kaijo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.GridSpacingItemDecoration;
import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.geek.kaijo.mvp.ui.activity.SocialManageActivity;
import com.geek.kaijo.mvp.ui.adapter.SubMenuAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 社会治理 - 安全生产
 * Created by LiuLi on 2019/1/18.
 */

public class TabItemFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<GridItemContent> submenuList;
    private SubMenuAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        submenuList = (List<GridItemContent>)getArguments().getSerializable("menus");
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 4, false));

        if(submenuList!=null && submenuList.size()>0){
            adapter = new SubMenuAdapter(submenuList);
            adapter.setOnItemClickListener((view, viewType, data, position) -> {
                Intent intent = new Intent(getActivity(), SocialManageActivity.class);
                intent.putExtra("Submenu", submenuList.get(position));
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
