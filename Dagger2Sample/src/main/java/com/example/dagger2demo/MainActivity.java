package com.example.dagger2demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        inject();
        mainPresenter.showUserName();

    }

    private void inject() {
        DaggerMainComponent.builder()
                .mainModule(new MainModule(this))//DaggerMainComponent与MainActivityModule的实例绑定
                .build()
                .inject(this);//再与调用者或者被依赖者MainActivity绑定,inject方法即MainComponent接口里声明的inject(),参数为MainActivity的引用
    }

    public void showUserName(String name) {
        textView.setText(name);
    }
}
