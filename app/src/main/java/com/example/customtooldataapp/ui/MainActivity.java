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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.services.WebService;
import com.example.customtooldataapp.source.TransactionRepository;
import com.example.customtooldataapp.ui.transactions.TransactionsFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private static final String EMP_ID = "0163";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, WebService.class);

        this.startService(i);

        //Toolbar Initialization
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addTransaction = findViewById(R.id.add_button);

        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getParent(), R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStartFragment(EMP_ID));
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.transactionsFragment, R.id.employeeHoursFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            case R.id.home:
                Log.d("onNavigationItemSelect", "R.id.home");
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.employee_hours:
                Log.d("onNavigationItemSelect", "R.id.employee_hours");
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToEmployeeHoursFragment());
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.operation_start:
                Log.d("onNavigationItemSelect", "R.id.operation_start");
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStartFragment(EMP_ID));
                drawerLayout.closeDrawer(GravityCompat.START);
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
                return true;
            case R.id.action_sync:
                Log.d("Case", "R.id.action_sync");
                TransactionRepository.getInstance(getApplication()).syncDatabases();
                return true;
            default:
                Log.d("Case", "default");
                return false;
        }
    }

}