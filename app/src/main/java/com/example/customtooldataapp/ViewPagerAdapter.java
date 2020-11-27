package com.example.customtooldataapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>{
    private List<String> mData;
    private LayoutInflater layoutInflater;
    private ViewPager2 viewPager2;

    ViewPagerAdapter(Context context, List<String> data, ViewPager2 viewPager2){
        this.layoutInflater = LayoutInflater.from(context);
        this.mData = data;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.jobName.setText(animal);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView jobName;
        TextView operationName;
        RelativeLayout relativeLayout;

        ViewHolder(View view){
            super(view);
            jobName = view.findViewById(R.id.jobName);
            operationName = view.findViewById(R.id.operationName);
            relativeLayout = view.findViewById(R.id.transactionContainer);
        }
    }
}
