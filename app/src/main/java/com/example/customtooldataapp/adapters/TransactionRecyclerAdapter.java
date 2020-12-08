package com.example.customtooldataapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder> {
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;
    private Lifecycle lifecycle;
    private List<Transaction> transactions;

    public TransactionRecyclerAdapter(Fragment fragment, List<Transaction> transactions){
        Log.d("TransactionRecyclerAdap", "Constructor");
        this.layoutInflater = LayoutInflater.from(fragment.getContext());
        this.fragmentManager = fragment.getChildFragmentManager();
        this.lifecycle = fragment.getLifecycle();
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TransactionRecyclerAdap", "onCreateViewHolder");
        View transactionView = layoutInflater.inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(transactionView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        if(transactions == null){
            Log.d("TransactionRecycAdapter", "Transactions are null...");
            return 0;
        }else{
            Log.d("TransactionRecycAdapter", "Size" +  transactions.size());
            return transactions.size();
        }
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ViewPager2 viewPager2;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("TransactionViewHolder", "Location");
            viewPager2 = itemView.findViewById(R.id.transactionViewPager);
            ViewPagerTransactionAdapter adapter = new ViewPagerTransactionAdapter(fragmentManager, lifecycle, transactions);
            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            viewPager2.setAdapter(adapter);
            viewPager2.setPageTransformer(new MarginPageTransformer(1500));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TRA: OnClick", "Location");
        }
    }
}
