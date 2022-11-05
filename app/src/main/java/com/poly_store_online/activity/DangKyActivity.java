package com.poly_store_online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.poly_store_online.R;
import com.poly_store_online.retrofit.ApiBanHang;
import com.poly_store_online.retrofit.RetrofitClient;
import com.poly_store_online.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email, matKhau, reMatKhau, sdt, tenND;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    FirebaseAuth firebaseAuth;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControll();
    }
    private void initControll(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        String str_tenND = tenND.getText().toString().trim();
        String str_email = email.getText().toString().trim();
        String str_matKhau = matKhau.getText().toString().trim();
        String str_reMatKhau = reMatKhau.getText().toString().trim();
        String str_sdt = sdt.getText().toString().trim();

        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Ban chua nhap Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_matKhau)) {
            Toast.makeText(getApplicationContext(), "Ban chua nhap mat khau", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_reMatKhau)) {
            Toast.makeText(getApplicationContext(), "Ban chua nhap lai mat khau", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_sdt)) {
            Toast.makeText(getApplicationContext(), "Ban chua nhap sdt", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_tenND)) {
            Toast.makeText(getApplicationContext(), "Ban chua nhap ten nguoi dung", Toast.LENGTH_SHORT).show();

        }else{
            if (str_matKhau.equals(str_reMatKhau)){
                //post data
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(str_email, str_matKhau)
                        .addOnCompleteListener(DangKyActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null){
                                        postData(str_tenND, str_email, str_matKhau, str_sdt, user.getUid());
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Email da ton tai hoac khong thanh cong", Toast.LENGTH_SHORT).show();
                                        postData(str_email, str_matKhau, str_tenND, str_sdt, user.getUid());
                                    }
                                }
                            }
                        });
            }else{
                Toast.makeText(getApplicationContext(), "Mat khau chua khop", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postData(String str_email, String str_matKhau, String str_tenND, String str_sdt, String uid){
        //post Data
        compositeDisposable.add(apiBanHang.dangKy(str_email, str_matKhau, str_tenND, str_sdt, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        nguoiDungModel -> {
                            if (nguoiDungModel.isSuccess()){
                                Utils.nguoidung_current.setEmail(str_email);
                                Utils.nguoidung_current.setMatKhau(str_matKhau);
                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), nguoiDungModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void initView(){
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        matKhau = findViewById(R.id.matKhau);
        reMatKhau = findViewById(R.id.reMatKhau);
        button = findViewById(R.id.btndangky);
        sdt = findViewById(R.id.sdt);
        tenND = findViewById(R.id.tenND);
    }

    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();;
    }
}