package com.laotsezu.materialdesign;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


public class MyCustomRecyclerViewAdapter extends RecyclerView.Adapter<MyCustomRecyclerViewAdapter.MyCustomViewHolder> {

    public MyCustomRecyclerViewAdapter(){

    }
    class MyCustomViewHolder extends RecyclerView.ViewHolder{

        public MyCustomViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public MyCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(MyCustomViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
