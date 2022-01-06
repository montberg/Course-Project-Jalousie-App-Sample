package com.example.jaluziapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Button loginButton = findViewById(R.id.loginButton);
        EditText loginText = findViewById(R.id.editTextLogin);
        EditText passwordText = findViewById(R.id.editTextPassword);
        TextView register = findViewById(R.id.register);
        View.OnClickListener loginBtnListener = view -> {
            if(loginText.getText().toString().isEmpty()) loginText.setError("Введите логин!");
            if(passwordText.getText().toString().isEmpty()) passwordText.setError("Введите пароль!");
            if(!loginText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()){
                getUserData getUserDataTask = new getUserData(this);
                getUserDataTask.execute(loginText.getText().toString(), GlobalClass.getHash(passwordText.getText().toString()));
            }
        };
        View.OnClickListener registerListener = view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        };
        loginButton.setOnClickListener(loginBtnListener);
        register.setOnClickListener(registerListener);
    }
}

class getUserData extends AsyncTask<String, Void, UserDataResponse> {

    Context context;

    public getUserData(Context context){
        this.context = context;
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
    protected UserDataResponse doInBackground(String... strings) {
        try{
            final String POST_PARAMS = "?login="+strings[0]+"&password="+strings[1];
            final String url_get_userData = context.getString(R.string.SERVER_GET_USER)+POST_PARAMS;
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
                map.get("message");
                GlobalClass.setUserId(Integer.parseInt(Objects.requireNonNull(map.get("client_id")).toString()));
                System.out.println(Objects.requireNonNull(map.get("code")).toString());
                System.out.println(Objects.requireNonNull(map.get("message")).toString());

                return new UserDataResponse(Integer.parseInt(map.get("code").toString()), map.get("message").toString());
            } else {
                return new UserDataResponse(0, "Ошибка подключения к серверу 1");
            }
        }catch (IOException e) {
            e.printStackTrace();
            return new UserDataResponse(0, "Ошибка подключения к серверу 2");
        }
    }

    @Override
    protected void onPostExecute(UserDataResponse userDataResponse) {
        super.onPostExecute(userDataResponse);
        pDialog.dismiss();
        if(userDataResponse.CODE == 1) context.startActivity(new Intent(context, MainMenu.class));
        else Toast.makeText(context, userDataResponse.MESSAGE, Toast.LENGTH_LONG).show();
    }
}