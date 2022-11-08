package com.poly_store_online.utils;

import com.poly_store_online.model.GioHang;
import com.poly_store_online.model.NguoiDung;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "https://luongngochai.000webhostapp.com/";
    public static NguoiDung nguoidung_current = new NguoiDung();
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static String ID_RECEIVED;
    public static final String SENDID = "idsend";
    public static final String RECEIVEDID = "idreceived";
    public static final String MESS = "message";
    public static final String DATETIME = "datetime";
    public static final String PATH_CHAT = "chat";

}
