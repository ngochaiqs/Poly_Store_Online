package com.poly_store.retrofit;

import com.poly_store.model.LoaiSPModel;
import com.poly_store.model.NguoiDungModel;
import com.poly_store.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();

    @GET("getsanpham.php")
    Observable<SanPhamModel> getSanPham();

    @POST("chitietdonhang.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getSanPham(
        @Field("page") int page,
        @Field("maLoai") int maLoai
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<NguoiDungModel> dangKy(
            @Field("tenND") String tenND,
            @Field("email") String email,
            @Field("matKhau") String matKhau,
            @Field("SDT") String SDT
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<NguoiDungModel> dangNhap(
            @Field("email") String email,
            @Field("matKhau") String matKhau
    );

    @POST("quenmatkhau.php")
    @FormUrlEncoded
    Observable<NguoiDungModel> quenMK(
            @Field("email") String email

    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<NguoiDungModel> datHang(
            @Field("email") String email,
            @Field("SDT") String SDT,
            @Field("tongTien") String tongTien,
            @Field("maND") int maND,
            @Field("diaChi") String diaChi,
            @Field("soLuong") int soLuong,
            @Field("chiTiet") String chiTiet
    );
}
