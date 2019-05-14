package com.geek.kaijo.mvp.ui.adapter;

import android.view.View;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.ui.adapter.holder.SocialItemHolder;
import com.geek.kaijo.mvp.ui.adapter.holder.SocialSearchItemHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 社会治理Adapter
 * Created by LiuLi on 2019/1/24.
 */

public class SocialThingAdapter extends DefaultAdapter<ThingPositionInfo> {

    private int entryType;

    public SocialThingAdapter(List<ThingPositionInfo> infos, int entryType) {
        super(infos);
        this.entryType = entryType;
    }

    @Override
    public int getItemCount() {
        return mInfos.size() + 1;
    }

    @Override
    public BaseHolder<ThingPositionInfo> getHolder(View v, int viewType) {

        if (viewType == 0) {
            return new SocialSearchItemHolder(v, entryType);
        } else {
            return new SocialItemHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(BaseHolder<ThingPositionInfo> holder, int position) {
        if (position == 0) {
            holder.setData(null, position);
        } else {
            holder.setData(mInfos.get(position - 1), position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.item_search_key;
        } else {
            return R.layout.item_contorl_service;
        }
    }


    protected ItemClickListener mOnItemClickListener = null;
    public interface ItemClickListener<T> {
        void onItemClick(View view,int position);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
