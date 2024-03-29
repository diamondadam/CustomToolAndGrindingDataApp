package com.customtoolandgrinding.customtooldataapp.ui.transactions.active;

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
import com.customtoolandgrinding.customtooldataapp.models.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActiveTransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActiveTransactionsFragment extends Fragment {

    private TransactionRecyclerAdapter transactionRecyclerAdapter;
    private String time;

    public ActiveTransactionsFragment() {
        // Required empty public constructor
    }

    public static ActiveTransactionsFragment newInstance() {
        return new ActiveTransactionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActiveTransactionsViewModel model = new ViewModelProvider(this, new ActiveTransactionsViewModelFactory(this.getActivity().getApplication())).get(ActiveTransactionsViewModel.class);

        transactionRecyclerAdapter = new TransactionRecyclerAdapter(this, model.getTransactions());
        model.getTransactions().observe(this, transactions -> {
            transactionRecyclerAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = getView().findViewById(R.id.current_recycler);
        recyclerView.setAdapter(transactionRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false);
    }
}