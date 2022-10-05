package com.poly_store.retrofit;

import com.poly_store.model.LoaiSPModel;
import com.poly_store.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();

    @GET("getsanpham.php")
    Observable<SanPhamModel> getSanPham();
}
