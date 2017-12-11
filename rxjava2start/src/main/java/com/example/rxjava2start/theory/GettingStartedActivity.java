package com.example.rxjava2start.theory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 实践使用Rx的最基础骨架，Observable.subscribe(observer)
 */
public class GettingStartedActivity extends AppCompatActivity {

    private String TAG = "GettingStartedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        connect();
    }

    private void connect() {
//        createObservable().subscribe(createObserver());
//        createObservableByFrom().subscribe(createObserver());
        listDo();
    }

    private void listDo() {
        Integer[] nums = {1,2,3};
        Observable.fromArray(nums)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: "+integer);
                    }
                });
    }

    /** 创建被观察者 （Observable ）& 生产事件 */
    private Observable<Integer> createObservable() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            // 当 Observable 被订阅时，OnSubscribe 的 call() 方法会自动被调用，即事件序列就会依照设定依次被触发
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });
        return observable;
    }

    /** just(T...)：直接将传入的参数依次发送出来 */
    private Observable<Integer> createObservableByJust(){
        return Observable.just(1,2,3);

    }
    /** from(Iterable<? extends T>) : 将传入的数组 / Iterable 拆分成具体对象后，依次发送出来 */
    private Observable<Integer> createObservableByFrom(){
        List<Integer> nums = new ArrayList<>();
        nums.add(1);nums.add(2);nums.add(3);
        return Observable.fromIterable(nums);
    }

    /** 创建观察者 （Observer ）对象,复写对应事件，从而响应 */
    private Observer<Integer> createObserver(){
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

                Log.d(TAG, "onSubscribe: 开始订阅 disposable "+d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: 做出响应 "+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: 事件结束");
            }
        };
    }

    private Subscriber<Integer> createSubscriber(){
        return new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe: s "+s);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: 做出响应 "+integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: 事件结束");
            }
        };
    }

    
    
}
