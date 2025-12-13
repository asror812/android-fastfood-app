package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.FavouriteApi;
import com.example.myapplication.api.dto.response.ApiMessageResponse;
import com.example.myapplication.api.dto.response.ProductResponseDto;
import com.example.myapplication.security.TokenStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {

    private List<ProductResponseDto> list;
    private Context context;

    private FavouriteApi favouriteApi;

    public ProductAdapter(List<ProductResponseDto> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product, parent, false);

        favouriteApi = ApiClient.getClient(context).create(FavouriteApi.class);

        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {
        ProductResponseDto p = list.get(position);

        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(p.getPrice() + " so'm");

        GlideUrl glideUrl = new GlideUrl(
                ApiClient.BASE_URL + "/attachments/download/" + p.getMainImage().getId(),
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + TokenStorage.getToken(context))
                        .build()
        );

        Glide.with(context)
                .load(glideUrl)
                .placeholder(R.drawable.sampl_food)
                .into(holder.img);

        holder.btnAdd.setOnClickListener(v -> {
            Toast.makeText(context, p.getName() + " savatga qo‘shildi", Toast.LENGTH_SHORT).show();
        });

        holder.imgFavorite.setOnClickListener(v -> {
            favouriteApi.addFavorite(p.getId()).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {
                    if (!response.isSuccessful()) {
                        holder.imgFavorite.setImageResource(R.drawable.ic_favorite);
                        //p.setFavorite(false);
                    }
                }


                @Override
                public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                    holder.imgFavorite.setImageResource(R.drawable.img_favorite);
                    // p.setFavorite(false);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductVH extends RecyclerView.ViewHolder {

        ImageView img, imgFavorite;
        TextView tvName, tvPrice;
        Button btnAdd;

        public ProductVH(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgProduct);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
