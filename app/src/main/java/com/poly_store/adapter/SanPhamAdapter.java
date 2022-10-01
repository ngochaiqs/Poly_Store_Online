package com.poly_store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poly_store.R;
import com.poly_store.model.SanPham;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.MyViewHolder> {
    Context context;
    List<SanPham> sanPhamList;

    public SanPhamAdapter(Context context, List<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPham = sanPhamList.get(position);
        holder.txtTenSP.setText(sanPham.getTenSP());
        holder.txtGiaSP.setText(sanPham.getGiaSP() + "VNĐ");
        Glide.with(context).load(sanPham.getHinhSP()).into(holder.imgSP);
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSP, txtGiaSP;
        ImageView imgSP;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSP = itemView.findViewById(R.id.tvTenSanPham);
            txtGiaSP = itemView.findViewById(R.id.tvGiaSanPham);
            imgSP = itemView.findViewById(R.id.imgSanPham);
        }
    }
}
