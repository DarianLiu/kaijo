package com.geek.kaijo.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Nodes;

import java.util.List;

public class NodesAdapter extends RecyclerView.Adapter {
    private List<Nodes> list;
    private Context context;

    public NodesAdapter(Context context, List<Nodes> list) {
        this.context = context;
        this.list = list;
    }

    public void notifyChanged(List<Nodes> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private OnItemOnClicklisten onItemOnClicklisten;

    public void setOnItemOnClilcklisten(OnItemOnClicklisten onItemOnClicklisten) {
        this.onItemOnClicklisten = onItemOnClicklisten;
    }

    public interface OnItemOnClicklisten {
        void onItemClick(View v, int position);

        void onEditeClick(View v, int position);
        void onDeleteClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nodes, parent,false);
        return new BleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BleViewHolder mHolder = (BleViewHolder) holder;
//        mHolder.tv_code.setText(list.get(position).code);
        mHolder.name.setText(list.get(position).getContent());


        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemOnClicklisten != null) {
                    onItemOnClicklisten.onItemClick(v, position);
                }
            }
        });
        mHolder.tv_edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemOnClicklisten != null) {
                    onItemOnClicklisten.onEditeClick(v, position);
                }
            }
        });
        mHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemOnClicklisten != null) {
                    onItemOnClicklisten.onDeleteClick(v, position);
                }
            }
        });

        if(position==0){
            mHolder.view_line.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,30,0,0);
            mHolder.itemView.setLayoutParams(layoutParams);
        }else {
            mHolder.view_line.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,0,0);
            mHolder.itemView.setLayoutParams(layoutParams);
        }

    }


    class BleViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView name;
        public View view_line;
        public TextView tv_edite;
        public TextView tv_delete;

        public BleViewHolder(View view) {
            super(view);
            itemView = view;
            name = view.findViewById(R.id.tv_name);
            view_line = view.findViewById(R.id.view_line);
            tv_edite = view.findViewById(R.id.tv_edite);
            tv_delete = view.findViewById(R.id.tv_delete);

        }
    }
}
