package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myapplication.api.dto.response.product.ProductImageResponseDto;
import com.example.myapplication.api.dto.response.product.ProductResponseDto;
import com.example.myapplication.security.TokenStorage;

import java.util.Comparator;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<ProductResponseDto> list;
    private final Context context;

    public FavoriteAdapter(List<ProductResponseDto> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView txtName, txtPrice;
        ImageButton btnDelete;
        LinearLayout btnAddToCart;

        public ViewHolder(View v) {
            super(v);
            imgFood = v.findViewById(R.id.imgFood);
            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            btnAddToCart = v.findViewById(R.id.btnAddSmall);
            btnDelete = v.findViewById(R.id.btnDelete);
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
        ProductResponseDto p = list.get(position);

        holder.txtName.setText(p.getName());
        holder.txtPrice.setText(p.getPrice() + " so'm");

        ProductImageResponseDto first = null;
        if (p.getImages() != null && !p.getImages().isEmpty()) {
            first = p.getImages().stream()
                    .min(Comparator.comparingInt(ProductImageResponseDto::getPosition))
                    .orElse(null);
        }

        if (first != null && first.getDownloadUrl() != null && first.getDownloadUrl().isBlank()) {
            GlideUrl glideUrl = new GlideUrl(
                    ApiClient.BASE_URL + first.getDownloadUrl(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + TokenStorage.getToken(context))
                            .build()
            );

            Glide.with(context)
                    .load(glideUrl)
                    .placeholder(R.drawable.sampl_food)
                    .error(R.drawable.sampl_food)
                    .into(holder.imgFood);
        }

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

