package com.example.mvp_todo.presenter;

import com.example.mvp_todo.base.BasePresenter;
import com.example.mvp_todo.base.ResultTransmitListener;
import com.example.mvp_todo.contract.TestDataContract;
import com.example.mvp_todo.model.dao.TestDataDao;

/**
 * Created by net on 2018/5/14.
 */

public class ObtainDataPresenter implements TestDataContract.Presenter {

    private TestDataDao testDataDao;
    private TestDataContract.UI mainUI;

    public ObtainDataPresenter(TestDataContract.UI ui){
        mainUI = ui;
        ui.setPresenter(this);
        testDataDao = new TestDataDao();
    }
    @Override
    public void start() {

    }

    @Override
    public void obtainData() {
        testDataDao.loadData(new ResultTransmitListener<String>() {
            @Override
            public void onResult(String data) {
                mainUI.showData(data);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
