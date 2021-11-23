package com.example.jaluziapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    ArrayList<ProductType> productTypes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_list);
        RecyclerView recyclerView = findViewById(R.id.productList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        productTypes.add(new ProductType(R.drawable.rulon, "Рулонные", "от 700 руб"));
        productTypes.add(new ProductType(R.drawable.plisse, "Плиссе", "от 2100 руб"));
        productTypes.add(new ProductType(R.drawable.zebra, "Зебра", "от 1500 руб"));
        productTypes.add(new ProductType(R.drawable.gorizont, "Горизонтальные", "от 1800 руб"));
        ListAdapter adapter = new ListAdapter(this, productTypes);
        recyclerView.setAdapter(adapter);
    }
}

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private final List<ProductType> dataSet;
    public final Context context;

    ListAdapter(Context context, List<ProductType> productTypes) {
        this.dataSet = productTypes;
        this.context = context;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductType currentProducts = dataSet.get(position);
        holder.getPreviewView().setImageResource(currentProducts.getPreview());
        holder.getNameView().setText(currentProducts.getName());
        holder.getPriceView().setText(currentProducts.getPrice());
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
                item = view.findViewById(R.id.mainmenuitem);
                previewView = view.findViewById(R.id.previewImage);
                nameView = view.findViewById(R.id.textLarge);
                priceView = view.findViewById(R.id.textSmall);
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
            Intent intent;
            switch(getLayoutPosition()){
                case 0:
                    intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("title", "0");
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("title", "1");
                    context.startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("title", "2");
                    context.startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("title", "3");
                    context.startActivity(intent);
                    break;
        }
        };
}}