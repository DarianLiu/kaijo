package com.geek.kaijo.mvp.model.entity;

import java.util.List;

/**
 * 数组数据封装
 * Created by LiuLi on 2018/9/8.
 */

public class BaseArrayResult<T> {
    private int total;
    private int size;
    private int current;
    private List<T> records;

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getRecords() {
        return records;
    }
}
