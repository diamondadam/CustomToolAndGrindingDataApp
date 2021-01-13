package com.customtoolandgrinding.customtooldataapp.ui.opstart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Vibrator;
import android.util.Log;
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

import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;
import com.customtoolandgrinding.customtooldataapp.ui.MainActivity;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.TransactionsFragment;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.TransactionsFragmentDirections;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import com.customtoolandgrinding.customtooldataapp.R;

import java.io.IOException;


public class OperationStartFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int VIBRATE_PERMISSION_CODE = 200;

    private String employeeId;
    private SurfaceView surfaceview;
    private CameraSource cameraSource;
    private TextView textView;
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
        View view = inflater.inflate(R.layout.fragment_operation_start, container, false);
        displayBarcodeScanner(view);
        return view;
    }


    // Function to check and request permission
    public void checkPermission(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, requestCode);
        } else {
            Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                displayBarcodeScanner(getView());
            } else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == VIBRATE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayBarcodeScanner(View view){

        surfaceview = view.findViewById(R.id.camera_surface_view);
        textView = view.findViewById(R.id.opId);
        Button startButton = view.findViewById(R.id.op_start_button);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String opCode = String.valueOf(textView.getText());
                if(opCode != null && opCode.length() == 6 && opCode.matches("[0-9]+")){
                    startWebView(view, opCode);
                    TransactionRepository.getInstance(getActivity().getApplication()).updateTransactions();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(OperationStartFragmentDirections.actionOperationStartFragmentToTransactionsFragment());

                }else{
                    Toast.makeText(getContext(), "Wrong format!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats((Barcode.CODE_128)).build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();

        surfaceview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    }else{
                        cameraSource.start(holder);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

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
                SparseArray<Barcode> barCodes= detections.getDetectedItems();
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

    private void startWebView(View view, String operationId){
        WebView webView = view.findViewById(R.id.operation_start_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://10.10.8.4/dcmobile2/");
        OpStartWebViewClient opStartWebViewClient = new OpStartWebViewClient(employeeId, operationId);
        webView.setWebViewClient(opStartWebViewClient);

    }
}