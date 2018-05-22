package com.example.mvp_todo.model.net;

import android.util.Log;

import com.example.mvp_todo.base.ResultTransmitListener;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by net on 2018/5/13.
 */

public class RequestServer {

    NetApi apiRequest;
    private String TAG = "RequestServer";
    private ResultTransmitListener<String> resultTransmitListener;

    public void netAccess() {
        Observable<Translation> observable = buildRequest().getCall();
        observable.subscribeOn(Schedulers.io())//若Observable.subscribeOn（）多次指定被观察者 生产事件的线程，则只有第一次指定有效，其余的指定线程无效
                .observeOn(AndroidSchedulers.mainThread())//若Observable.observeOn（）多次指定观察者 接收 & 响应事件的线程，则每次指定均有效，即每指定一次，就会进行一次线程的切换
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation value) {
                        value.show();
                        if (resultTransmitListener!=null) resultTransmitListener.onResult(value.getResult());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private NetApi buildRequest(){
        if (apiRequest == null){
            Retrofit retrofit = RetrofitBuilderHelper.getInstance();
            apiRequest = retrofit.create(NetApi.class);
        }
        return apiRequest;
    }

    public void setResultTransmitListener(ResultTransmitListener<String> listener){
        resultTransmitListener = listener;
    }
}
