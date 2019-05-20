package com.geek.kaijo.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.api.Api;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.jess.arms.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter {
    private List<UploadFile> list;
    private Context context;

    public PhotoAdapter(Context context, List<UploadFile> list) {
        this.context = context;
        this.list = list;
    }

    public void notifyChanged(List<UploadFile> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private OnItemOnClicklisten onItemOnClicklisten;

    public void setOnItemOnClilcklisten(OnItemOnClicklisten onItemOnClicklisten) {
        this.onItemOnClicklisten = onItemOnClicklisten;
    }

    public interface OnItemOnClicklisten {
        void onItemClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent,false);
        return new BleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BleViewHolder mHolder = (BleViewHolder) holder;

        Glide.with(context).load(Api.URL_BANNER+"/"+list.get(position).getUrl()).into(mHolder.img_upload_phto);

//        if(list.get(position).selectStatus==0){
//            mHolder.photo_checkBox.setChecked(false);
//        }else {
//            mHolder.photo_checkBox.setChecked(true);
//        }

//        mHolder.item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(onItemOnClicklisten!=null){
//                    onItemOnClicklisten.onItemClick(view,position);
//                }
//            }
//        });

    }


    class BleViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout item;
        public ImageView img_upload_phto;
//        public CheckBox photo_checkBox;

        public BleViewHolder(View view) {
            super(view);

            item = view.findViewById(R.id.item);
            img_upload_phto = view.findViewById(R.id.img_upload_phto);
//            photo_checkBox = view.findViewById(R.id.photo_checkBox);
        }
    }
}
