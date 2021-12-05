package com.example.jaluziapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.balysv.materialripple.MaterialRippleLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

public class ProductSpecify extends AppCompatActivity {

    int id;
    String action;
    ImageView image;
    EditText widthField;
    EditText heightField;
    TextView priceText;
    TextView nameText;
    TextView finalPriceText;
    Button btnaddtocart;
    MaterialRippleLayout addToCart;
    Product thisProduct;
    int currentPrice;
    Bundle extras;

    int productid;
    int width;
    int height;
    Date date;
    int price;
    String imageUrl;
    String name;
    int index;
    OrderedProduct p;
    double mul;

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_product_specify);
        image = findViewById(R.id.previewImage);
        widthField = findViewById(R.id.widthField);
        btnaddtocart = findViewById(R.id.btnaddtocart);
        heightField = findViewById(R.id.heightField);
        height = 0;
        width = 0;
        addToCart = findViewById(R.id.buttonAddToCart);
        priceText = findViewById(R.id.priceField);
        nameText = findViewById(R.id.nameField);

        finalPriceText = findViewById(R.id.finalPriceField);

        extras = getIntent().getExtras();

        assert extras != null;
        id = extras.getInt("id");
        getProductInfo productInfo = new getProductInfo();
        imageUrl = extras.getString("image");
        name = extras.getString("name");
        action = extras.getString("action");
        index = extras.getInt("index");
        if (action.equals("edit")) {
            btnaddtocart.setText("Подтвердить");
            index = extras.getInt("index");
            p = CartClass.getCart().get(index);
            width = p.width;
            height = p.height;
        }


        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(widthField.getText().toString().length() > 0 & heightField.getText().toString().length() > 0){
                    currentPrice = (int) Math.round((Double.parseDouble(widthField.getText().toString())/100) * (Double.parseDouble(heightField.getText().toString())/100) * thisProduct.type_price_multiplier * Double.parseDouble(thisProduct.material_price));
                    finalPriceText.setText("Итоговая цена: " + currentPrice);
                }
                else finalPriceText.setText("Заполните все поля");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        widthField.addTextChangedListener(tw);
        heightField.addTextChangedListener(tw);
        addToCart.setOnClickListener(listener(action));
        System.out.println(action);
        productInfo.execute(id);
    }

    public View.OnClickListener listener(String action) {
        String MESSAGE = "Добавить в корзину?";
        if(Objects.equals(action, "edit")) MESSAGE = "Подтвердить изменения?";
        String finalMESSAGE = MESSAGE;
        return view -> {
            if (isEmpty(widthField)){
                widthField.setError("Поле не должно быть пустым");

            }else
            if (isEmpty(heightField)) {
                heightField.setError("Поле не должно быть пустым");

            }else
            if(Double.parseDouble(widthField.getText().toString()) > thisProduct.max_width) {
                widthField.setError("Ширина должна быть меньше " + thisProduct.max_width);

            }else

            if(Integer.parseInt(heightField.getText().toString()) > thisProduct.max_height) {
                heightField.setError("Ширина должна быть меньше " + thisProduct.max_height);

            }else
            if(Integer.parseInt(widthField.getText().toString()) <= thisProduct.max_width &&
                    !isEmpty(widthField) &&
                    Integer.parseInt(heightField.getText().toString()) <= thisProduct.max_height &&
                    !isEmpty(heightField)
            ){
                buttonAction(action, finalMESSAGE);
            }
        };
    }
    public void buttonAction(String action, String finalMESSAGE){
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(finalMESSAGE);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                finalMESSAGE.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        if (action.equals("specify")) {
            new AlertDialog.Builder(this, R.style.alertdialogstyle)
                    .setMessage(ssBuilder)
                    .setCancelable(true)
                    .setPositiveButton("Да", (dialogInterface, i) -> {
                        OrderedProduct p = new OrderedProduct(
                                thisProduct.id,
                                Integer.parseInt(widthField.getText().toString()),
                                Integer.parseInt(heightField.getText().toString()),
                                date,
                                currentPrice,
                                thisProduct.image,
                                thisProduct.name,
                                thisProduct.type_price_multiplier);
                        System.out.println("currdate: " + date);
                        CartClass.getCart().add(p);
                        Toast.makeText(ProductSpecify.this, "Кол-во товаров в корзине равно " + CartClass.getCart().size(), Toast.LENGTH_LONG).show();
                        finish();
                    }).setNegativeButton("Отмена", null).show();
        }
            else if(action.equals("edit")) {
            new AlertDialog.Builder(this, R.style.alertdialogstyle)
                    .setMessage(ssBuilder)
                    .setCancelable(true)
                    .setPositiveButton("Да", (dialogInterface, i) -> {
                        OrderedProduct p = new OrderedProduct(
                                id,
                                Integer.parseInt(widthField.getText().toString()),
                                Integer.parseInt(heightField.getText().toString()),
                                date,
                                currentPrice,
                                imageUrl,
                                name,
                                thisProduct.type_price_multiplier);
                        System.out.println("currname: " + name);
                        System.out.println("currimage: " + imageUrl);
                        CartClass.setProduct(extras.getInt("index"), p);
                        Toast.makeText(ProductSpecify.this, "Изменено", Toast.LENGTH_LONG).show();
                        finish();
                    }).setNegativeButton("Отмена", null).show();
            }
        }

class getProductInfo extends AsyncTask<Integer, Void, Product> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductSpecify.this);
            pDialog.setMessage("Идет загрузка товара...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Product doInBackground(Integer... integers) {
            try{
                final String POST_PARAMS = "?id="+integers[0];
                URL obj = new URL(ProductSpecify.this.getString(R.string.SERVER_GET_PRODUCT_INFO)+POST_PARAMS); ///////////////////
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
                    final ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(response.toString(), Product.class);
                } else {
                    return null;
                }
            }catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
            pDialog.dismiss();
            if(product!=null){
                thisProduct = product;
                Picasso.get().load(ProductSpecify.this.getString(R.string.SERVER_URL) + product.image).into(image);
                if(p!=null){
                    //widthField.setHint(p.width);
                    //heightField.setHint(p.height);
                }else {
                    widthField.setHint("Ширина до " + product.max_width + " мм");
                    heightField.setHint("Высота до " + product.max_height + " мм");
                }
                priceText.setText("Цена за кв м: " + product.material_price);
                nameText.setText(product.name);
            }
        }
    }
}