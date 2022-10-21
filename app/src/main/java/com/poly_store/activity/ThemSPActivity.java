package com.poly_store.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.poly_store.R;

import java.util.ArrayList;
import java.util.List;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_spactivity);
        initView();
        initData();
    }
    private void initData(){
        List<String> stringList = new ArrayList<>();
        stringList.add("Áo khoác");
        stringList.add("Áo thun");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);

    }
    private void initView(){
        spinner = findViewById(R.id.spinner_loai);
    }
}