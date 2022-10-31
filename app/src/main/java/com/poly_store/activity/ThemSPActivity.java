package com.poly_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.poly_store.R;
import com.poly_store.databinding.ActivityThemSpBinding;
import com.poly_store.model.SanPham;
import com.poly_store.retrofit.ApiBanHang;
import com.poly_store.retrofit.RetrofitClient;
import com.poly_store.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    AppCompatButton btnThemSP;
    EditText edtTenSP, edtGiaSP, edtHinhAnhSP, edtMoTaSP;
    int loai=0;
    ActivityThemSpBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SanPham sanPhamSua;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemSpBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);


        setContentView(binding.getRoot());
        Intent intent = getIntent();
        sanPhamSua = (SanPham) intent.getSerializableExtra("sua");
        if(sanPhamSua == null ){
            // them moi
            flag = false;
        }else {
            // sua
            flag = true;
            binding.btnthemsp.setText("Sửa sản phẩm");
            // show data
            binding.motathemsp.setText(sanPhamSua.getMoTa());
            binding.giaspthemsp.setText(sanPhamSua.getGiaSP()+"");
            binding.tenspthemsp.setText(sanPhamSua.getTenSP());
            binding.hinhanhthemsp.setText(sanPhamSua.getHinhAnhSP());
            binding.spinnerLoai.setSelection(sanPhamSua.getMaLoai());
        }


        setContentView(R.layout.activity_them_sp);
        initView();
        initData();
    }
    private void initData(){
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn sản phẩm");
        stringList.add("Áo khoác");
        stringList.add("Áo thun");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        binding.btnthemsp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                themsanpham();
//            }
//        });
        btnThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == false){
                    themsanpham();
                }else {
                    suaSanPham();
                }
            }
        });

    }

    private void suaSanPham() {
        String str_ten = edtTenSP.getText().toString().trim();
        String str_gia = edtGiaSP.getText().toString().trim();
        String str_mota = edtMoTaSP.getText().toString().trim();
        String str_hinhanh = edtHinhAnhSP.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || loai == 0 ){
            Toast.makeText(getApplicationContext(), " Vui lòng nhập đủ thông tin", Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiBanHang.updatesp(str_ten,str_gia,str_hinhanh,str_mota,loai,sanPhamSua.getMaSP())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    private void themsanpham() {
//        String str_ten = binding.tenspthemsp.getText().toString().trim();
//        String str_gia = binding.giaspthemsp.getText().toString().trim();
//        String str_mota = binding.motathemsp.getText().toString().trim();
//        String str_hinhanh = binding.hinhanhthemsp.getText().toString().trim();
        String str_ten = edtTenSP.getText().toString().trim();
        String str_gia = edtGiaSP.getText().toString().trim();
        String str_mota = edtMoTaSP.getText().toString().trim();
        String str_hinhanh = edtHinhAnhSP.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || loai == 0 ){
            Toast.makeText(getApplicationContext(), " Vui lòng nhập đủ thông tin", Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiBanHang.themSP(str_ten,str_gia,str_hinhanh,str_mota,(loai))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    private void initView(){
        spinner = findViewById(R.id.spinner_loai);
        btnThemSP = findViewById(R.id.btnthemsp);
        edtTenSP = findViewById(R.id.tenspthemsp);
        edtGiaSP = findViewById(R.id.giaspthemsp);
        edtHinhAnhSP = findViewById(R.id.hinhanhthemsp);
        edtMoTaSP = findViewById(R.id.motathemsp);
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}