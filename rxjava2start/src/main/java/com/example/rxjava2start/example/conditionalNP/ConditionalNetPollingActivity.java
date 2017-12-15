package com.example.rxjava2start.example.conditionalNP;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;
import com.example.rxjava2start.RetrofitBuilderHelper;
import com.example.rxjava2start.example.netPolling.NetApi;
import com.example.rxjava2start.example.netPolling.Translation;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
/**
 * （有条件）网络请求轮询
 */
public class ConditionalNetPollingActivity extends AppCompatActivity {

    volatile int i = 0;
    private String TAG = "ConditionalNetPollingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditional_net_polling);
        
        conditionNetPolling();
    }

    private void conditionNetPolling() {
        Observable<Translation> observable = RetrofitBuilderHelper.getInstance().create(NetApi.class).getCall();
        observable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                /** 必须对输入的 objectObservable 进行处理，此处使用flatMap */
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {

                    // 将原始 Observable 停止发送事件的标识（Complete（） /  Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable）
                    // 以此决定是否重新订阅 & 发送原来的 Observable，即轮询
                    // 此处有2种情况：
                    // 1. 若返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable，即轮询结束
                    // 2. 若返回其余事件，则重新订阅 & 发送原来的 Observable，即继续轮询
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                        if (i  > 3) return Observable.error(new Throwable("轮询结束"));
                        // 若轮询次数＜5次，则发送1个Next事件以继续轮询
                        // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间隔设置
                        return Observable.just(0).delay(2, TimeUnit.SECONDS);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation value) {
                        value.show();
                        i++;
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
}
