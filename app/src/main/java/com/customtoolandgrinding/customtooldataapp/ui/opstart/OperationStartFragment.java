package com.customtoolandgrinding.customtooldataapp.ui.opstart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
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

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import com.customtoolandgrinding.customtooldataapp.R;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.vision.L.TAG;


public class OperationStartFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int VIBRATE_PERMISSION_CODE = 200;

    private String employeeID;
    private SurfaceView surfaceview;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private Vibrator vib;
    private int i = 0;
    private String prevBarcode = "";
    private boolean setupTimer = false;
    private View view;

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
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
            setupTimer = sharedPreferences.getBoolean("Auto Setup", false);
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //mPermissionResult.launch(Manifest.permission.CAMERA);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_operation_start, container, false);
        displayBarcodeScanner();
        return view;
    }

    ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                        displayBarcodeScanner();
                    } else {
                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(OperationStartFragmentDirections.actionOperationStartFragmentToTransactionsFragment());
                    }
                }
            });


    private void displayBarcodeScanner() {
        surfaceview = view.findViewById(R.id.camera_surface_view);
        textView = view.findViewById(R.id.opId);
        Button startButton = view.findViewById(R.id.op_start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opCode = String.valueOf(textView.getText());
                if (opCode != null && opCode.length() == 6 && opCode.matches("[0-9]+")) {
                    startWebView(view, opCode);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(OperationStartFragmentDirections.actionOperationStartFragmentToTransactionsFragment());
                } else {
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
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionResult.launch(Manifest.permission.CAMERA);
                    }
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
            private final Context context = getContext();

            @Override
            public void release() {}

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

        OpStartWebViewClient opStartWebViewClient = new OpStartWebViewClient(employeeID, operationId, getActivity().getApplication());
        webView.setWebViewClient(opStartWebViewClient);

    }
}