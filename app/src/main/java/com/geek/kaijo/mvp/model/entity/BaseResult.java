package com.geek.kaijo.mvp.model.entity;

/**
 * 服务器返回数据统一处理
 * Created by LiuLi on 2018/9/4.
 */

public class BaseResult<T> {

    /**
     * result : 422
     * msg : 密码错误
     * data : null
     */

    private int result;
    private String msg;
    private T data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
