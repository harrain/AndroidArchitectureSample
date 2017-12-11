package com.example.rxjava2start.example.nestedNR;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;
import com.example.rxjava2start.RetrofitBuilderHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * 网络请求嵌套回调
 */
public class NestedNetRequstActivity extends AppCompatActivity {

    Observable<Translation1> observable1;
    Observable<Translation2> observable2;
    NetRequestInterface request;
    private String TAG = "NestedNetRequstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_net_requst);
        request = buildRequest();
        nestedNR();
    }

    private void nestedNR(){
        observable1 = request.getCall();
        observable2 = request.getCall_2();

        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Translation1>() {
                    @Override
                    public void accept(Translation1 translation1) throws Exception {
                        Log.d(TAG, "第1次网络请求成功");
                        translation1.show();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Translation1, ObservableSource<Translation2>>() {
                    @Override
                    public ObservableSource<Translation2> apply(Translation1 translation1) throws Exception {
                        return observable2;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation2>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation2 value) {
                        Log.d(TAG, "第2次网络请求成功");
                        value.show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private NetRequestInterface buildRequest(){
        Retrofit retrofit = RetrofitBuilderHelper.getInstance();
        return  retrofit.create(NetRequestInterface.class);


    }
}
