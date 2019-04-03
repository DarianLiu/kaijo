package com.geek.kaijo.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.ui.activity.UploadActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class UploadPhotoAdapter extends RecyclerView.Adapter {
    private List<UploadFile> list;
    private Context context;

    public UploadPhotoAdapter(Context context, List<UploadFile> list) {
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
        void onItemDeleteClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upload_photo,parent, false);
        return new BleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BleViewHolder mHolder = (BleViewHolder) holder;

//        Glide.with(context).load(list.get(position).getUrl()).into(mHolder.img_upload_phto);
//        Picasso.get().load(new File(list.get(position).getFileName())).resize(170, 300).centerCrop().into(mHolder.img_upload_phto);
        Picasso.get().load(new File(list.get(position).getFileName())).resize(UploadActivity.imag_width, UploadActivity.imag_height).centerCrop().into(mHolder.img_upload_phto);

        if(list.get(position).getIsSuccess()==1){
            mHolder.tv_status.setVisibility(View.VISIBLE);
            mHolder.progress.setVisibility(View.GONE);
            mHolder.tv_status.setText("上传成功");
            mHolder.tv_upload_delete.setText("删除");
        }else if(list.get(position).getIsSuccess()==0) {
            mHolder.tv_status.setVisibility(View.GONE);
//            mHolder.progress.setVisibility(View.VISIBLE);
            mHolder.tv_status.setText("上传中");
            mHolder.tv_upload_delete.setText("上传中");
        }else {
            mHolder.tv_status.setVisibility(View.VISIBLE);
            mHolder.progress.setVisibility(View.GONE);
            mHolder.tv_status.setText("上传失败");
            mHolder.tv_upload_delete.setText("重新上传");
        }

        mHolder.tv_size.setText(list.get(position).getFileSize());

        mHolder.rl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemOnClicklisten!=null){
                    onItemOnClicklisten.onItemDeleteClick(view,position);
                }
            }
        });
    }


    class BleViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_upload_phto;
        public TextView tv_size;
        public TextView tv_status;
        public TextView tv_upload_delete;
        public RelativeLayout rl_delete;
        public ProgressBar progress;

        public BleViewHolder(View view) {
            super(view);

            img_upload_phto = view.findViewById(R.id.img_upload_phto);
            tv_size = view.findViewById(R.id.tv_size);
            tv_status = view.findViewById(R.id.tv_status);
            rl_delete = view.findViewById(R.id.rl_delete);
            tv_upload_delete = view.findViewById(R.id.tv_upload_delete);
            progress = view.findViewById(R.id.progress);
        }
    }
}
