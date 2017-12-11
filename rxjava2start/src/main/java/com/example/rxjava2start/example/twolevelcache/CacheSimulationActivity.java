package com.example.rxjava2start.example.twolevelcache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava2start.R;

import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * 从二级缓存及网络获取数据
 */
public class CacheSimulationActivity extends AppCompatActivity {

    String memoryCache = null;//模拟内存
    String diskCache = "磁盘数据";//模拟磁盘缓存
    private String TAG = "CacheSimulationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_simulation);

        getDataFromTwoLevelCache();
    }

    private void getDataFromTwoLevelCache() {
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (memoryCache !=null) e.onNext(memoryCache);
                else e.onComplete();
            }
        });
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (diskCache != null) e.onNext(diskCache);
                else e.onComplete();
            }
        });
        Observable<String> network = Observable.just("网络数据");
        Observable.concat(memory,disk,network)
                .firstElement()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG,"最终获取的数据来源 =  "+ s);
                    }
                });
    }
}
