package com.example.sofsis.remindercustomer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Home extends AppCompatActivity {
    TextView TxtPhone,TxtPassword,TxtSoftwareID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences pref = getSharedPreferences("Customer_Details", MODE_PRIVATE);
        final String Phone = pref.getString("Phone","");
        final String SoftwareID = pref.getString("Software_Id","");
        final String Reg_Date = pref.getString("Reg_Date","");

        TxtPhone=(TextView)findViewById(R.id.textviewPhone);
        TxtSoftwareID=(TextView)findViewById(R.id.textviewSoftID);

        TxtPhone.setText(Phone);
        TxtSoftwareID.setText(SoftwareID);

        SharedPreferences prefs = getSharedPreferences("LoggedIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Login", "0");
        editor.commit();

    }
}