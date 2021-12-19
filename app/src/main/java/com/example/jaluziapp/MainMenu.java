package com.example.jaluziapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainMenu extends AppCompatActivity {

    Toolbar actionBar;
    ImageView checkStatusButton;
    TextView titleText;
    ArrayList<ProductType> productTypes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_list);
        RecyclerView recyclerView = findViewById(R.id.productList);


        actionBar = findViewById(R.id.my_toolbar);

        setSupportActionBar(actionBar);
        actionBar.setNavigationOnClickListener(v -> {
            finish();
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowCustomEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setCustomView(R.layout.action_bar_layout);

        checkStatusButton = (ImageView) findViewById(R.id.goCheckStatus);
        titleText = (TextView) findViewById(R.id.titleText);
        checkStatusButton.setOnClickListener(view -> {
            final Dialog fbDialogue = new Dialog(this);
            fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
            fbDialogue.setContentView(R.layout.check_order_status_fragment);
            EditText orderCode = fbDialogue.findViewById(R.id.orderCode);
            TextView existance = fbDialogue.findViewById(R.id.doesnotexisttext);
            MaterialRippleLayout goToOrder = fbDialogue.findViewById(R.id.goToOrder);

            goToOrder.setOnClickListener(view1 -> {
                if(orderCode.getText().length()!=0){
                CheckOrder order = new CheckOrder(this, existance);
                order.execute(orderCode.getText().toString());
                }else orderCode.setError("Введите код");
            });
            fbDialogue.setCancelable(true);
            fbDialogue.show();
        });


        titleText.setText("Главное меню");



        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        productTypes.add(new ProductType(R.drawable.rulon, "Рулонные", "от 1150 руб"));
        productTypes.add(new ProductType(R.drawable.plisse, "Плиссе", "от 3385 руб"));
        productTypes.add(new ProductType(R.drawable.zebra, "Зебра", "от 2640 руб"));
        productTypes.add(new ProductType(R.drawable.gorizont, "Горизонтальные", "от 1350 руб"));
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