package com.example.mvp_todo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mvp_todo.R;
import com.example.mvp_todo.contract.TestDataContract;
import com.example.mvp_todo.presenter.ObtainDataPresenter;

public class MainActivity extends AppCompatActivity implements TestDataContract.UI{

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.content);
        new ObtainDataPresenter(this).obtainData();

    }

    @Override
    public void showData(String data) {
        textView.setText(data);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void setPresenter(TestDataContract.Presenter presenter) {

    }


}
