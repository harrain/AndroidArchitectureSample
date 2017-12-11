package com.example.rxjava2start.example.netPolling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rxjava2start.R;
import com.example.rxjava2start.RetrofitBuilderHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * 无条件网络轮询
 */
public class PollingActivity extends AppCompatActivity {

    NetApi apiRequest;
    Disposable mDisposable;
    private String TAG = "PollingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);

        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setBackgroundColor(getResources().getColor(android.R.color.secondary_text_dark));
                button.setEnabled(false);
                if (mDisposable != null && !mDisposable.isDisposed()){
                    mDisposable.dispose();
                }
            }
        });

        rxPolling();


    }

    private void rxPolling() {
        Observable.interval(2,5, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        netAccess();
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void netAccess() {
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

}
