package com.example.customtooldataapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.adapters.TransactionRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransactionRecyclerAdapter transactionRecyclerAdapter;

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
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Assign arguments
        }
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
        ArrayList<String> jobNames = new ArrayList<String>(
                Arrays.asList("11111", "44444", "55669", "Testing Past Name")
        );
        transactionRecyclerAdapter = new TransactionRecyclerAdapter(this, jobNames);
        recyclerView.setAdapter(transactionRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        super.onViewCreated(view, savedInstanceState);
    }

}