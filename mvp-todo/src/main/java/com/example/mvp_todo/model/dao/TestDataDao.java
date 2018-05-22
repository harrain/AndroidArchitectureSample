package com.example.mvp_todo.model.dao;

import com.example.mvp_todo.base.ResultTransmitListener;
import com.example.mvp_todo.model.net.RequestServer;

/**
 * Created by net on 2018/5/14.
 */

public class TestDataDao {

    private RequestServer requestServer;

    public TestDataDao(){
        requestServer = new RequestServer();
    }

    public void loadData(ResultTransmitListener<String> listener){
        requestServer.setResultTransmitListener(listener);
        requestServer.netAccess();
    }
}
