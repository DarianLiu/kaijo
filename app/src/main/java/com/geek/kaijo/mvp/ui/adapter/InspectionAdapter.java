package com.geek.kaijo.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Inspection;

import java.util.List;

public class InspectionAdapter extends RecyclerView.Adapter {
    private List<Inspection> list;
    private Context context;

    public InspectionAdapter(Context context, List<Inspection> list) {
        this.context = context;
        this.list = list;
    }

    public void notifyChanged(List<Inspection> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private OnItemOnClicklisten onItemOnClicklisten;

    public void setOnItemOnClilcklisten(OnItemOnClicklisten onItemOnClicklisten) {
        this.onItemOnClicklisten = onItemOnClicklisten;
    }

    public interface OnItemOnClicklisten {
        void onItemClick(View v, int position);
//        void onSubtractClick(View v, int position);
//        void onAddClick(View v, int position);
//        void onTextChanged(int position, CharSequence charSequence, int i, int i1, int i2);
//        void afterTextChanged(int position, Editable editable);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_inspection_item,parent,false);
        return new BleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BleViewHolder mHolder = (BleViewHolder) holder;
        if(list.get(position).radioState==0){
            mHolder.img_radio.setImageResource(R.mipmap.radio_defalt);
        }else {
            mHolder.img_radio.setImageResource(R.mipmap.radio_selected);
        }

        mHolder.tv_name.setText(list.get(position).getName());

//        mHolder.tv_name.setText(list.get(position).getProgram_name());
//        //判断是否有TextWatcher监听事件，有的话先移除
//        if (mHolder.et_count.getTag(R.id.et_count) instanceof TextWatcher){
//            mHolder.et_count.removeTextChangedListener(((TextWatcher) mHolder.et_count.getTag(R.id.et_count)));
//        }
//
//        if(list.get(position).getPivot()!=null && list.get(position).getPivot().group>0){
//            mHolder.et_count.setText(list.get(position).getPivot().group+"");
//        }else {
//            mHolder.et_count.setText("");
//        }
//        mHolder.et_count.setSelection(mHolder.et_count.getText().toString().trim().length());
//
//        Glide.with(context).load(list.get(position).getRight_image()).into(mHolder.img_action);
//
//
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemOnClicklisten != null) {
                    onItemOnClicklisten.onItemClick(v, position);
                }
            }
        });
//
//
//        mHolder.img_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onItemOnClicklisten != null) {
//                    onItemOnClicklisten.onAddClick(view, position);
//                }
//            }
//        });
//        mHolder.img_subtract.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onItemOnClicklisten != null) {
//                    onItemOnClicklisten.onSubtractClick(view, position);
//                }
//            }
//        });
//
//        mHolder.et_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//
//
//            }
//        });
//
//        TextWatcher textWatcher=new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (onItemOnClicklisten != null) {
//                    onItemOnClicklisten.afterTextChanged(position,s);
//                }
//            }
//        };
//        //设置tag为TextWatcher
//        mHolder.et_count.addTextChangedListener(textWatcher);
//        mHolder.et_count.setTag(R.id.et_count,textWatcher);
    }


    class BleViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ImageView img_radio;
        public TextView tv_name;


        public BleViewHolder(View view) {
            super(view);
            itemView = view;
            img_radio = view.findViewById(R.id.img_radio);
            tv_name = view.findViewById(R.id.tv_name);
//            img_add = view.findViewById(R.id.img_add);
//            img_subtract = view.findViewById(R.id.img_subtract);
//            et_count = view.findViewById(R.id.et_count);
        }
    }
}
