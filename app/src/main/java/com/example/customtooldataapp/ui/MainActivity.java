package com.example.customtooldataapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.models.ClockInAndOut;
import com.example.customtooldataapp.services.GetStatus;
import com.example.customtooldataapp.source.TransactionRepository;
import com.example.customtooldataapp.ui.transactions.TransactionsFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private String empID;
    public static final ExecutorService executorService =
            Executors.newFixedThreadPool(4);

    private MenuItem refreshItem;
    private TextView navigationHeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("Employee Identification", MODE_PRIVATE);
        empID = sharedPreferences.getString("ID", "");
        Intent i = new Intent(this, GetStatus.class);
        this.startService(i);

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

        setTitle("Employee: " + empID);

        FloatingActionButton addTransaction = findViewById(R.id.add_button);
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStartFragment(empID));
                addTransaction.setVisibility(View.GONE);
            }
        });

        timesObserver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        refreshItem = menu.findItem(R.id.action_sync);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("onNavigationItemSelect", "Item Selected...");
        int id = item.getItemId();
        switch (id) {
            case R.id.employee_hours:
                Log.d("onNavigationItemSelect", "R.id.employee_hours");
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToYourHoursFragment());
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.operation_start:
                Log.d("onNavigationItemSelect", "R.id.operation_start");
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStartFragment(empID));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.clock_in_and_out:
                Log.d("onNavigationItemSelect", "R.id.clock_in_and_out");
                break;
            default:
                Log.d("onNavigationItemSelect", "default");
                break;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("onOptionsItemSelected", "Here");
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("Case", "R.id.action_settings");
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToSettingsFragment2());
                return true;
            case R.id.action_sync:
                Log.d("Case", "R.id.action_sync");
                startSync();
                return true;
            default:
                Log.d("Case", "default");
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

    public void timesObserver(){
        TransactionRepository.getInstance(getApplication()).getTimes().observe(this, times -> {
            Log.d("timesObserver", "...Updating times");
            if(times.size() > 0){
                String time = times.get(times.size()-1).getDate();
                if(time != null){
                    navigationHeaderTitle = findViewById(R.id.clock_in_and_out_text);
                    Log.d("Times Observer", time);
                    navigationHeaderTitle.setText(time);
                    for(ClockInAndOut c : times){
                        Log.d("TimesObserver", c.getDate());
                    }
                }
            }
        });
    }

    public void startSync() {
        Log.d("startSync", "Starting...");
        //Start sync animation
        syncAnimationStart();
        executorService.execute(() -> {
            TransactionRepository.getInstance(getApplication()).updateTransactions();
            this.runOnUiThread(new Runnable(){
               @Override
               public void run() {
                   syncAnimationStop();
               }
            });
        });
    }

}