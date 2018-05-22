package com.example.mvp_todo.base;

/**
 * Created by net on 2018/5/14.
 */

public interface ResultTransmitListener<T> {
    void onResult(T data);
    void onError(String error);
}
