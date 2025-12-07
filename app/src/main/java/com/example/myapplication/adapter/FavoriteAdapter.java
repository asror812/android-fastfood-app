package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.dto.response.ProductResponseDto;
import com.example.myapplication.security.TokenStorage;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<ProductResponseDto> list;
    private Context context;

    public FavoriteAdapter(List<ProductResponseDto> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView txtName, txtPrice, txtCount;
        Button btnPlus, btnMinus;
        ImageButton btnDelete;

        private Context context;

        LinearLayout btnAddToCart;

        public ViewHolder(View v) {
            super(v);
            imgFood = v.findViewById(R.id.imgFood);
            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            btnAddToCart = v.findViewById(R.id.btnAddSmall);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductResponseDto item = list.get(position);

        holder.txtName.setText(item.getName());
        holder.txtPrice.setText(item.getPrice() + " so'm");


        GlideUrl glideUrl = new GlideUrl(
                ApiClient.BASE_URL + "/attachments/download/" + item.getMainImage().getId(),
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + TokenStorage.getToken(context))
                        .build()
        );

        Glide.with(context)
                .load(glideUrl)
                .placeholder(R.drawable.sampl_food)
                .into(holder.imgFood);

        holder.btnDelete.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

