package com.example.rxjava2start.theory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 实践变换操作符，map flatMap concatMap buffer
 */
public class TransferOperatorActivity extends AppCompatActivity {

    private String TAG= "TransferOperatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_opreator);

//        map();
//        flatMap();
//        concatMap();
//        buffer();
        bufferTU();
    }

    /**
     * 将被观察者发送的事件转换为任意的类型事件。
     * 应用场景：数据转换
     */
    private void map() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);e.onNext(2);e.onNext(3);
                e.onComplete();

            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "integer to string---"+integer;
            }
        });
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: "+s);
            }
        });
    }
    
    /**
     * 将发送的每个事件转换包装后放进一个新的被观察者，最后将总的被观察者的全部数据无序输出
     * 应用场景： 发送期间，加入新的被观察者。事件转换成新的被观察者创建的事件
     */
    private void flatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);e.onNext(2);e.onNext(3);
                e.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                List<String> list = new ArrayList<>();
                for (int i=0;i<3;i++){
                    list.add("我是事件"+integer+"分出来的子事件"+i);
                }
                return Observable.fromIterable(list);// 最终合并，再发送给被观察者
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: "+s);
            }
        });
    }

    /**
     * 将发送的每个事件转换包装后放进一个新的被观察者，最后将总的被观察者的全部数据无序输出
     * 应用场景：有序的将被观察者发送的整个事件序列进行变换
     */
    private void concatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);e.onNext(2);e.onNext(3);
                e.onComplete();
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i=0;i<3;i++){
                    list.add("我是事件"+integer+"分出来的子事件"+i);
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: "+s);
            }
        });
    }

    /**
     * 定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件并放到缓存区中，最终发送
     * 应用场景：缓存被观察者发送的事件
     */
    private void buffer(){
        Observable.just(1,2,3,4,5)
                .buffer(3,1)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG, "accept: size : "+integers.size());
                        for (Integer integer : integers) {
                            Log.d(TAG, "accept: congtent : "+integer);
                        }
                    }
                });
    }

    private void bufferTU(){
        Observable.just(1,2,3,4,5)
                .buffer(10, TimeUnit.SECONDS,3)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG, "accept: size : "+integers.size());
                        for (Integer integer : integers) {
                            Log.d(TAG, "accept: congtent : "+integer);
                        }
                    }
                });
    }
}
