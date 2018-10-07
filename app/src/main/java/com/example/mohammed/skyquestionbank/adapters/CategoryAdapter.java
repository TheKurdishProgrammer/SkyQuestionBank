package com.example.mohammed.skyquestionbank.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.interfaces.OnRecyclerItemClick;
import com.example.mohammed.skyquestionbank.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categories;
    private OnRecyclerItemClick listener;
    private CategoryViewHolder lastHolder;
    private int categoryPosition;

    public CategoryAdapter(int categoryPosition, Context context, List<Category> categories, OnRecyclerItemClick listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
        this.categoryPosition = categoryPosition;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (holder.getAdapterPosition() == categoryPosition) {
            holder.catRadio.setChecked(true);
            lastHolder = holder;
        }

        holder.catName.setText(categories.get(position).getName());

        holder.itemView.setOnClickListener(v -> {

            listener.onItemClicked(holder.getAdapterPosition());

            holder.catRadio.setChecked(true);

            if (lastHolder != holder)
                lastHolder.catRadio.setChecked(false);

            lastHolder = holder;
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private RadioButton catRadio;
        private TextView catName;

        CategoryViewHolder(View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.category_name);
            catRadio = itemView.findViewById(R.id.category_radio);
        }
    }

}
