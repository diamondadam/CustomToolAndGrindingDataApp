package com.example.customtooldataapp.ui.data;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.model.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuantitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuantitiesFragment extends Fragment {



    private String orderQty;
    private String inProductionQty;

    private String makeQty;
    private String operationQtyCompleted;

    private String pickQty;
    private String shippedQty;



    public QuantitiesFragment() {
        // Required empty public constructor
    }

    public static QuantitiesFragment newInstance(Transaction transaction) {
        QuantitiesFragment fragment = new QuantitiesFragment();
        Bundle args = new Bundle();

        args.putString("Order Qty", String.valueOf(transaction.getJob().getOrderQty()));
        args.putString("In Production Qty", String.valueOf(transaction.getJob().getInProductionQty()));

        args.putString("Make Qty", String.valueOf(transaction.getJob().getMakeQty()));
        args.putString("Operation Qty Completed", String.valueOf(transaction.getOperation().getQtyCompleted()));

        args.putString("Pick Qty", String.valueOf(transaction.getJob().getPickQty()));
        args.putString("Shipped Qty", String.valueOf(transaction.getJob().getShippedQty()));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.orderQty = getArguments().getString("Order Qty");
            this.inProductionQty = getArguments().getString("In Production Qty");

            this.makeQty = getArguments().getString("Make Qty");
            this.operationQtyCompleted = getArguments().getString("Operation Qty Completed");

            this.pickQty = getArguments().getString("Pick Qty");
            this.shippedQty = getArguments().getString("Shipped Qty");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_quantities, container, false);

        TextView orderQtyWidget = layout.findViewById(R.id.order_qty);
        TextView inProductionQtyWidget = layout.findViewById(R.id.in_production_qty);
        TextView makeQtyWidget = layout.findViewById(R.id.make_qty);
        TextView operationQtyCompletedWidget = layout.findViewById(R.id.qty_completed);
        TextView pickQtyWidget = layout.findViewById(R.id.pick_qty);
        TextView shippedQtyWidget = layout.findViewById(R.id.shipped_qty);

        orderQtyWidget.setText(String.format("Order Qty: %s", orderQty));
        inProductionQtyWidget.setText(String.format("In Production Qty: %s", inProductionQty));

        makeQtyWidget.setText(String.format("Make Qty: %s", makeQty));
        operationQtyCompletedWidget.setText(String.format("Completed Qty: %s", operationQtyCompleted));

        pickQtyWidget.setText(String.format("Pick Qty: %s", pickQty));
        shippedQtyWidget.setText(String.format("Shipped Qty: %s", shippedQty));

        return layout;

    }
}