package com.example.jaluziapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Toolbar actionBar;
    EditText txtName, txtCity, txtStreet, txtHouse, txtIndex, txtPassportID, txtPassportSerie;
    TextView txtDateOfIssue;
    Date dateOfIssue, dateOfBirth;
    TextView txtDateOfBirth, titleText;
    ImageView checkStatusButton;
    int index;
    EditText txtLogin, txtPassword, txtPasswordRepeat;
    MaterialRippleLayout button;
    SimpleDateFormat simpleDate;
    final int DATE_PICKER_BIRTH = 0;
    final int DATE_PICKER_ISSUE = 1;

    int birthyear, birthmonth, birthday, issueyear, issuemonth, issueday;
    DatePickerDialog.OnDateSetListener birthListener, issueListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        actionBar = findViewById(R.id.my_toolbar);
        txtName = findViewById(R.id.editTextFullName);
        txtCity = findViewById(R.id.editTextCity);
        txtStreet = findViewById(R.id.editTextStreet);
        txtHouse = findViewById(R.id.editTextHouseNumber);
        txtIndex = findViewById(R.id.editTextIndex);
        txtPassportID = findViewById(R.id.editTextPassportNumber);
        txtPassportSerie = findViewById(R.id.editTextPassportSerie);
        txtDateOfIssue = findViewById(R.id.editTextDateOfIssue);
        txtDateOfBirth = findViewById(R.id.editTextBirthDay);
        txtLogin = findViewById(R.id.editTextLogin);
        txtPassword = findViewById(R.id.editTextPassword);
        txtPasswordRepeat = findViewById(R.id.editTextPasswordRepeat);
        button = findViewById(R.id.buttonRegister);
        simpleDate =  new SimpleDateFormat("yyyy-MM-dd");




        setSupportActionBar(actionBar);
        actionBar.setNavigationOnClickListener(v -> {
            finish();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);






        setTitle("Регистрация");



        index = 0;
        Calendar myCalendar = Calendar.getInstance();

        birthyear = myCalendar.get(Calendar.YEAR);
        System.out.println("я устал " + birthyear );
        issueyear = myCalendar.get(Calendar.YEAR);
        birthmonth = myCalendar.get(Calendar.MONTH);
        issuemonth = myCalendar.get(Calendar.MONTH);
        issueday = myCalendar.get(Calendar.DAY_OF_MONTH);
        birthday = myCalendar.get(Calendar.DAY_OF_MONTH);
        birthListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateOfBirth = myCalendar.getTime();
            txtDateOfBirth.setText(simpleDate.format(dateOfBirth.getTime()));
        };
        issueListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateOfIssue = myCalendar.getTime();
            txtDateOfIssue.setText(simpleDate.format(dateOfIssue.getTime()));
        };

        txtDateOfIssue.setOnClickListener(view -> showDialog(DATE_PICKER_ISSUE));
        txtDateOfBirth.setOnClickListener(view -> showDialog(DATE_PICKER_BIRTH));
        button.setOnClickListener(view -> {
            if(!checkFields()) return;
            RegistrationInfo userInfo = new RegistrationInfo(txtName.getText().toString(),
                    txtCity.getText().toString(),
                    txtStreet.getText().toString(),
                    Integer.parseInt(txtHouse.getText().toString()),
                    Integer.parseInt(txtIndex.getText().toString()),
                    Integer.parseInt(txtPassportID.getText().toString()),
                    Integer.parseInt(txtPassportSerie.getText().toString()),
                    txtDateOfIssue.getText().toString(),
                    txtDateOfBirth.getText().toString(),
                    txtLogin.getText().toString(),
                    txtPassword.getText().toString());
            RegisterUser registerTask = new RegisterUser(this);
            registerTask.execute(userInfo);
        });
    }

    private Boolean checkFields(){
        if(!txtPassword.getText().toString().equals(txtPasswordRepeat.getText().toString())){
            txtPasswordRepeat.setError("Пароли не совпадают");
            return false;
        }
        if(txtName.getText().toString().isEmpty()) {
            txtName.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtStreet.getText().toString().isEmpty()) {
            txtStreet.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtHouse.getText().toString().isEmpty()) {
            txtHouse.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtIndex.getText().toString().isEmpty()) {
            txtIndex.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtPassportID.getText().toString().isEmpty()) {
            txtPassportID.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtPassportSerie.getText().toString().isEmpty()) {
            txtPassportSerie.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtDateOfIssue.getText().toString().isEmpty()) {
            txtDateOfIssue.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtDateOfBirth.getText().toString().isEmpty()) {
            txtDateOfBirth.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtLogin.getText().toString().isEmpty()) {
            txtLogin.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Поле не должно быть пустым");
            return false;
        }
        if(txtPasswordRepeat.getText().toString().isEmpty()) {
            txtPasswordRepeat.setError("Поле не должно быть пустым");
            return false;
        }
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATE_PICKER_BIRTH:
                return new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, birthListener, birthyear, birthmonth, birthday);
            case DATE_PICKER_ISSUE:
                return new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, issueListener, issueyear, issuemonth, issueday);
        }
        return null;
    }
    public class RegisterUser extends AsyncTask<RegistrationInfo, Void, Integer>{


        Context context;

        public RegisterUser(Context context){
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
        protected Integer doInBackground(RegistrationInfo... info){
            try{
                RegistrationInfo userInfo = info[0];
                final String POST_PARAMS = "?name="+userInfo.name+"&city="+userInfo.city+"&street="+userInfo.street+"&house="+userInfo.house+"&address_index="+userInfo.address_index+"&passport_id="+userInfo.passport_id+"&passport_serie="+userInfo.passport_serie+"&date_of_issue="+userInfo.date_of_issue+"&date_of_birth="+userInfo.date_of_birth+"&login="+userInfo.login+"&password="+userInfo.password;
                final String registerUserUrl = context.getString(R.string.SERVER_REGISTER_USER)+POST_PARAMS;
                URL obj = new URL(registerUserUrl);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);
                if(responseCode == HttpURLConnection.HTTP_OK) { //success
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

                    System.out.println(map.get("code").toString());
                    System.out.println(map.get("message").toString());

                    return Integer.parseInt(Objects.requireNonNull(map.get("code")).toString());

                } else {
                    return 1;
                }
            }catch (IOException e) {
                e.printStackTrace();
                return 2;
            }
        }

        @Override
        protected void onPostExecute(Integer status) {
            pDialog.dismiss();
            if (status == 200) {
                Toast.makeText(context, "Успешная регистрация", Toast.LENGTH_LONG).show();
                finish();
            }else Toast.makeText(context, "Ошибка сервера", Toast.LENGTH_LONG).show();

        }
    }
}