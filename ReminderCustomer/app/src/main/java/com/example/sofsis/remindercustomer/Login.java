package com.example.sofsis.remindercustomer;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

import static android.content.Context.MODE_PRIVATE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Login extends AppCompatActivity {

    static EditText PhoneField;
    static EditText PasswordField;
    Button Reset;
    String Phone,Password;
    TextView Register_Here,Forget_Pass;
    ProgressDialog pdia;

    String myJSON;
    private static final String TAG_STATUS="Status" ;
    private static final String TAG_REGDATE="Reg_Date";
    private static final String TAG_SOFTWAREID="SoftwareId";
    private static final String TAG_NAME="Name";
    private static final String TAG_RESULTS="result";
    JSONArray peoples = null;
    String Status,Software_Id,Reg_Date,CName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PhoneField=(EditText)findViewById(R.id.phone);
        PasswordField=(EditText)findViewById(R.id.password);
        Reset=(Button)findViewById(R.id.reset);
        Register_Here=(TextView)findViewById(R.id.regHere);
        Forget_Pass=(TextView)findViewById(R.id.forget);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneField.setText(null);
                PasswordField.setText(null);
            }
        });

        SharedPreferences prefs = getSharedPreferences("Registration", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();editor.putString("Registered", "1");
        editor.commit();

    }
    public void Forget(View view){
        Intent i = new Intent(Login.this, ForgetPassword.class);
        startActivity(i);
    }
    public void regHere(View view){
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
        Login.this.finish();
    }
    public void Login(View view){
         Phone = PhoneField.getText().toString();
         Password = PasswordField.getText().toString();

        if("".equals(Phone)){
            PhoneField.setError("Enter Phone Number");
        }
        else if("".equals(Password)){
            PasswordField.setError("Enter Password");
        }
        else {
        //AsyncDataClass2 asyncRequestObject = new AsyncDataClass2(this,PhoneField,PasswordField);
        //asyncRequestObject.execute(phone, password);
            getData();
        }
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int j = 0; j < peoples.length(); j++) {
                JSONObject d = peoples.getJSONObject(j);
                Status = d.getString(TAG_STATUS);
            }
            if(Status.equals("Login Failed")){
                pdia.dismiss();
                Toast.makeText(Login.this,"Invalid Phone Or Password",Toast.LENGTH_SHORT).show();
            }
            else if(Status.equals("0")){

                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    Status = c.getString(TAG_STATUS);
                    Reg_Date = c.getString(TAG_REGDATE);
                    Software_Id = c.getString(TAG_SOFTWAREID);
                    CName=c.getString(TAG_NAME);
                }

                SharedPreferences pre = getSharedPreferences("Customer_Details", MODE_PRIVATE);
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("Phone", PhoneField.getText().toString());
                editor.putString("Password",PasswordField.getText().toString());
                editor.putString("Reg_Date",Reg_Date);
                editor.putString("Software_Id",Software_Id);
                editor.putString("Name",CName);
                editor.commit();

                pdia.dismiss();
                Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, Home.class);
                startActivity(i);
                Login.this.finish();
            }
            else {
                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    Status = c.getString(TAG_STATUS);
                    Reg_Date = c.getString(TAG_REGDATE);
                    Software_Id = c.getString(TAG_SOFTWAREID);
                    CName=c.getString(TAG_NAME);
                }

                SharedPreferences pre = getSharedPreferences("Customer_Details", MODE_PRIVATE);
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("Phone", PhoneField.getText().toString());
                editor.putString("Password",PasswordField.getText().toString());
                editor.putString("Reg_Date",Reg_Date);
                editor.putString("Software_Id",Software_Id);
                editor.putString("Name",CName);
                editor.commit();

                pdia.dismiss();

                Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, Main2Activity.class);
                startActivity(i);
                Login.this.finish();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getData() {

        class AsyncDataClassLogin extends AsyncTask<String, String, String> {
           // private ProgressDialog pdia;
            protected void onPreExecute() {
                super.onPreExecute();
                pdia = new ProgressDialog(Login.this);
                pdia.setMessage("Loading...");
                pdia.show();
            }

            @Override
            protected String doInBackground(String... arg0) {

                try {
                    String phone = (String) arg0[0];
                    String password = (String) arg0[1];

                    //String link = "http://192.168.1.2/Reminder/Customer/ViewItem.php";
                    String link="http://www.sofsisindia.com/Reminder/Customer/Login.php";
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
                //pdia.dismiss();

            }
        }
        AsyncDataClassLogin g = new AsyncDataClassLogin();
        g.execute(Phone,Password);

    }
}
