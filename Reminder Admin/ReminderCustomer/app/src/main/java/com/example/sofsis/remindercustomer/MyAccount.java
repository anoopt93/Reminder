package com.example.sofsis.remindercustomer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyAccount extends AppCompatActivity {

    TextView Name,Email,Phone,Password,SoftwareID;
    String name,email,phone,password,softid;
    String strPhone,strPassword;
    String myJSON;
    private static final String TAG_Name="Customer_Name" ;
    private static final String TAG_Email="Email";
    private static final String TAG_Phone="Phone";
    private static final String TAG_Password="Password";
    private static final String TAG_SoftID="Verification";

    private static final String TAG_RESULTS="result";
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Name=(TextView)findViewById(R.id.txtName);
        Email=(TextView)findViewById(R.id.txtEmail);
        Phone=(TextView)findViewById(R.id.txtPhone);
        SoftwareID=(TextView)findViewById(R.id.txtSoftwareid);

        SharedPreferences prefCustomer =getSharedPreferences("Customer_Details",MODE_PRIVATE);
        strPhone = prefCustomer.getString("Phone", "");
        strPassword = prefCustomer.getString("Password", "");
        getData();
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                name = c.getString(TAG_Name);
                email = c.getString(TAG_Email);
                phone = c.getString(TAG_Phone);
                password = c.getString(TAG_Password);
                softid = c.getString(TAG_SoftID);
            }
            Name.setText(name);
            Email.setText(email);
            Phone.setText(phone);
            SoftwareID.setText(softid);

            System.out.println(name);
            System.out.println(email);
            System.out.println(phone);
            System.out.println(password);
            System.out.println(softid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void getData() {

        class AsyncDataClassDisplayItems extends AsyncTask<String, String, String> {
            private ProgressDialog pdia;
            protected void onPreExecute() {
                super.onPreExecute();
                pdia = new ProgressDialog(MyAccount.this);
                pdia.setMessage("Loading...");
                pdia.show();
            }

            @Override
            protected String doInBackground(String... arg0) {

                try {
                    String phone = (String) arg0[0];
                    String password= (String)arg0[1];

                    //String link = "http://192.168.1.2/Reminder/Customer/Profile.php";
                    String link="http://www.sofsisindia.com/Reminder/Customer/Profile.php";
                    String data = URLEncoder.encode("phone", "UTF-8") + "=" +
                            URLEncoder.encode(phone, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                            URLEncoder.encode(password, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    System.out.println(data);

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();


                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    //System.out.println(sb);
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            protected void onPostExecute(String result) {

                System.out.println(result);

                myJSON=result;
                showList();
                pdia.dismiss();

            }
        }
        AsyncDataClassDisplayItems g = new AsyncDataClassDisplayItems();
        g.execute(strPhone,strPassword);

    }
}
