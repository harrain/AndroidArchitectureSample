package com.example.rxjava2start.theory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 实践过滤操作符 filter ofType skip distinct take throttle sample debounce element
 */
public class FilterOperatorActivity extends AppCompatActivity {

    private String TAG = "FilterOperatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_operator);

//        filter();
//        ofType();
//        skipByCount();
//        skipByTime();
//        distinct();
//        distinctUntilChanged();
//        take();
//        takeLast();
//        throttleFirst();
//        throttleLast();
//        sample();
//        debounceOrthrottleWithTimeout();
//        firstElementAndLastElement();
//        elementAt();
//        elementAtOrError();
//        elementAtOrError2();
    }

    /**
     * 通过判断逻辑过滤特定条件下的事件
     */
    private void filter() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 1. 发送5个事件
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
            }

            // 2. 采用filter（）变换操作符
        }).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {

                // 根据test()的返回值 对被观察者发送的事件进行过滤 & 筛选
                // a. 返回true，则继续发送
                // b. 返回false，则不发送（即过滤）
                return integer > 3;
            }
        }).subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "过滤后得到的事件是："+ value  );
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

    /** 过滤 特定数据类型的数据 */
    private void ofType(){
        Observable.just(1,"damon",3,false)
                .ofType(Integer.class)// 筛选出 整型数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: "+integer);
                    }
                });
    }

    /**
     * 根据顺序跳过数据项
     */
    private void skipByCount(){
        Observable.just(1,2,3,4,5,6)
                .skip(2)//跳过前2项
                .skipLast(2)//跳过后2项
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: "+integer);
                    }
                });
    }

    /**
     * 根据时间跳过数据项
     */
    private void skipByTime(){
        Observable.intervalRange(0,10,0,1, TimeUnit.SECONDS)
                .skip(2,TimeUnit.SECONDS)//跳过前2秒发送的数据
                .skipLast(2,TimeUnit.SECONDS)//跳过后2秒发送的数据
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);
                    }
                });
    }

    /**
     * 过滤事件序列中重复的事件
     */
    private void distinct(){
        Observable.just(1,2,3,1,2)
                .distinct()//过滤事件序列中重复的事件
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG,"不重复的整型事件元素是： "+ integer);
                    }
                });
    }

    /**
     * 过滤事件序列中 连续重复的事件
     */
    private void distinctUntilChanged(){
        Observable.just(1,2,3,1,2,3,3,4,4)
                .distinctUntilChanged()//过滤事件序列中 连续重复的事件
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG,"不连续重复的整型事件元素是： "+ integer);
                    }
                });
    }

    /**
     * 指定观察者最多能接收到的事件数量
     */
    private void take(){
        Observable.just(1,2,3,4,5)
                .take(2)//只能接收到被观察者发送的前2个事件
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: "+integer);
                    }
                });
    }

    /**
     * 指定观察者只能接收到被观察者发送的最后几个事件
     */
    private void takeLast(){
        Observable.intervalRange(0,10,0,1,TimeUnit.SECONDS)
                .takeLast(3,TimeUnit.SECONDS)//只能接收到被观察者发送的最后3个事件
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);
                    }
                });
    }

    /**
     * 在某段时间内，只发送该段时间内第1次事件
     * 应用场景：一段时间内连续点击按钮，但只执行第1次的点击操作
     */
    private void throttleFirst(){
        Observable.intervalRange(0,10,0,1,TimeUnit.SECONDS)
                .throttleFirst(3,TimeUnit.SECONDS)//每3秒钟只发送该段时间内第1次事件
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);
                    }
                });
    }

    /**
     * 在某段时间内，只发送该段时间内最后1次事件
     */
    private void throttleLast(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);

                e.onNext(2);
                Thread.sleep(400);

                e.onNext(3);
                Thread.sleep(300);

                e.onNext(4);
                Thread.sleep(300);

                e.onNext(5);
                Thread.sleep(300);

                e.onNext(6);
                Thread.sleep(400);

                e.onNext(7);
                Thread.sleep(300);
                e.onNext(8);

                Thread.sleep(300);
                e.onNext(9);

                Thread.sleep(300);
                e.onComplete();
            }
        }).throttleLast(1, TimeUnit.SECONDS)//每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

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

    /**
     * 在某段时间内，只发送该段时间内最新（最后）1次事件
     * 与 throttleLast（） 操作符类似
     */
    private void sample(){
        Observable.intervalRange(0,10,0,1,TimeUnit.SECONDS)
                .sample(3,TimeUnit.SECONDS)//每3秒钟只发送该段时间内第1次事件
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);
                    }
                });
    }

    /**
     * 发送数据事件时，若2次发送事件的间隔＜指定时间，就会丢弃前一次的数据，直到指定时间内都没有新数据发射时才会发送后一次的数据
     */
    private void debounceOrthrottleWithTimeout(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);
                e.onNext(2); // 1和2之间的间隔小于指定时间1s，所以前1次数据（1）会被抛弃，2会被保留
                Thread.sleep(1500);  // 因为2和3之间的间隔大于指定时间1s，所以之前被保留的2事件将发出
                e.onNext(3);
                Thread.sleep(1500);  // 因为3和4之间的间隔大于指定时间1s，所以3事件将发出
                e.onNext(4);
                Thread.sleep(500); // 因为4和5之间的间隔小于指定时间1s，所以前1次数据（4）会被抛弃，5会被保留
                e.onNext(5);
                Thread.sleep(500); // 因为5和6之间的间隔小于指定时间1s，所以前1次数据（5）会被抛弃，6会被保留
                e.onNext(6);
                Thread.sleep(1500); // 因为6和Complete实践之间的间隔大于指定时间1s，所以之前被保留的6事件将发出

                e.onComplete();
            }
        }).debounce(1, TimeUnit.SECONDS)//每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

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

    /**
     * 仅选取第1个元素 / 最后一个元素
     */
    private void firstElementAndLastElement(){
        // 获取第1个元素
        Observable.just(1, 2, 3, 4, 5)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept( Integer integer) throws Exception {
                        Log.d(TAG,"获取到的第一个事件是： "+ integer);
                    }
                });

// 获取最后1个元素
        Observable.just(1, 2, 3, 4, 5)
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept( Integer integer) throws Exception {
                        Log.d(TAG,"获取到的最后1个事件是： "+ integer);
                    }
                });
    }

    /**
     * 指定接收某个元素（通过 索引值 确定）
     * 允许越界，即获取的位置索引 ＞ 发送事件序列长度
     */
    private void elementAt(){
        // 使用1：获取位置索引 = 2的 元素
        // 位置索引从0开始
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept( Integer integer) throws Exception {
                        Log.d(TAG,"获取到的事件元素是： "+ integer);
                    }
                });

// 使用2：获取的位置索引 ＞ 发送事件序列长度时，设置默认参数
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(6,10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept( Integer integer) throws Exception {
                        Log.d(TAG,"获取到的事件元素是： "+ integer);
                    }
                });
    }

    /**
     * 在elementAt（）的基础上，当出现越界情况（即获取的位置索引 ＞ 发送事件序列长度）时，即抛出异常
     */
    private void elementAtOrError(){
        Observable.just(1,2,3,4)
                .elementAtOrError(6)
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer value) {
                        Log.d(TAG, "onSuccess: "+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                    }
                });
    }

    private void elementAtOrError2(){
        Observable.just(1,2,3,4)
                .elementAtOrError(6)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: ");
                    }
                });
    }
}
