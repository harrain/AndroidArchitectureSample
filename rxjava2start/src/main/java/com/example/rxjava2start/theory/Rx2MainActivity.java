package com.example.rxjava2start.theory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.rxjava2start.MainAdapter;
import com.example.rxjava2start.MainDM;
import com.example.rxjava2start.OnItemClickListener;
import com.example.rxjava2start.R;

import java.util.ArrayList;
/**
 * 该Activity是目录,其他Activity是具体类别的操作符实践
 */
public class Rx2MainActivity extends AppCompatActivity {
    private ArrayList<MainDM> mList = new ArrayList<>();
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx2_main);

        initRecyclerView();

        mList.add(new MainDM("rx初步使用流程：Observable subcribe Observer",new Intent(this,GettingStartedActivity.class)));
        mList.add(new MainDM("rx延迟创建操作符：defer timer interval intervalrange range",new Intent(this,CreateOperatorActivity.class)));
        mList.add(new MainDM("rx变换操作符：map flatMap concatMap buffer",new Intent(this,TransferOperatorActivity.class)));
        mList.add(new MainDM("rx合并操作符：concat merge zip combineLatest reduce collect startWith count",new Intent(this,CombineOperatorActivity.class)));
        mList.add(new MainDM("rx功能操作符：doxxx onErrorReturn retry repeat",new Intent(this,FunctionalOperatorActivity.class)));
        mList.add(new MainDM("rx过滤操作符：filter ofType skip distinct take throttle",new Intent(this,FilterOperatorActivity.class)));
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(mList);
        adapter.setOnItemCLickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(mList.get(position).intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }




}
