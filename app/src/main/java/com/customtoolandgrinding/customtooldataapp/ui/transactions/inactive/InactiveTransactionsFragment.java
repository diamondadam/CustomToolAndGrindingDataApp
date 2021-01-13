package com.customtoolandgrinding.customtooldataapp.ui.transactions.inactive;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.adapters.TransactionRecyclerAdapter;

public class InactiveTransactionsFragment extends Fragment {

    private TransactionRecyclerAdapter transactionRecyclerAdapter;

    public InactiveTransactionsFragment() {
        // Required empty public constructor
    }

    public static InactiveTransactionsFragment newInstance() {
        return new InactiveTransactionsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("InactiveFragment", "Constructor");
        InactiveTransactionsViewModel model = new ViewModelProvider(this, new InactiveTransactionViewModelFactory(this.getActivity().getApplication())).get(InactiveTransactionsViewModel.class);
        transactionRecyclerAdapter = new TransactionRecyclerAdapter(this, model.getTransactions());
        model.getTransactions().observe(this, transactions -> {
            Log.d("InactiveFragment", "Data Changed");
            transactionRecyclerAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("InactiveFragment", "On View Create");
        RecyclerView recyclerView = view.findViewById(R.id.past_recycler);
        recyclerView.setAdapter(transactionRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past, container, false);
    }
}