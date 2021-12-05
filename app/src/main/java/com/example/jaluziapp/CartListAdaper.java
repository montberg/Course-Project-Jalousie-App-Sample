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

public class CartListAdaper extends RecyclerView.Adapter<CartListAdaper.ViewHolder>{

    public final List<OrderedProduct> dataSet;
    public final Context context;

    CartListAdaper(Context context, List<OrderedProduct> products) {
        this.dataSet = products;
        this.context = context;
    }

    @NonNull
    @Override
    public CartListAdaper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_item, parent, false);
        return new CartListAdaper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartListAdaper.ViewHolder holder, int position) {
        OrderedProduct currentProduct = dataSet.get(position);
        Picasso.get().load(context.getString(R.string.SERVER_URL) + currentProduct.image).into(holder.getPreviewView());
        holder.getNameView().setText(String.valueOf(currentProduct.name));
        holder.getPriceView().setText(String.valueOf(currentProduct.price));
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
            item = view.findViewById(R.id.cart_product_item);
            previewView = view.findViewById(R.id.cart_image_preview);
            nameView = view.findViewById(R.id.cart_big_text);
            priceView = view.findViewById(R.id.cart_price_view);
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
            Context context = view.getContext();
            OrderedProduct current = ProductListActivity.cartProducts.get(getLayoutPosition());
            Intent intent = new Intent(context, ProductSpecify.class);
            intent.putExtra("action", "edit");
            intent.putExtra("id", current.productID);
            intent.putExtra("width", current.width);
            intent.putExtra("height", current.height);
            intent.putExtra("date", current.date);
            intent.putExtra("price", current.price);
            intent.putExtra("image", current.image);
            intent.putExtra("name", current.name);
            intent.putExtra("index", getLayoutPosition());
            intent.putExtra("mul", current.type_price_multiplier);
            context.startActivity(intent);
        };
    }
}