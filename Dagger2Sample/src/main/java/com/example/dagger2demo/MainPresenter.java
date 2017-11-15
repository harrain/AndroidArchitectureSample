package com.example.dagger2demo;

/**
 * Created by data on 2017/11/14.
 */

public class MainPresenter {

    private MainActivity activity;
    private User user;


    public MainPresenter(MainActivity mainActivity,User u){
        activity = mainActivity;
        user = u;
    }



    public void showUserName(){
        activity.showUserName(user.name);
    }



}
