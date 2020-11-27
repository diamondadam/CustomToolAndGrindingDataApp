package com.example.customtooldataapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customtooldataapp.R;

import java.util.ArrayList;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder> {
    LayoutInflater layoutInflater;
    ArrayList<String> jobNames;

    public TransactionRecyclerAdapter(Context context, ArrayList<String> jobNames){
        this.layoutInflater = LayoutInflater.from(context);
        this.jobNames = jobNames;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View transactionView = layoutInflater.inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(transactionView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        String currentJobName = jobNames.get(position);
        holder.jobName.setText(currentJobName);
    }

    @Override
    public int getItemCount() {
        return jobNames.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView jobName;
        private TextView operationName;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobName);
            operationName = itemView.findViewById(R.id.operationName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("OnClick", String.valueOf(jobName.getText()));
        }
    }
}
