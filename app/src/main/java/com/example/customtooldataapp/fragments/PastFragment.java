package com.example.customtooldataapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customtooldataapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastFragment extends Fragment {

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
}