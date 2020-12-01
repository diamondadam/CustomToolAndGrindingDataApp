package com.example.customtooldataapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.adapters.ViewPagerFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout.Tab currentTab;
    private TabLayout.Tab pastTab;
    private TabLayout tabLayout;
    private final String[] tabTitles = {"Current", "Past"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar Initialization
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ViewPager2 Initialization
        ViewPager2 viewPager2 = findViewById(R.id.viewpager2);
        viewPager2.setUserInputEnabled(false);
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new MarginPageTransformer(1500));
        //currentTab = findViewById(R.id.current_tab);
        TabLayout tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("onOptionsItemSelected", "Here");
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("Case", "R.id.action_settings");
                return true;
            default:
                Log.d("Case", "default");

        }
        return super.onOptionsItemSelected(item);
    }
}