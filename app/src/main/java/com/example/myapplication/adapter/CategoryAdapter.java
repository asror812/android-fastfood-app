package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.api.dto.response.CategoryResponseDto;

import java.util.List;
import android.content.Context;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CategoryResponseDto> list;
    Context ctx;
    int selectedIndex = 0;
    CategoryClickListener listener;

    public interface CategoryClickListener {
        void onCategoryClick(CategoryResponseDto category);
    }

    public CategoryAdapter(Context ctx, List<CategoryResponseDto> list, CategoryClickListener listener) {
        this.ctx = ctx;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder h, @SuppressLint("RecyclerView") int position) {
        CategoryResponseDto c = list.get(position);
        h.txt.setText(c.getName());

        if (selectedIndex == position) {
            h.txt.setBackgroundResource(R.drawable.bg_category_selected);
        } else {
            h.txt.setBackgroundResource(R.drawable.bg_category_default);
        }

        h.txt.setOnClickListener(v -> {
            selectedIndex = position;
            notifyDataSetChanged();
            listener.onCategoryClick(c);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txtCategory);
        }
    }
}

