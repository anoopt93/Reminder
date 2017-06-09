package com.example.sofsis.remindercustomer;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

public class ForgetPassword extends AppCompatActivity {

    String phone;
    EditText PhoneField;
    Button Send,Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        PhoneField=(EditText)findViewById(R.id.mobno);
        Send=(Button)findViewById(R.id.send);
        Reset=(Button)findViewById(R.id.reset);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneField.setText(null);
            }
        });
    }

    public void SEND(View view){

        phone = PhoneField.getText().toString();

        if("".equals(phone)){
            PhoneField.setError("Enter Mobile Number");
        }
        else if(phone.length()!=10){
            PhoneField.setError("Enter 10 digit Mobile number");
        }
        else if(!validation.isPhoneValid(phone)){
            PhoneField.setError("Invalid Mobile Number");
        }
        else {
            AsyncDataClassForgetPass asyncRequestObject = new AsyncDataClassForgetPass(this);
            asyncRequestObject.execute(phone);
        }
    }
}
class AsyncDataClassForgetPass extends AsyncTask<String, String, String> {

    private Context context;
    private ProgressDialog pdia;

    public AsyncDataClassForgetPass(Context context) {
        this.context = context;

    }
    protected void onPreExecute(){
        super.onPreExecute();
        pdia = new ProgressDialog(context);
        pdia.setMessage("Loading...");
        pdia.setCancelable(false);
        pdia.show();
    }
    @Override
    protected String doInBackground(String... arg0) {

        try{
            String phone = (String)arg0[0];

            //String link="http://192.168.1.2/Reminder/Customer/Registration.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/ForgetPassword.php";
            String data  = URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            System.out.println(data);

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            System.out.println(sb);
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected void onPostExecute(String result){
        pdia.dismiss();
        if(result.equals("Failed")) {
            Toast.makeText(context,"Invalid Mobile Number ",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Password Send to your mobile Number ",Toast.LENGTH_SHORT).show();
            System.out.println(result);
        }
    }
}