package com.example.customtooldataapp.ui.transactions.past;

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

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.adapters.CurrentRecyclerAdapter;
import com.example.customtooldataapp.adapters.PastRecyclerAdapter;

public class PastFragment extends Fragment {

    private PastRecyclerAdapter recyclerAdapter;

    public PastFragment() {
        // Required empty public constructor
    }

    public static PastFragment newInstance() {
        return new PastFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Past Fragment", "Constructor");
        PastViewModel model = new ViewModelProvider(this, new PastViewModelFactory(this.getActivity().getApplication())).get(PastViewModel.class);
        recyclerAdapter = new PastRecyclerAdapter(this, model.getTransactions());
        model.getTransactions().observe(this, transactions -> {
            Log.d("Past Fragment", "Data Changed");
            recyclerAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("Past Fragment", "On View Create");
        RecyclerView recyclerView = view.findViewById(R.id.past_recycler);
        recyclerView.setAdapter(recyclerAdapter);
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