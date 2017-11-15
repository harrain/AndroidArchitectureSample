package com.example.dagger2demo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by data on 2017/11/14.
 */

@Module
public class MainModule {

    private MainActivity mainActivity;

    public MainModule(MainActivity activity){ mainActivity = activity;}

    @Provides
    public MainActivity providesActivity(){return mainActivity;}

    @Provides
    public User providesUser(){return new User("the user from MainModule");}

    @Provides
    public MainPresenter providesMainPresenter(MainActivity activity,User user){
        return new MainPresenter(activity,user);
    }



}
