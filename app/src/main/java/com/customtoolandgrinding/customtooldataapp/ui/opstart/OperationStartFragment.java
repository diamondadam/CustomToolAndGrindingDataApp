package com.customtoolandgrinding.customtooldataapp.ui.opstart;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import com.customtoolandgrinding.customtooldataapp.R;

import java.io.IOException;

public class OperationStartFragment extends Fragment {
    private String employeeID;
    private Context context;
    private Activity activity;
    private int i = 0;
    private String prevBarcode = "";
    private Vibrator vib;

    public OperationStartFragment() {
        // Required empty public constructor
    }

    public static OperationStartFragment newInstance() {
        return new OperationStartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employeeID = OperationStartFragmentArgs.fromBundle(getArguments()).getEmployeeId();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = getActivity();
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_operation_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayBarcodeScanner(view);
    }



    private void displayBarcodeScanner(View view) {
        SurfaceView surfaceview = view.findViewById(R.id.camera_surface_view);
        TextView textView = view.findViewById(R.id.opId);
        Button startButton = view.findViewById(R.id.op_start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opCode = String.valueOf(textView.getText());
                if (opCode != null && opCode.length() == 6 && opCode.matches("[0-9]+")) {
                    startWebView(view, opCode);
                    Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(OperationStartFragmentDirections.actionOperationStartFragmentToTransactionsFragment());
                } else {
                    Toast.makeText(context, "Wrong format!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats((Barcode.CODE_128)).build();

        CameraSource cameraSource = new CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();

        surfaceview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(holder);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barCodes = detections.getDetectedItems();
                if(barCodes.size() > 0) {
                    if (i == 0) {
                        textView.setText(barCodes.valueAt(0).displayValue);
                        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vib != null) {
                            vib.vibrate(500);
                        }
                        i++;
                        prevBarcode = barCodes.valueAt(0).displayValue;
                    } else if (!(prevBarcode.equals(barCodes.valueAt(0).displayValue))) {
                        //Checks to see if current scanned code is equal to the last.
                        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vib != null) {
                            vib.vibrate(500);
                        }
                        i++;
                    }
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void startWebView(View view, String operationId){
        WebView webView = view.findViewById(R.id.operation_start_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://10.10.8.4/dcmobile2/");

        OpStartWebViewClient opStartWebViewClient = new OpStartWebViewClient(employeeID, operationId, activity.getApplication());
        webView.setWebViewClient(opStartWebViewClient);
    }
}