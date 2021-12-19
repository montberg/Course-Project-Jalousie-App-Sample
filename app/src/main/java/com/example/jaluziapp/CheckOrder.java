package com.example.jaluziapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class CheckOrder extends AsyncTask<String, Void, String> {

    Context context;
    TextView d;

    public CheckOrder(Context context, TextView d){
        this.context = context;
        this.d = d;
    }

    ProgressDialog pDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(this.context);
        pDialog.setMessage("Пожалуйста, подождите...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try{
            final String POST_PARAMS = "?order_code="+strings[0];
            final String url_get_userData = context.getString(R.string.SERVER_CHECK_AN_ORDER)+POST_PARAMS;
            URL obj = new URL(url_get_userData);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);
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
                return map.get("status").toString();
            } else {
                return "Ошибка сервера";
            }
        }catch (IOException e) {
            e.printStackTrace();
            return "Заказ не существует";
        }
    }
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        pDialog.dismiss();
        d.setVisibility(View.VISIBLE);
        d.setText(response);
    }
}
