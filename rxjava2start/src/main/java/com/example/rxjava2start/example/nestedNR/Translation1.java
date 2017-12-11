package com.example.rxjava2start.example.nestedNR;

import android.util.Log;

/**
 * Created by net on 2017/12/8.
 */

public class Translation1 {
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
    public String show() {

        Log.d("RxJava", "first 翻译内容 = " + content.out);

        return "first 翻译内容 = " + content.out;

    }
}

