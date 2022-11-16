package com.poly_store_online.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poly_store_online.R;
import com.poly_store_online.adapter.AoSoMiAdapter;
import com.poly_store_online.model.SanPham;
import com.poly_store_online.retrofit.ApiBanHang;
import com.poly_store_online.retrofit.RetrofitClient;
import com.poly_store_online.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
public class AoSoMiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    AoSoMiAdapter adapterASM;
    List<SanPham> sanPhamList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ao_so_mi);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("maLoai", 3);

        AnhXa();
        ActionToolBar();
        getData(page);
//        addEventLoad();

    }

    private void addEventLoad(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamList.size()-1){
                        isLoading = true;
                        loadMore();
                    }

                }
            }
        });
    }

    private void loadMore(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                sanPhamList.add(null);
                adapterASM.notifyItemInserted(sanPhamList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //recover null
                sanPhamList.remove(sanPhamList.size()-1);
                adapterASM.notifyItemRemoved(sanPhamList.size());
                page = page+1;
                getData(page);
                adapterASM.notifyDataSetChanged();
                isLoading =false;
            }
        },2000);
    }

    private void getData(int page){
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()){
                                if (adapterASM == null){
                                    sanPhamList = sanPhamModel.getResult();
                                    adapterASM = new AoSoMiAdapter(getApplicationContext(), sanPhamList);
                                    recyclerView.setAdapter(adapterASM);
                                }else {
                                    int vitri = sanPhamList.size()-1;
                                    int soluongadd = sanPhamModel.getResult().size();
                                    for (int i = 0; i < soluongadd; i++){
                                        sanPhamList.add(sanPhamModel.getResult().get(i));
                                    }
                                    adapterASM.notifyItemRangeChanged(vitri, soluongadd);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu", Toast.LENGTH_SHORT).show();
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với Server", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void ActionToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarAoSoMi);
        recyclerView = findViewById(R.id.recycleview_asm);
        linearLayoutManager = new LinearLayoutManager(this,linearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
