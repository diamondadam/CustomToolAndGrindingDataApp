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
import com.example.customtooldataapp.models.Transaction;

import java.util.List;
import java.util.Objects;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder> {
    private final LayoutInflater layoutInflater;
    private final FragmentManager fragmentManager;
    private final Lifecycle lifecycle;
    private final LiveData<List<Transaction>> transactions;

    public TransactionRecyclerAdapter(Fragment fragment, LiveData<List<Transaction>> transactions){
        Log.d("TransactionRecycler", "Constructor");
        this.layoutInflater = LayoutInflater.from(fragment.getContext());
        this.fragmentManager = fragment.getChildFragmentManager();
        this.lifecycle = fragment.getLifecycle();
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TransactionRecycler", "onCreateViewHolder");
        View transactionView = layoutInflater.inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(transactionView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.position = position;
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        if(transactions.getValue() == null){
            Log.d("TransactionRecycler", "Transactions are null...");
            return 0;
        }else{
            Log.d("TransactionRecycler", "Size: " +  transactions.getValue().size());
            return transactions.getValue().size();
        }
    }



    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ViewPager2 viewPager2;
        private int position;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPager2 = itemView.findViewById(R.id.transactionViewPager);
            Log.d("TransactionViewHolder", "Constructor...");
        }
        public void onBind(){
            DataPagerAdapter adapter = new DataPagerAdapter(fragmentManager, lifecycle, Objects.requireNonNull(transactions.getValue()).get(position));
            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            viewPager2.setAdapter(adapter);
            viewPager2.setPageTransformer(new MarginPageTransformer(1500));
            Log.d("TransactionViewHolder", "onBind()...");
        }

        @Override
        public void onClick(View v) {
            Log.d("TransactionViewHolder", "OnClick");
        }
    }
}
