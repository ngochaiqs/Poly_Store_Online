package com.poly_store.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.poly_store.R;
import com.poly_store.databinding.ActivityThemSpBinding;
import com.poly_store.model.MessageModel;
import com.poly_store.model.SanPham;
import com.poly_store.retrofit.ApiBanHang;
import com.poly_store.retrofit.RetrofitClient;
import com.poly_store.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    int loai=0;
    ImageView imgcamera;
    ActivityThemSpBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SanPham sanPhamSua;
    boolean flag = false;
    String mediaPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemSpBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        setContentView(binding.getRoot());
        initView();
        initData();
        ActionToolBar();

        Intent intent = getIntent();
        sanPhamSua = (SanPham) intent.getSerializableExtra("sua");
        if(sanPhamSua == null ){
            // them moi
            flag = false;
        }else {
            // sua
            flag = true;
            binding.btnthemsp.setText("Sửa sản phẩm");
            binding.toolbarThemSP.setTitle("Sửa sản phẩm");
//             show data
            binding.motathemsp.setText(sanPhamSua.getMoTa());
            binding.giaspthemsp.setText(sanPhamSua.getGiaSP()+"");
            binding.tenspthemsp.setText(sanPhamSua.getTenSP());
            binding.hinhanh.setText(sanPhamSua.getHinhAnhSP());
            binding.spinnerLoaiThemsp.setSelection(sanPhamSua.getMaLoai());
        }


    }

    private void ActionToolBar() {
        setSupportActionBar(binding.toolbarThemSP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarThemSP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                loai = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnthemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == false){
                    themsanpham();
                }else {
                    suaSanPham();
                }
            }
        });
        imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ThemSPActivity.this)
                        .crop()	    			//Cắt ảnh
                        .compress(3072)			//Dung lượng ảnh tối đa
                        .maxResultSize(800, 1066)	//Kích thước ảnh sẽ được chọn
                        .start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFile();
        Log.d("LOG","onActivityResult: " +mediaPath);
    }

    private void suaSanPham() {
        String str_ten = binding.tenspthemsp.getText().toString().trim();
        String str_gia = binding.giaspthemsp.getText().toString().trim();
        String str_mota = binding.motathemsp.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
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
                                    Intent intent = new Intent(ThemSPActivity.this, QuanLiActivity.class);
                                    startActivity(intent);
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
        String str_ten = binding.tenspthemsp.getText().toString().trim();
        String str_gia = binding.giaspthemsp.getText().toString().trim();
        String str_mota = binding.motathemsp.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
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
                                    Intent intent = new Intent(ThemSPActivity.this, QuanLiActivity.class);
                                    startActivity(intent);
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

    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null , null, null);
        if (cursor == null){
            result = uri.getPath();
        }else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void uploadMultipleFile() {
        Uri uri = Uri.parse(mediaPath);

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(getPath(uri));

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);

        retrofit2.Call<MessageModel> call = apiBanHang.uploadFile(fileToUpload1);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(retrofit2.Call<MessageModel> call, Response<MessageModel> response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.hinhanh.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
            }


            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.v("log", t.getMessage());

            }
        });
    }



    private void initView(){
        spinner = findViewById(R.id.spinner_loai_themsp);
        imgcamera = findViewById(R.id.imgcamera);
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}
