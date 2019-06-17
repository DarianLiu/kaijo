package com.grid.im.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grid.im.R;
import com.grid.im.bean.Contacts;
import com.grid.im.di.component.DaggerConnectComponent;
import com.grid.im.mvp.contract.ConnectContract;
import com.grid.im.mvp.presenter.ConnectPresenter;
import com.grid.im.mvp.ui.activity.ChatActivity;
import com.grid.im.mvp.ui.adapter.ConnectAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * 沟通
 */
public class ConnectFragment extends BaseFragment<ConnectPresenter> implements ConnectContract.View {

    private RecyclerView recyclerView;
    private ConnectAdapter adapter;
    private List<Contacts> list;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerConnectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        for(int i=0;i<20;i++){
            Contacts contacts = new Contacts();
            contacts.name= "name"+i;
            list.add(contacts);
        }
        adapter = new ConnectAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemOnClilcklisten(new ConnectAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {


    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }


}
