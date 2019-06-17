package com.grid.im.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.grid.im.R;
import com.grid.im.bean.Contacts;

import java.util.List;

public class ConnectAdapter extends RecyclerView.Adapter {
    private List<Contacts> list;

    public ConnectAdapter(Context context, List<Contacts> list) {
        this.list = list;
    }

    public void notifyChanged(List<Contacts> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private OnItemOnClicklisten onItemOnClicklisten;

    public void setOnItemOnClilcklisten(OnItemOnClicklisten onItemOnClicklisten) {
        this.onItemOnClicklisten = onItemOnClicklisten;
    }

    public interface OnItemOnClicklisten {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connect, parent, false);
        return new BleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BleViewHolder mHolder = (BleViewHolder) holder;
        mHolder.tv_name.setText(list.get(position).name);
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemOnClicklisten != null) {
                    onItemOnClicklisten.onItemClick(v, position);
                }
            }
        });

        mHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemOnClicklisten != null) {
                    onItemOnClicklisten.onItemLongClick(view, position);
                }
                return false;
            }
        });

    }


    class BleViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView tv_name;

        public BleViewHolder(View view) {
            super(view);
            itemView = view;
//            itemView = view.findViewById(R.id.itemView);
            tv_name = view.findViewById(R.id.tv_name);

        }
    }
}
