package com.customtoolandgrinding.customtooldataapp.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;
import com.customtoolandgrinding.customtooldataapp.services.PunchInService;
import com.customtoolandgrinding.customtooldataapp.services.PunchOutService;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.TransactionsFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.android.gms.vision.L.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String CHANNEL_ID = "CustomTool0001";
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private String empID;
    private TransactionRepository transactionRepository;
    public static final ExecutorService executorService =
            Executors.newFixedThreadPool(4);

    private MenuItem refreshItem;
    private TextView navigationHeaderTitle;
    private FloatingActionButton addTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionRepository = TransactionRepository.getInstance(getApplication());
        transactionRepository.syncDatabasesThreaded();
        initNotifications();

        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("Employee Identification", MODE_PRIVATE);
        empID = sharedPreferences.getString("ID", "");

        //Toolbar Initialization
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.transactionsFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navOnChange(navController);
        setTitle("Employee: " + empID);

        addTransaction = findViewById(R.id.add_button);
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCameraPermission();
            }
        });

        timesObserver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        refreshItem = menu.findItem(R.id.action_sync);
        //Has to wait until refreshItem is initialized before running start sync the first time.
        startSync();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            /*
            case R.id.employee_hours:

                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToYourHoursFragment());
                drawerLayout.closeDrawer(GravityCompat.START);
                break;*/
            case R.id.operation_start:
                getCameraPermission();
                drawerLayout.closeDrawer(GravityCompat.START);
                addTransaction.hide();
                break;

            case R.id.clock_in_and_out:
                executorService.execute(() -> {
                    if (transactionRepository.isPunchedIn()) {
                        Intent i = new Intent(this, PunchOutService.class);
                        this.startService(i);
                    } else {
                        Intent i = new Intent(this, PunchInService.class);
                        this.startService(i);
                    }
                });
                break;

            default:
                break;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToSettingsFragment2());
                return true;
            case R.id.action_sync:
                startSync();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void syncAnimationStart() {
        /* Attach a rotating ImageView to the refresh item as an ActionView */
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.action_bar_sync, null);

        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.spin);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        refreshItem.setActionView(iv);
    }

    public void syncAnimationStop() {
            refreshItem.getActionView().clearAnimation();
            refreshItem.setActionView(null);
    }

    public void timesObserver() {
        transactionRepository.getPunchHoles().observe(this, punchHoles -> {
            if (punchHoles.size() > 0) {
                String time;
                if (punchHoles.get(punchHoles.size() - 1).getPrefix()) {
                    time = "Clock In Time: " + punchHoles.get(punchHoles.size() - 1).getDate();
                } else {
                    time = "Clock Out Time: " + punchHoles.get(punchHoles.size() - 1).getDate();
                }

                if (time != null) {
                    try {
                        navigationHeaderTitle = findViewById(R.id.clock_in_and_out_text);
                        navigationHeaderTitle.setText(time);
                    } catch (Exception e) {
                        //Do nothing things are still loading
                    }
                }
            }
        });
    }

    public void initNotifications() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void startSync() {
        try {
            //Start sync animation
            syncAnimationStart();
            executorService.execute(() -> {
                transactionRepository.syncDatabases();
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syncAnimationStop();
                    }
                });
            });
        } catch (NullPointerException e) {
            // Do nothing things are messing up
        }
    }

    public void navOnChange(NavController navController){
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId() == R.id.operationStartFragment) {
                addTransaction.hide();
            } else if(destination.getId() == R.id.transactionsFragment){
                //startSync();
                if(addTransaction != null){addTransaction.show();}
            }
        });
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStartFragment(empID));
                    Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                } else {

                    Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                }
            });

    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStartFragment(empID));
        }  else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
}