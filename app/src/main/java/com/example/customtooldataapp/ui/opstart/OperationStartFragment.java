package com.example.customtooldataapp.ui.opstart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import com.example.customtooldataapp.R;
import com.example.customtooldataapp.ui.opstop.OperationStopFragment;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperationStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperationStartFragment extends Fragment {
    private static final String EMP_ID = "Employee Id";
    private static final String OPERATION_ID = "Operation Id";

    private String employeeId;
    private String operationId;

    private SurfaceView surfaceview;

    private CameraSource cameraSource;
    private TextView textView;
    private Button submitButton;
    private BarcodeDetector barcodeDetector;
    private Vibrator vib;

    private int i = 0;
    private String prevBarcode = "";

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
            employeeId = OperationStartFragmentArgs.fromBundle(getArguments()).getEmployeeId();
            Log.d("OpStartFragment", "onCreate()");
            Log.d("OpStartFragment", employeeId);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_operation_start, container, false);
        WebView webView = view.findViewById(R.id.operation_start_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("http://10.10.8.4/dcmobile2/");
        OpStartWebViewClient opStartWebViewClient = new OpStartWebViewClient(employeeId, operationId);
        webView.setWebViewClient(opStartWebViewClient);
        return view;
    }
/*
    private void displayBarcode(){

        surfaceview = (SurfaceView)findViewById(R.id.surfaceView2);
        textView = (TextView) findViewById(R.id.opId);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats((Barcode.CODE_128)).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();

        surfaceview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(holder);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            private final Context context = getContext();

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                SparseArray<Barcode> barCodes = detections.getDetectedItems();
                if(barCodes.size() > 0) {
                    if (i == 0) {
                        textView.setText(barCodes.valueAt(0).displayValue);
                        vib = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vib != null) {
                            vib.vibrate(500);
                        }
                        i++;
                        prevBarcode = barCodes.valueAt(0).displayValue;
                    } else if (!(prevBarcode.equals(barCodes.valueAt(0).displayValue))) {
                        //Checks to see if current scanned code is equal to the last.
                        vib = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vib != null) {
                            vib.vibrate(500);
                        }
                        i++;
                    }
                }
            }
        });
    }
*/


}