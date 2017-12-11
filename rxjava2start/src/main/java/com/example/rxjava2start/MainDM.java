package com.example.rxjava2start;

import android.content.Intent;

/**
 * Created by net on 2017/12/8.
 */

public class MainDM{
    public String title;
    public Intent intent;

    public MainDM(String title, Intent intent) {
        this.title = title;
        this.intent = intent;
    }
}
