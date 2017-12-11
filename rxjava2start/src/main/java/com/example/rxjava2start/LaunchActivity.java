package com.example.rxjava2start;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.rxjava2start.example.Rx2ExampleActivity;
import com.example.rxjava2start.theory.Rx2MainActivity;

import java.util.ArrayList;

public class LaunchActivity extends AppCompatActivity {

    private ArrayList<MainDM> mList = new ArrayList<>();
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx2_main);

        initRecyclerView();

        mList.add(new MainDM("rx理论知识： 基本使用流程和操作符", new Intent(this, Rx2MainActivity.class)));
        mList.add(new MainDM("rx知识点例子实践", new Intent(this, Rx2ExampleActivity.class)));
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
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

}
