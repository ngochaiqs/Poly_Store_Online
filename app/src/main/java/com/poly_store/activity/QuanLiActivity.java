package com.poly_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.poly_store.R;

import soup.neumorphism.NeumorphCardView;

public class QuanLiActivity extends AppCompatActivity {
    NeumorphCardView themp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li);
        initView();
        initControl();

    }
    private void initControl(){
        themp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThemSPActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initView(){
        themp = findViewById(R.id.neu_themsanpham);
    }
}