package com.example.rxjava2start;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;

/**
 * Created by net on 2017/12/8.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyHolder>{
    private ArrayList<MainDM> mList;

    public MainAdapter(ArrayList<MainDM> list){
        mList = list;
    }
    OnItemClickListener mListener;

    @Override
    public MainAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MainAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter.MyHolder holder, int position) {
        holder.tv.setText(mList.get(position).title);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemCLickListener(OnItemClickListener listener){mListener = listener;}

    class MyHolder extends RecyclerView.ViewHolder{

        TextView tv;
        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.item_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        mListener.onClick(v,getAdapterPosition());
                    }
                }
            });
        }
    }
}
