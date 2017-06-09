package com.example.sofsis.remindercustomer;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

public class Register extends AppCompatActivity {


    static EditText CustomerNameField;
    static EditText PhoneField;
    //static EditText EmailField;
    int SoftwareID;
    String email;
    Button Reset;
    TextView LoginHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CustomerNameField=(EditText)findViewById(R.id.cname);
        PhoneField=(EditText)findViewById(R.id.phone);
        //EmailField=(EditText)findViewById(R.id.email);
        Reset =(Button)findViewById(R.id.reset);
        LoginHere=(TextView)findViewById(R.id.loginHere);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerNameField.setText(null);
                PhoneField.setText(null);
               // EmailField.setText(null);
            }
        });

        LoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                Register.this.finish();
            }
        });

        //############################ AUTOFILL EMAIL ID WITH PRIMARY ACCOUNTT ####################################
        String possibleEmail = "";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
       // EmailField.setText(possibleEmail);
        email=possibleEmail;

    }

    public void RegisterPost(View view){

        String cname = CustomerNameField.getText().toString();
        String phone = PhoneField.getText().toString();
       // String email = EmailField.getText().toString();
        String OTP =""+((int)(Math.random()*9000)+1000);
        Random rnd = new Random();
        SoftwareID = 100000 + rnd.nextInt(900000);
        String verification= String.valueOf(SoftwareID);
        //String verification=""+((int)(Math.random()*9000)+1000);
        if("".equals(cname)){
            CustomerNameField.setError("Enter name");
        }
        else if("".equals(phone)){
            PhoneField.setError("Enter Mobile Number");
        }
        else if(phone.length()!=10){
            PhoneField.setError("Enter 10 digit Mobile number");
        }
        else if(!validation.isPhoneValid(phone)){
            PhoneField.setError("Invalid Mobile Number");
        }
        else {
            final CheckBox checkBox = new CheckBox(this);
            checkBox.setText("agree");
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
            linearLayout.setOrientation(1);
            linearLayout.addView(checkBox);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(linearLayout);
            alertDialogBuilder.setTitle("License agreement");
            alertDialogBuilder.setMessage("1. This is the message of alert dialog \n" +
                    "2. ssdfsdfsdfsd sdfs dfsdfsdfsdf sdfsdfsdf sdfsd fs \n"+
            "3. asdad a asdasdasd asdasda sdasdasda sadasd asdad \n"+
            "4. App validity is 5 years from the date of registration \n" +

                    "\nAgree the terms to continue with registration.");

            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    if( checkBox.isChecked()){
                        Toast.makeText(Register.this,"Registration Successfull",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Register.this,"Registration Failed",Toast.LENGTH_SHORT).show();


                    }
                }
            });
            alertDialogBuilder.show();

            //AsyncDataClass asyncRequestObject = new AsyncDataClass(this,phone,OTP);
            //asyncRequestObject.execute(cname, phone, email, OTP, verification);
        }
    }
}

class AsyncDataClass extends AsyncTask<String, String, String> {

    private Context context;
    String phone,OTP;
    private ProgressDialog pdia;

    public AsyncDataClass(Context context, String phone, String OTP) {
        this.context = context;
        this.phone=phone;
        this.OTP=OTP;

    }
    protected void onPreExecute(){
        super.onPreExecute();
        pdia = new ProgressDialog(context);
        pdia.setMessage("Loading...");
        pdia.show();
    }
    @Override
    protected String doInBackground(String... arg0) {

        try{
            String customername = (String)arg0[0];
            String phone = (String)arg0[1];
            String email = (String)arg0[2];
            String password= (String)arg0[3];
            String verification= (String)arg0[4];

            //String link="http://192.168.1.2/Reminder/Customer/Registration.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/Registration.php";
            String data  = URLEncoder.encode("customername", "UTF-8") + "=" +
                    URLEncoder.encode(customername, "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("verification", "UTF-8") + "=" +
                    URLEncoder.encode(verification, "UTF-8");


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
        if(result.equals("Success")) {
            //Toast.makeText(context,"Registration Successful",Toast.LENGTH_SHORT).show();
            //Toast.makeText(context,"Password Send to your Mobile Number",Toast.LENGTH_SHORT).show();

            SharedPreferences prefs = context.getSharedPreferences("Registration", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Registered", "1");
            editor.commit();

            AsyncDataClassSendOTP asyncRequestObject = new AsyncDataClassSendOTP(context,phone,OTP);
            asyncRequestObject.execute(this.phone,this.OTP);

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Success !");
            alert.setMessage("Registration Succesfull. Password send to your Mobile Number");
            alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    ((Activity)context).finish();

                }
            });
            alert.show();
        }
        else if(result.equals("Phone number exist")){
            Toast.makeText(context,"Mobile Number already exist Try another number",Toast.LENGTH_SHORT).show();
        }
        else if(result.equals("Email exist")){
            Toast.makeText(context,"Email address already exist. Try another Email",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Registration Failed",Toast.LENGTH_SHORT).show();
            System.out.println(result);
        }

    }

}

class AsyncDataClassSendOTP extends AsyncTask<String, String, String> {

    private Context context;
    String phone,OTP;

    public AsyncDataClassSendOTP(Context context, String phone, String OTP) {
        this.context = context;
        this.phone=phone;
        this.OTP=OTP;

    }
    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... arg0) {

        try{
            String phone = (String)arg0[0];
            String otp = (String)arg0[1];


            //String link="http://192.168.1.2/Reminder/sms.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/SendOTP.php";
            String data  = URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("otp", "UTF-8") + "=" +
                    URLEncoder.encode(otp, "UTF-8");

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
       System.out.println(result);

    }

}

