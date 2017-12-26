package com.example.rxjava2start.example.errorReconnection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;
import com.example.rxjava2start.RetrofitBuilderHelper;
import com.example.rxjava2start.example.netPolling.NetApi;
import com.example.rxjava2start.example.netPolling.Translation;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ErrorReconnectActivity extends AppCompatActivity {

    int maxConnectCount = 3;
    int currentRetryCount = 0;
    int waitRetryTime = 2000;
    private String TAG = "ErrorReconnectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_reconnect);

        Observable<Translation> observable = RetrofitBuilderHelper.getInstance().create(NetApi.class).getCall();

        errorReconnect(observable);
    }

    private void errorReconnect(Observable<Translation> observable) {
//        observable.retry(new BiPredicate<Integer, Throwable>() {
//            @Override
//            public boolean test(Integer integer, Throwable throwable) throws Exception {
//                return false;
//            }
//        });
//
//        observable.retry(3, new Predicate<Throwable>() {
//            @Override
//            public boolean test(Throwable throwable) throws Exception {
//                return false;
//            }
//        });

        observable.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {

            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {

                        if (throwable instanceof IOException){
                            if (currentRetryCount <= maxConnectCount){
                                currentRetryCount ++;
                                return Observable.just(0).delay(waitRetryTime, TimeUnit.MILLISECONDS);
                            }else return Observable.error(new Throwable("加载失败"));
                        }else return Observable.error(new Throwable("非IO异常"));

                    }
                });
            }
        })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: 已重试 "+currentRetryCount+" 次");
                    }

                    @Override
                    public void onNext(Translation value) {
                        value.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage() );

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
