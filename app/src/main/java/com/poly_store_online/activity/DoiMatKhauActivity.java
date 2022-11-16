package com.poly_store_online.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.poly_store_online.R;
import com.poly_store_online.retrofit.ApiBanHang;
import com.poly_store_online.retrofit.RetrofitClient;
import com.poly_store_online.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DoiMatKhauActivity extends AppCompatActivity {
    EditText edtMKCu, edtMKMoi, edtReMKMoi, edtEmail;
    AppCompatButton btnDoiMatKhau;
    Toolbar toolbar;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        AnhXa();
        initControl();
        ActionToolBar();
    }

    private void initControl() {
        edtEmail.setText(Paper.book().read("email"));
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doiMatKhau();
            }
        });
        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        edtMKCu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        edtMKMoi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        edtReMKMoi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void doiMatKhau() {
        String str_MatKhauCu = edtMKCu.getText().toString().trim();
        String str_email = edtEmail.getText().toString().trim();
        String str_matKhauMoi = edtMKMoi.getText().toString().trim();
        String str_reMatKhauMoi = edtReMKMoi.getText().toString().trim();

        if (TextUtils.isEmpty(str_MatKhauCu)) {
            Toast.makeText(getApplicationContext(), "Chưa mật khẩu cũ!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Chưa nhập Email!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_matKhauMoi)) {
            Toast.makeText(getApplicationContext(), "Chưa nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_reMatKhauMoi)) {
            Toast.makeText(getApplicationContext(), "Chưa xác nhận mật khẩu mới!", Toast.LENGTH_SHORT).show();
        }else{
            if (str_matKhauMoi.equals(str_reMatKhauMoi)){
                postData(str_email,str_matKhauMoi);
            }else{
                Toast.makeText(getApplicationContext(), "Mật khẩu mới chưa khớp!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void  postData(String str_email, String str_matKhauMoi){
        //post data
        compositeDisposable.add(apiBanHang.doiMatKhau(str_email, str_matKhauMoi)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        nguoiDungModel -> {
                            if (nguoiDungModel.isSuccess()){
                                Utils.nguoidung_current.setMatKhau(str_matKhauMoi);
                                Toast.makeText(getApplicationContext(),nguoiDungModel.getMessage(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void AnhXa() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        edtMKCu = findViewById(R.id.edtMatKhauCu);
        edtMKMoi = findViewById(R.id.edtMatKhauMoi);
        edtReMKMoi = findViewById(R.id.edtreMatKhauMoi);
        edtEmail = findViewById(R.id.edtEmailDoiMK);
        btnDoiMatKhau = findViewById(R.id.btnDoiMK);
        toolbar = findViewById(R.id.toolbarDoiMK);
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();;
    }
}