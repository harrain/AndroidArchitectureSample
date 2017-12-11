package com.example.rxjava2start.theory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 实践创建，延迟创建操作符 defer timer interval intervalRange range
 */
public class CreateOperatorActivity extends AppCompatActivity {

    private String TAG = "CreateOperatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_opreator);

//        defer();
//        timer();
//        interval();
        intervalRange();
    }

    /**
     * 直到有观察者（Observer ）订阅时，才动态创建被观察者对象（Observable） & 发送事件
     * 应用场景：动态创建被观察者对象（Observable） & 获取最新的Observable对象数据
     */
    private void defer() {
        final int[] i = new int[1];
        i[0] = 10;
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i[0]);
            }
        });
        i[0] = 15;
        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: "+integer);
            }
        });
    }

    /**
     * 延迟指定时间后，调用一次 onNext(),发送一条数据
     */
    private void timer(){
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);
                    }
                });
    }

    /**
     * 每隔指定时间 就发送一条数据，一直进行
     */
    private void interval(){
        Observable.interval(4,2,TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);
                    }
                });
    }

    /**
     * 每隔指定时间 就发送 事件，可指定发送的数据的数量
     */
    private void intervalRange(){
        Observable.intervalRange(3,10,3,1,TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);//事件从3到12
                    }
                });
    }

    /**
     * 无延迟发送一个事件，可指定事件起点和事件数量
     */
    private void range(){
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 注：若设置为负数，则会抛出异常
        Observable.range(3,10)
                // 该例子发送的事件序列特点：从3开始发送，每次发送事件递增1，一共发送10个事件
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe（）

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件"+ value  );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });


    }
}
