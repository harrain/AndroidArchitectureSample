package com.example.rxjava2start.theory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;

import com.example.rxjava2start.R;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 实践合并操作符 concat concatArray concatArrayDelayError merge mergeArray mergeDelayError
 *      zip combineLatest combineDelayError reduce collect startWith startWithArray count
 */
public class CombineOperatorActivity extends AppCompatActivity {

    private String TAG = "CombineOperatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_observable);

//        concat();
//        concatArray();
//        concatArrayDelayError();
//        merge();
//        mergeArray();
//        mergeDelayError();
//        zip();
//        combineLatest();
//        combineDelayError();
//        reduce();
//        collect();
//        startWith();
        count();
    }

    /**
     * 组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
     * 源码中concat方法是调用的concatArray(),所以直接使用concatArray()即可
     * 应用场景：需要合并被观察者，而且串行执行
     */
    private void concat() {
        io.reactivex.Observable.concat(
                Observable.just(1, 2, 3),
                Observable.just(4, 5, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
    }

    private void concatArray() {
        Observable.concatArray(
                Observable.just(0, 1),
                Observable.just(2, 3),
                Observable.just(4, 5),
                Observable.just(6, 7),
                Observable.just(8)
        ).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: " + integer);
            }
        });
    }

    /**
     * 直接使用此方法，无需使用concatDelayError()
     */
    private void concatArrayDelayError() {
        Observable.concatArrayDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(1);
                        e.onNext(2);
                        e.onNext(3);
                        e.onError(new Throwable("我是异常"));
                        e.onComplete();
                    }
                }),

                Observable.just(4, 5, 6)

        ).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: enen");
            }
        });
    }

    /**
     * 组合多个被观察者一起发送数据，合并后 按时间线并行执行
     * 应用场景：合并被观察者， 并行执行
     */
    private void merge() {
        Observable.merge(
                Observable.intervalRange(0, 3, 0, 1, TimeUnit.SECONDS),

                Observable.create(new ObservableOnSubscribe<Long>() {
                    @Override
                    public void subscribe(ObservableEmitter<Long> e) throws Exception {
                        e.onNext(4l);
                        Thread.sleep(1000);
                        e.onNext(5l);
                        Thread.sleep(1000);
                        e.onNext(6l);
                        e.onComplete();
                    }
                })
        ).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "accept: " + aLong);
            }
        });
    }

    /**
     * 合并后按照时间线并行执行
     */
    private void mergeArray() {
        Observable.mergeArray(
                Observable.intervalRange(1, 3, 1, 2, TimeUnit.SECONDS),
                Observable.intervalRange(4, 7, 1, 2, TimeUnit.SECONDS)
        ).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "accept: " + aLong);
            }
        });
    }

    private void mergeDelayError() {
        Observable.mergeDelayError(
                Observable.create(new ObservableOnSubscribe<Long>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Long> e) throws Exception {

                        e.onNext(4l);
                        Thread.sleep(5000);
                        e.onNext(5l);
                        Thread.sleep(5000);
                        e.onNext(6l);

                        e.onError(new Throwable("我是异常"));
                        e.onComplete();


                    }
                }).subscribeOn(Schedulers.io()),
                Observable.create(new ObservableOnSubscribe<Long>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Long> e) throws Exception {
                        e.onNext(0l);
                        Thread.sleep(5000);
                        e.onNext(1l);
                        Thread.sleep(5000);
                        e.onNext(2l);
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread())
        ).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
    }

    /**
     * 合并 多个被观察者（Observable）发送的事件，按照一定的事件组合规则，生成一个新的事件序列，并最终发送
     * 应用场景：对多个数据接口中的数据进行合并，然后按照某种形式输出
     * 事件组合规则 = 严格按照原先事件序列 进行对位合并
     * 最终合并的事件数量 = 多个被观察者（Observable）中事件最少的数量
     */
    private void zip() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "被观察者1发送了事件1");
                emitter.onNext(1);
                // 为了方便展示效果，所以在发送事件后加入2s的延迟
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件3");
                emitter.onNext(3);


                emitter.onComplete();


            }
        }).subscribeOn(Schedulers.io());
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "被观察者2发送了事件A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件C");
                emitter.onNext("C");
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d(TAG, "被观察者2发送了事件D");
                emitter.onNext("D");


                emitter.onComplete();


            }
        }).subscribeOn(Schedulers.newThread());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {

            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: "+s);
            }
        });
    }

    /**
     * 当两个Observables中的任何一个发送了数据后，将先发送了数据的Observables 的最新（最后）一个数据 与 另外一个Observable发送的每个数据结合，最终基于该函数的结果发送数据
     * 注意点：按时间合并，即在同一个时间点上合并 或者 把顺序上排在最后的数据进行合并
     * 应用场景：合并一组可能发生变化的数据，合并都是最新的情况。数据变化后对多组数据联合判断
     */
    private void combineLatest(){
        Observable.combineLatest(
                Observable.just(1l, 2l, 3l),
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
                new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long o1, Long o2) throws Exception {
                        // o1 = 第1个Observable发送的最新（最后）1个数据
                        // o2 = 第2个Observable发送的每1个数据
                        Log.e(TAG, "合并的数据是： "+ o1 + " and "+ o2);
                        return o1 + o2;
                        // 合并的逻辑 = 相加
                        // 即第1个Observable发送的最后1个数据 与 第2个Observable发送的每1个数据进行相加

                    }
                }
        ).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "accept: "+aLong);
            }
        });
    }

    private void combineDelayError(){
        Observable<Long> observable1 = Observable.intervalRange(0,3,0,1,TimeUnit.SECONDS);
        Observable<Long> observable2 = Observable.create(new ObservableOnSubscribe<Long>() {

                    @Override
                    public void subscribe(ObservableEmitter<Long> e) throws Exception {
                        e.onNext(5L);
                        Thread.sleep(1000);
                        e.onNext(6l);
                        Thread.sleep(1000);
                        e.onNext(7l);
                        e.onError(new Throwable(" i am exception"));
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io());

        Observable[] observables = {observable1,observable2};
//        observables.add(observable1);
//        observables.add(observable2);
        Observable.combineLatestDelayError(
                observables,
                new Function<Object[],Long>() {


                    @Override
                    public Long apply(Object[] aLong) throws Exception {
                        Log.d(TAG, "apply: 合并的数据是： "+(Long) aLong[0]+" and "+(Long) aLong[1]);
                        return (Long) aLong[0] + (Long) aLong[1];
                    }
                }

        ).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {
                Log.d(TAG, "onNext: result -- "+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "+e.getMessage() );
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
    }

    /**
     * 合并被观察者发送的所有事件，聚合成一个事件 ，输出结果
     * 本质都是前2个数据聚合，然后与后1个数据继续进行聚合，依次类推
     */
    private void reduce(){
        Observable.just(1,2,3,4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.d(TAG, "apply: "+integer +" 乘 "+integer2);
                        return integer * integer2;
                        // 本次聚合的逻辑是：全部数据相乘起来
                        // 原理：第1次取前2个数据相乘，之后每次获取到的数据 = 返回的数据x下1个数据
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: 最终结果是 "+integer);
            }
        });
    }

    /**
     * 将被观察者Observable发送的数据收集到一个数据结构里
     * 应用场景：创建数据结构（容器），用于收集被观察者发送的数据
     */
    private void collect(){
        Observable.just(1,2,3,4,5)
                .collect(
                        new Callable<ArrayList<Integer>>() {
                            @Override
                            public ArrayList<Integer> call() throws Exception {
                                return new ArrayList<>();//  创建数据结构（容器），用于收集被观察者发送的数据
                            }
                        },
                        new BiConsumer<ArrayList<Integer>, Integer>() {
                            @Override
                            public void accept(ArrayList<Integer> list, Integer integer) throws Exception {
                                // 参数说明：list = 容器，integer = 后者数据
                                list.add(integer);
                                // 对发送的数据进行收集
                            }
                        }

                )
                .subscribe(new Consumer<ArrayList<Integer>>() {
                    @Override
                    public void accept(ArrayList<Integer> integers) throws Exception {
                        Log.d(TAG, "accept: "+integers);
                    }
                });
    }

    /**
     * 在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
     */
    private void startWith(){
        Observable.just(7,8,9)
                .startWithArray(4,5,6)
                .startWith(Observable.just(1,2,3))
                .startWith(0)
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: "+integer);
            }
        });
    }

    /**
     * 统计被观察者发送事件的数量
     */
    private void count(){
        Observable.just(1,2,3,4)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: 发送的事件数是 "+aLong);
                    }
                });
    }



}
