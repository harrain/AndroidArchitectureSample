package com.example.rxjava2start.example.coojudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.rxjava2start.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * 联合判断多个事件
 */
public class CooJudgeActivity extends AppCompatActivity {
    EditText name,age,job;
    Button list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coo_judge);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        job = (EditText) findViewById(R.id.job);
        list = (Button) findViewById(R.id.list);

        cooJudge();

    }

    private void cooJudge() {
        final Observable<CharSequence> nameObservale = RxTextView.textChanges(name).skip(1);//采用skip(1)原因：跳过 一开始EditText无任何输入时的空值
        Observable<CharSequence> jobObservale = RxTextView.textChanges(job).skip(1);
        Observable<CharSequence> ageObservale = RxTextView.textChanges(age).skip(1);

        Observable.combineLatest(nameObservale, ageObservale, jobObservale, new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) throws Exception {
                boolean isNameValid = !TextUtils.isEmpty(name.getText());
                boolean isAgeValid = !TextUtils.isEmpty(age.getText());
                boolean isJobValid = !TextUtils.isEmpty(job.getText());
                return isNameValid && isAgeValid && isJobValid;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                list.setEnabled(aBoolean);
            }
        });

    }
}
