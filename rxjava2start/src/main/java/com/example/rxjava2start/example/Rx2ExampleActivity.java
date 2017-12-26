package com.example.rxjava2start.example;

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
import com.example.rxjava2start.example.associationSearchOptimize.AssociationSearchOptimizedActivity;
import com.example.rxjava2start.example.combineData.CombineDataSourceActivity;
import com.example.rxjava2start.example.conditionalNP.ConditionalNetPollingActivity;
import com.example.rxjava2start.example.coojudge.CooJudgeActivity;
import com.example.rxjava2start.example.errorReconnection.ErrorReconnectActivity;
import com.example.rxjava2start.example.nestedNR.NestedNetRequstActivity;
import com.example.rxjava2start.example.netPolling.PollingActivity;
import com.example.rxjava2start.example.taskOnly.TaskOnlyActivity;
import com.example.rxjava2start.example.twolevelcache.CacheSimulationActivity;

import java.util.ArrayList;

public class Rx2ExampleActivity extends AppCompatActivity {

    private ArrayList<MainDM> mList = new ArrayList<>();
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx2_main);

        initRecyclerView();

        mList.add(new MainDM("（无条件）网络请求轮询",new Intent(this,PollingActivity.class)));
        mList.add(new MainDM("网络请求嵌套回调",new Intent(this,NestedNetRequstActivity.class)));
        mList.add(new MainDM("合并数据源，有机组合，同时展示",new Intent(this,CombineDataSourceActivity.class)));
        mList.add(new MainDM("从二级缓存及网络获取数据",new Intent(this,CacheSimulationActivity.class)));
        mList.add(new MainDM("联合判断多个事件",new Intent(this,CooJudgeActivity.class)));
        mList.add(new MainDM("（有条件）网络请求轮询",new Intent(this,ConditionalNetPollingActivity.class)));
        mList.add(new MainDM("网络请求出错重连",new Intent(this,ErrorReconnectActivity.class)));
        mList.add(new MainDM("功能防抖，任务执行不冗余",new Intent(this,TaskOnlyActivity.class)));
        mList.add(new MainDM("联想搜索优化",new Intent(this,AssociationSearchOptimizedActivity.class)));
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
