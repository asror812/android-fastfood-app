package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.RegisterActivity;
import com.example.myapplication.activity.VerifyCodeActivity;
import com.example.myapplication.adapter.FavoriteAdapter;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.FavouriteApi;
import com.example.myapplication.api.dto.response.ProductResponseDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment {
    RecyclerView rv;
    Button btnClear;
    FavoriteAdapter adapter;

    FavouriteApi api;
    List<ProductResponseDto> list = new ArrayList<>();

    FavoriteAdapter favoriteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        rv = v.findViewById(R.id.rvFavorites);
        btnClear = v.findViewById(R.id.btnClear);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FavoriteAdapter(list, requireContext());
        rv.setAdapter(adapter);

        loadFavorites();

        btnClear.setOnClickListener(view -> {
            list.clear();
            adapter.notifyDataSetChanged();
        });

        return v;
    }

    private void loadFavorites() {
        api = ApiClient.getClient(requireActivity()).create(FavouriteApi.class);

        api.getFavoriteProducts().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductResponseDto>> call, Response<List<ProductResponseDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API", "Error: " + response.code());
                    Toast.makeText(requireContext(), "Server xatosi", Toast.LENGTH_SHORT).show();
                    return;
                }

                list.clear();
                list.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ProductResponseDto>> call, Throwable t) {

            }
        });
    }

}

