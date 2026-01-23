package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.myapplication.api.dto.response.product.ProductImageResponseDto;
import com.example.myapplication.api.dto.response.product.ProductResponseDto;
import com.example.myapplication.security.TokenStorage;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {

    public enum Mode { MENU, FAVORITES }

    private final List<ProductResponseDto> list;
    private final Context context;
    private final Mode mode;

    private FavouriteApi favouriteApi;

    public ProductAdapter(List<ProductResponseDto> list, Context context, Mode mode) {
        this.list = list;
        this.context = context;
        this.mode = mode;
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (mode == Mode.FAVORITES)
                ? R.layout.item_favorite
                : R.layout.item_product;

        View view = LayoutInflater.from(context).inflate(layout, parent, false);

        if (favouriteApi == null) {
            favouriteApi = ApiClient.getClient(context).create(FavouriteApi.class);
        }

        return new ProductVH(view, mode);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {
        ProductResponseDto p = list.get(position);

        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(p.getPrice() + " so'm");

        loadFirstImage(holder.img, p);

        if (mode == Mode.FAVORITES) {
            // Favorites layout: delete button removes from favorites and from RecyclerView
            holder.btnDelete.setOnClickListener(v -> removeFromFavorites(holder, p));

            holder.btnAddSmall.setOnClickListener(v ->
                    Toast.makeText(context, p.getName() + " savatga qo‘shildi", Toast.LENGTH_SHORT).show()
            );

        } else {
            // Menu layout: heart toggles favorite state
            holder.imgFavorite.setImageResource(
                    p.isFavorite() ? R.drawable.ic_favorite : R.drawable.img_favorite
            );

            holder.btnAdd.setOnClickListener(v ->
                    Toast.makeText(context, p.getName() + " savatga qo‘shildi", Toast.LENGTH_SHORT).show()
            );

            holder.imgFavorite.setOnClickListener(v -> toggleFavoriteMenu(holder, p));
        }
    }

    private void loadFirstImage(ImageView target, ProductResponseDto p) {
        ProductImageResponseDto first = null;

        if (p.getImages() != null && !p.getImages().isEmpty()) {
            first = p.getImages().stream()
                    .min(Comparator.comparingInt(ProductImageResponseDto::getPosition))
                    .orElse(null);
        }

        if (first != null && first.getDownloadUrl() != null && !first.getDownloadUrl().isBlank()) {
            GlideUrl glideUrl = new GlideUrl(
                    ApiClient.BASE_URL + first.getDownloadUrl(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + TokenStorage.getToken(context))
                            .build()
            );

            Glide.with(context)
                    .load(glideUrl)
                    .placeholder(R.drawable.sampl_food)
                    .into(target);
        } else {
            // optional: fallback placeholder
            Glide.with(context)
                    .load(R.drawable.sampl_food)
                    .into(target);
        }
    }

    private void removeFromFavorites(ProductVH holder, ProductResponseDto p) {
        favouriteApi.removeFavorite(p.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {
                if (response.isSuccessful()) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        list.remove(pos);
                        notifyItemRemoved(pos);
                    }
                    Log.e("TAG", "Removed from favorites");
                } else {
                    Toast.makeText(context, "Remove failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavoriteMenu(ProductVH holder, ProductResponseDto p) {
        if (p.isFavorite()) {
            favouriteApi.removeFavorite(p.getId()).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {
                    if (response.isSuccessful()) {
                        p.setFavorite(false);
                        holder.imgFavorite.setImageResource(R.drawable.img_favorite);
                        Log.e("TAG", "Clicked remove from favorite");
                    } else {
                        Toast.makeText(context, "Remove failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                    // revert UI if you want
                    holder.imgFavorite.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            favouriteApi.addFavorite(p.getId()).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {
                    if (response.isSuccessful()) {
                        p.setFavorite(true);
                        holder.imgFavorite.setImageResource(R.drawable.ic_favorite);
                        Log.e("TAG", "Clicked add to favorite");
                    } else {
                        Toast.makeText(context, "Add failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                    // revert UI if you want
                    holder.imgFavorite.setImageResource(R.drawable.img_favorite);
                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductVH extends RecyclerView.ViewHolder {

        // Shared
        ImageView img;
        TextView tvName, tvPrice;

        // MENU only
        ImageView imgFavorite;
        Button btnAdd;

        // FAVORITES only
        ImageButton btnDelete;
        View btnAddSmall;

        public ProductVH(@NonNull View itemView, Mode mode) {
            super(itemView);

            if (mode == Mode.FAVORITES) {
                // IDs from your favorites item layout
                img = itemView.findViewById(R.id.imgFood);
                tvName = itemView.findViewById(R.id.txtName);
                tvPrice = itemView.findViewById(R.id.txtPrice);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                btnAddSmall = itemView.findViewById(R.id.btnAddSmall);
            } else {
                // IDs from your menu item layout
                img = itemView.findViewById(R.id.imgProduct);
                tvName = itemView.findViewById(R.id.tvName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                imgFavorite = itemView.findViewById(R.id.imgFavorite);
                btnAdd = itemView.findViewById(R.id.btnAdd);
            }
        }
    }
}
