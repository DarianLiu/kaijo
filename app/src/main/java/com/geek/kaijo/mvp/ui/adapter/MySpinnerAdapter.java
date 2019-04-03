package com.geek.kaijo.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geek.kaijo.R;

import java.util.List;

/**
 * Spinner适配器
 * Created by LiuLi on 2018/9/6.
 */
public class MySpinnerAdapter<T> extends ArrayAdapter {

    private LayoutInflater inflater;
    private List<T> datas;

    public MySpinnerAdapter(@NonNull Context context, @NonNull List<T> objects) {
        super(context, R.layout.spinner_list_item, objects);
        setDropDownViewResource(R.layout.spinner_select_singlechoice);
        this.inflater = LayoutInflater.from(context);
        this.datas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.spinner_list_item, null);
        TextView textView = convertView.findViewById(android.R.id.text1);
       if (position < datas.size()) {
            T t = datas.get(position);
            textView.setText(t.toString());
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return datas.size();
    }
}
