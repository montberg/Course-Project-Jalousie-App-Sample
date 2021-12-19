package com.example.jaluziapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{

    private final List<Product> dataSet;
    public final Context context;

    ProductListAdapter(Context context, List<Product> products) {
        this.dataSet = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product currentProduct = dataSet.get(position);

        Picasso.get().load(context.getString(R.string.SERVER_URL) + currentProduct.image).into(holder.getPreviewView());
        holder.getNameView().setText(currentProduct.name);
        holder.getPriceView().setText(currentProduct.material_price + " руб за кв м");
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView previewView;
        private final TextView nameView, priceView;
        private final MaterialRippleLayout item;
        ViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.product_item);
            previewView = view.findViewById(R.id.image_preview);
            nameView = view.findViewById(R.id.big_text);
            priceView = view.findViewById(R.id.small_text);
            item.setOnClickListener(main_menu_item_click_listener);
        }
        public ImageView getPreviewView(){
            return previewView;
        }

        public TextView getNameView(){
            return nameView;
        }

        public TextView getPriceView(){
            return priceView;
        }

        View.OnClickListener main_menu_item_click_listener = view -> {
            if(!ProductListActivity.products.isEmpty()){
                Context context = view.getContext();
                Product current = ProductListActivity.products.get(getLayoutPosition());
                Intent intent = new Intent(context, ProductSpecify.class);
                intent.putExtra("id", current.id);
                intent.putExtra("action", "specify");
                context.startActivity(intent);
            }
        };
    }}
