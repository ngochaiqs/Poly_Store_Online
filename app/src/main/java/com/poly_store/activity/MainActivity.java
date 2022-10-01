package com.poly_store.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.poly_store.R;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewMain;
    NavigationView navigationView;
    ListView lvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreate();
    }

    public void onCreate() {
//        toolbar = findViewById(R.id.toolbarMain);
        viewFlipper = findViewById(R.id.viewLipper);
        recyclerViewMain = findViewById(R.id.recyclerviewMain);
        navigationView = findViewById(R.id.navigationView);
        lvMain = findViewById(R.id.lvMain);
    }
}