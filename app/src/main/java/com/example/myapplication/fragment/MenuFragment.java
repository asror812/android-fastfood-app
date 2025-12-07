package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CategoryAdapter;
import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.CategoryApi;
import com.example.myapplication.api.dto.response.CategoryResponseDto;
import com.example.myapplication.api.dto.response.ProductResponseDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {

    private RecyclerView rvCategories, rvProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private CategoryApi categoryApi;

    private final List<CategoryResponseDto> categoryList = new ArrayList<>();
    private final List<ProductResponseDto> productList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        rvCategories = view.findViewById(R.id.rvCategories);
        rvProducts = view.findViewById(R.id.rvProducts);

        rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        );

        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        categoryApi = ApiClient.getClient(requireContext()).create(CategoryApi.class);

        categoryAdapter = new CategoryAdapter(getContext(), categoryList, category -> {
            loadProducts(category.getId());
        });

        productAdapter = new ProductAdapter(productList, getContext());

        rvCategories.setAdapter(categoryAdapter);
        rvProducts.setAdapter(productAdapter);

        loadCategories();

        return view;
    }

    private void loadCategories() {

        categoryApi.getAllParentCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CategoryResponseDto>> call,
                                   Response<List<CategoryResponseDto>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(), "Kategoriya yuklanmadi", Toast.LENGTH_SHORT).show();
                    return;
                }

                categoryList.clear();
                categoryList.addAll(response.body());
                categoryAdapter.notifyDataSetChanged();

                if (!categoryList.isEmpty()) {
                    loadProducts(categoryList.get(0).getId());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryResponseDto>> call, Throwable t) {
                Log.e("MenuFragment", t.getMessage());
            }
        });
    }

    private void loadProducts(UUID id) {
        categoryApi.getProductsByCategoryId(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductResponseDto>> call,
                                   Response<List<ProductResponseDto>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(), "Mahsulotlar yuklanmadi", Toast.LENGTH_SHORT).show();
                    return;
                }

                productList.clear();
                productList.addAll(response.body());

                StringBuilder str = new StringBuilder();
                for (ProductResponseDto productResponseDto : productList) {
                    str.append(productResponseDto).append("\n");
                }

                Log.e("API", str.toString());

                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ProductResponseDto>> call, Throwable t) {
                Log.e("MenuFragment", t.getMessage());
            }
        });
    }
}
