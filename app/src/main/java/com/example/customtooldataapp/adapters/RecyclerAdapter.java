package com.example.customtooldataapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.model.Transaction;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TransactionViewHolder> {
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;
    private Lifecycle lifecycle;
    private LiveData<List<Transaction>> transactions;

    public RecyclerAdapter(Fragment fragment, LiveData<List<Transaction>> transactions){
        Log.d("RecyclerAdapter", "Constructor");
        this.layoutInflater = LayoutInflater.from(fragment.getContext());
        this.fragmentManager = fragment.getChildFragmentManager();
        this.lifecycle = fragment.getLifecycle();
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("RecyclerAdapter", "onCreateViewHolder");
        View transactionView = layoutInflater.inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(transactionView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        if(transactions.getValue() == null){
            Log.d("RecyclerAdapter", "Transactions are null...");
            return 0;
        }else{
            Log.d("RecyclerAdapter", "Size: " +  transactions.getValue().size());
            return transactions.getValue().size();
        }
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ViewPager2 viewPager2;
        private int position;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.d("TransactionViewHolder", "ViewHolder");
            viewPager2 = itemView.findViewById(R.id.transactionViewPager);

            DataFragmentAdapter adapter = new DataFragmentAdapter(fragmentManager, lifecycle, transactions.getValue().get(position));


            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            viewPager2.setAdapter(adapter);
            viewPager2.setPageTransformer(new MarginPageTransformer(1500));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Recycler Adapter", "OnClick");
        }


    }
}
