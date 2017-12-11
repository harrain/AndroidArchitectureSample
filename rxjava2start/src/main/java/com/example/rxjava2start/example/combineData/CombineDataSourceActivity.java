package com.example.rxjava2start.example.combineData;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;
import com.example.rxjava2start.RetrofitBuilderHelper;
import com.example.rxjava2start.example.nestedNR.Translation1;
import com.example.rxjava2start.example.nestedNR.Translation2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * 合并数据源，有机组合，同时展示
 */
public class CombineDataSourceActivity extends AppCompatActivity {

    private DSInterface api;
    private String TAG = "CombineDataSourceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_data_source);

        api = RetrofitBuilderHelper.getInstance().create(DSInterface.class);
        combineNetworkData();

    }

    private void combineNetworkData() {
        Observable<Translation1> observable1 = api.getCall().subscribeOn(Schedulers.io());
        Observable<Translation2> observable2 = api.getCall_2().subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<Translation1, Translation2, String>() {

            @Override
            public String apply(Translation1 translation1, Translation2 translation2) throws Exception {
                return translation1.show() + " & "+translation2.show();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, "最终接收到的数据是：" + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
