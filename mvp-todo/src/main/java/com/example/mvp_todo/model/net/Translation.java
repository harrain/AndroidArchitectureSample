package com.example.mvp_todo.model.net;

import android.util.Log;

/**
 * Created by net on 2017/12/7.
 */

public class Translation {

    private int status;

    private content content;
    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {
        Log.d("RxJava", content.out );
    }

    public String getResult(){
        return content.out;
    }
}
