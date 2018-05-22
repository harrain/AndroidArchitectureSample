package com.example.mvp_todo.contract;

import com.example.mvp_todo.base.BasePresenter;
import com.example.mvp_todo.base.BaseUI;

/**
 * Created by net on 2018/5/14.
 */

public interface TestDataContract {

    interface Presenter extends BasePresenter{
        void obtainData();
    }

    interface UI extends BaseUI<Presenter>{
        void showLoading();
        void dismissLoading();
        void showData(String data);
    }
}
