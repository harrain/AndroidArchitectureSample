package com.example.dagger2demo;

import dagger.Component;

/**
 * Created by data on 2017/11/14.
 */
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
