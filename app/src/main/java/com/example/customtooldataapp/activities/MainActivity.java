package com.example.customtooldataapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.adapters.ViewPagerFragmentAdapter;
import com.example.customtooldataapp.fragments.EmployeeHoursFragment;
import com.example.customtooldataapp.fragments.TransactionsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Fragment employeeHoursFragment;
    private Fragment transactionsFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        employeeHoursFragment = new EmployeeHoursFragment();
        transactionsFragment = TransactionsFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.nested_scroll_view, transactionsFragment).addToBackStack("Transactions").commit();
        fragmentManager.executePendingTransactions();

        setContentView(R.layout.activity_main);
        //Toolbar Initialization
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        int fragmentsInStack = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("Back Stack", fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName());
        if (fragmentsInStack > 1) { // If we have more than one fragment, pop back stack
            getSupportFragmentManager().popBackStack();
        } else if (fragmentsInStack == 1) { // Finish activity, if only one fragment left, to prevent leaving empty screen
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("onOptionsItemSelected", "Here");
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("Case", "R.id.action_settings");
                return true;
            case R.id.employee_hours:
                Log.d("Case", "R.id.employee_hours");
                replaceFragment(employeeHoursFragment, "Employee Hours");
                //fragmentManager.beginTransaction().replace(R.id.nested_scroll_view, employeeHoursFragment, "Employee Hours").addToBackStack("Employee Hours").commit();
                return true;

            default:
                Log.d("Case", "default");

        }
        return super.onOptionsItemSelected(item);
    }
    public void replaceFragment(Fragment fragment, String tag) {
        //Get current fragment placed in container
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nested_scroll_view);

        //Prevent adding same fragment on top
        if (currentFragment.getClass() == fragment.getClass()) {
            return;
        }

        //If fragment is already on stack, we can pop back stack to prevent stack infinite growth
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        //Otherwise, just replace fragment
        fragmentManager
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.nested_scroll_view, fragment, tag)
                .commit();
    }

}