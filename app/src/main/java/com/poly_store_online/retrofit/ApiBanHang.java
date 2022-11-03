package com.poly_store_online.retrofit;

import com.poly_store_online.model.DonHangModel;
import com.poly_store_online.model.LoaiSPModel;
import com.poly_store_online.model.MessageModel;
import com.poly_store_online.model.NguoiDungModel;
import com.poly_store_online.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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


    @POST("xemdon.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("maND") int maND

    );

    @POST("timkiemsp.php")
    @FormUrlEncoded
    Observable<SanPhamModel> timKiem(
            @Field("timKiem") String timKiem

    );

    @POST("xoa.php")
    @FormUrlEncoded
    Observable<SanPhamModel> xoaSanPham(
            @Field("maSP") int maSP

    );

    @POST("themsp.php")
    @FormUrlEncoded
    Observable<MessageModel> themSP(
            @Field("tenSP") String tenSP,
            @Field("giaSP") String giaSP,
            @Field("hinhAnhSP") String hinhAnhSP,
            @Field("moTa") String moTa,
            @Field("maLoai") int maLoai
    );

    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> updatesp(
            @Field("tenSP") String tenSP,
            @Field("giaSP") String giaSP,
            @Field("hinhAnhSP") String hinhAnhSP,
            @Field("moTa") String moTa,
            @Field("maLoai") int maLoai,
            @Field("maSP") int maSP

    );
    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(
            @Part MultipartBody.Part file
    );
}
