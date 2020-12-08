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
import com.example.customtooldataapp.adapters.RecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    public PastFragment() {
        // Required empty public constructor
        Log.d("PastFragment", "Constructor");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PastFragment newInstance() {
        PastFragment fragment = new PastFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("PastFragment", "onCreate");
        super.onCreate(savedInstanceState);
        PastViewModel model = new ViewModelProvider(this).get(PastViewModel.class);
        Log.d("CurrentFragment", "...Creating Adapter");
        recyclerAdapter = new RecyclerAdapter(this, model.getTransactions());
        model.getTransactions().observe(this, transactions -> {
            Log.d("PastFragment", "...Updating Adapter");
            recyclerAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = getView().findViewById(R.id.past_recycler);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        super.onViewCreated(view, savedInstanceState);
    }

}