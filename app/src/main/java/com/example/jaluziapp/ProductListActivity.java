package com.example.jaluziapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ProductListActivity extends AppCompatActivity {
    static ProductListAdapter adapter;
    static CartListAdaper cartadapter;
    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<OrderedProduct> cartProducts = new ArrayList<>();
    int[] currentProductContext;
    Button btnGoToCart;
    MaterialRippleLayout goToCart;
    static TextView priceString, titleText;
    RelativeLayout cartLayout;
    static RecyclerView recyclerView;
    static String task;
    Toolbar actionBar;
    ImageView checkStatusButton;
    @Override
    protected void onResume() {
        super.onResume();
        invalidateUI();
        products.clear();
        if(currentProductContext[0]==100) updateCart(recyclerView); else {
        getProductList getProductListTask = new getProductList(this);
        getProductListTask.execute(currentProductContext[0]);
        }
        priceUpdate();
        System.out.println("12344 цена обновилась");
        adapter.notifyDataSetChanged();
    }

    public static void priceUpdate(){
        priceString.setText("Итого: " + CartClass.getPrice() + " руб.");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("12344 ondestroy");
        priceUpdate();
        if(currentProductContext[0]==100){
            currentProductContext[0] = currentProductContext[1];
            currentProductContext[1] = -1;
        }
    }

    @Override
    public boolean isDestroyed() {
        priceUpdate();
        return super.isDestroyed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = "";
        System.out.println("12344 oncreate");
        setContentView(R.layout.product_list);
        if(products!=null) products.clear();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        currentProductContext = new int[]{-1,-1};
        //определение тулбара


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


        // показываем кнопку назад
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        invalidateUI();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        btnGoToCart.setOnClickListener(view -> {
            Intent gotocart = new Intent(ProductListActivity.this, ProductListActivity.class);
            gotocart.putExtra("title", "100");
            startActivity(gotocart);
        });
        View.OnClickListener makeAnOrder = view -> {
            if(!CartClass.getCart().isEmpty()){
                ObjectMapper objMapper = new ObjectMapper();
            try {
                String jsonString = objMapper.writeValueAsString(CartClass.getCart());

                makeAnOrderClass order = new makeAnOrderClass(this ,jsonString);
                order.execute();

                System.out.println(jsonString);


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Произошла ошибка");
            }
        }};
        adapter = new ProductListAdapter(this, products);
        btnGoToCart.setText("Корзина");
        //получаем нажатую кнопку с предыдущего активити
        if(title != null){
            switch (title){
                case "0":
                    titleText.setText("Рулонные шторы");
                    currentProductContext[0] = 1;
                    break;
                case "1":
                    titleText.setText("Шторы плиссе");
                    currentProductContext[0] = 3;
                    break;
                case "2":
                    titleText.setText("Шторы зебра");
                    currentProductContext[0] = 4;
                    break;
                case "3":
                    titleText.setText("Горизонтальные шторы");
                    currentProductContext[0] = 2;
                    break;
                case "100":
                    titleText.setText("Корзина");
                    currentProductContext[1] = currentProductContext[0];
                    currentProductContext[0] = 100;
                    btnGoToCart.setText("Оформить заказ");
                    cartadapter = new CartListAdaper(this, cartProducts);
                    btnGoToCart.setOnClickListener(makeAnOrder);
                    break;
            }
        }
        else setTitle("Ошибка");

        if(currentProductContext[0]==100) {
            updateCart(recyclerView);
        }else{
            recyclerView.setAdapter(adapter);
            getProductList getProductListTask = new getProductList(this);
            getProductListTask.execute(currentProductContext[0]);
        }
    }

    public static RecyclerView getRecyclerView(){
        return recyclerView;
    }
    public static void updateCart(RecyclerView view){
        view.setAdapter(cartadapter);
        cartProducts.clear();
        cartProducts.addAll(CartClass.getCart());
        priceUpdate();
        adapter.notifyDataSetChanged();
    }

    private void invalidateUI(){
        btnGoToCart = findViewById(R.id.buttongtc);
        goToCart = findViewById(R.id.btnGoToCart);
        cartLayout = findViewById(R.id.cartLayout);
        priceString = findViewById(R.id.priceString1);
        recyclerView = findViewById(R.id.product_list);
    }

    static class getProductList extends AsyncTask<Integer, Void, ArrayList<Product>>{

        Context context;
        ProgressDialog pDialog;

        public getProductList(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(this.context);
            pDialog.setMessage("Идет загрузка товаров...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected ArrayList<Product> doInBackground(Integer... integers) {
            if(integers[0]==100) return null;
                try{
                    final String POST_PARAMS = "?context="+integers[0];
                    URL obj = new URL(context.getString(R.string.SERVER_GET_ALL_PRODUCTS)+POST_PARAMS); ///////////////////
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    System.out.println("POST Response Code :: " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) { //success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        System.out.println("Response :: " + response.toString());
                        System.out.println("это  код "+integers[0]);
                        final ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(response.toString(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Product.class));
                    } else {
                        return null;
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
        }
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(ArrayList<Product> productsList) {
            super.onPostExecute(productsList);

            pDialog.dismiss();
            priceUpdate();

            if(productsList!=null)
            {
                products.clear();
                products.addAll(productsList);
                adapter.notifyDataSetChanged();
            }
        }
    }


    private static class makeAnOrderClass extends AsyncTask<Void, Void, Integer> {
        String jsonString;
        Context context;
        ProgressDialog pDialog;
        String orderCode;
        static ClipboardManager clipboard;
        public makeAnOrderClass(Context context ,String jsonString) {
            this.jsonString = jsonString;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Пожалуйста, подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Integer doInBackground(Void... strings) {
            try
            {
            final String POST_PARAMS = "?json="+jsonString+"&client_id="+GlobalClass.getUserId();
            System.out.println("POST PARAMS "+POST_PARAMS);
            URL obj = new URL(context.getString(R.string.SERVER_SEND_AN_ORDER)+POST_PARAMS);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> map = mapper.readValue(response.toString(), Map.class);
                orderCode = map.get("order_code").toString();
            }

            System.out.println("POST Response Code :: " + responseCode);

                return responseCode;
            }
            catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
        @Override
        protected void onPostExecute(Integer code) {
            pDialog.dismiss();
            if(code==200){
                final Dialog fbDialogue = new Dialog(context);
                fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
                fbDialogue.setContentView(R.layout.check_order_status_fragment);
                TextView text = fbDialogue.findViewById(R.id.text);
                text.setText("Ваш номер заказа:");
                EditText orderCodeText = fbDialogue.findViewById(R.id.orderCode);
                orderCodeText.setEnabled(true);
                orderCodeText.setText(orderCode);
                MaterialRippleLayout goToOrder = fbDialogue.findViewById(R.id.goToOrder);
                Button dummybtn = fbDialogue.findViewById(R.id.btn);
                dummybtn.setText("Закрыть");
                goToOrder.setOnClickListener(view1 -> {
                    CartClass.cleanCart();
                    fbDialogue.dismiss();
                    ((Activity)context).finish();
                });
                orderCodeText.setOnClickListener(view -> {
                    clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("code", orderCodeText.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show();
                });
                fbDialogue.setCancelable(false);
                fbDialogue.show();
            }else{
                Toast.makeText(context, "Ошибка " + code, Toast.LENGTH_SHORT).show();
            }
        }
    }
}




