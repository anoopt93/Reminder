package com.example.sofsis.remindercustomer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItems extends AppCompatActivity {

    String Phone,Password,Reg_Date,SoftwareId,Name;
    TextView SoftwareID,Item;
    EditText ETItem,ETDate;
    Button BtnAdd,BtnClear;
    int year,month,date;
    Date date_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SoftwareID=(TextView)findViewById(R.id.softID);
        Item = (TextView)findViewById(R.id.item);
        ETItem=(EditText)findViewById(R.id.itemname);
        ETDate=(EditText)findViewById(R.id.date);
        BtnAdd=(Button)findViewById(R.id.btAdd);
        BtnClear=(Button)findViewById(R.id.btClear);

        SharedPreferences prefCustomer =getSharedPreferences("Customer_Details",MODE_PRIVATE);
        Phone = prefCustomer.getString("Phone", "");
        Password = prefCustomer.getString("Password", "");
        Reg_Date = prefCustomer.getString("Reg_Date","");
        SoftwareId = prefCustomer.getString("Software_Id","");
        Name=prefCustomer.getString("Name","");

        SoftwareID.setText(SoftwareId);
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date_reg = (Date) formatter.parse(Reg_Date);
            java.sql.Timestamp timeStampDate = new Timestamp(date_reg.getTime());
            System.out.println("..................................................................");
            System.out.println(date_reg);
            System.out.println(timeStampDate);
            System.out.println(date_reg.getTime());
            System.out.println("..................................................................");
        } catch (Exception e){
        }

        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETItem.setText(null);
                ETDate.setText(null);
            }
        });


        ETDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final DatePickerDialog  datePickerDialog=new DatePickerDialog(AddItems.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = i2 + "-" + (++i1) + "-" + i;
                        ETDate.setText(date);

                    }
                },year,month,date);
                //12 * 30 * 24 * 60 * 60 * 1000L
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime()+1* 11 * 24 * 60 * 60 * 1000L);
                //datePickerDialog.getDatePicker().setMaxDate(new Date().getTime()+ 5 * 12 * 30 * 24 * 60 * 60 * 1000L);
                datePickerDialog.getDatePicker().setMaxDate(date_reg.getTime()+ 5 * 12 * 30 * 24 * 60 * 60 * 1000L);
                datePickerDialog.show();
            }
        });

        AsyncDataClassGetItemsRemaining asyncRequestObject2 = new AsyncDataClassGetItemsRemaining(this,Item );
        asyncRequestObject2.execute(Phone);

    }
    public void ADDITEMS(View view){

        //String SoftwareId= SoftwareID.getText().toString();
        String Phone_no=Phone;
        String Item = ETItem.getText().toString();
        String Date = ETDate.getText().toString();
        if(Item.equals("")){
            Toast.makeText(AddItems.this,"Enter Item Name",Toast.LENGTH_SHORT).show();
        }else if(Date.equals(""))
        {
            Toast.makeText(AddItems.this,"Enter Date",Toast.LENGTH_SHORT).show();
        }
        else {

            AsyncDataClassAddItem asyncRequestObject = new AsyncDataClassAddItem(this);
            asyncRequestObject.execute(SoftwareId, Phone_no, Item, Date,Name);

            //Intent refresh = new Intent(this, AddItems.class);
            //startActivity(refresh);//Start the same Activity
            //finish(); //finish Activity.
        }
    }

}

class AsyncDataClassAddItem extends AsyncTask<String, String, String> {

    private Context context;
    private ProgressDialog pdia;

    public AsyncDataClassAddItem(Context context)
    {
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
            String softwareID = (String)arg0[0];
            String phone = (String)arg0[1];
            String item = (String)arg0[2];
            String date= (String)arg0[3];
            String name = (String)arg0[4];

            //String link="http://192.168.1.2/Reminder/Customer/AddItem.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/AddItem.php";
            String data  = URLEncoder.encode("softwareID", "UTF-8") + "=" +
                    URLEncoder.encode(softwareID, "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("item", "UTF-8") + "=" +
                    URLEncoder.encode(item, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" +
                    URLEncoder.encode(date, "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(name, "UTF-8");


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
            Toast.makeText(context," 1 Item Added Successfully ",Toast.LENGTH_SHORT).show();
            System.out.println(result);
            Intent refresh = new Intent(context, AddItems.class);
            context.startActivity(refresh);//Start the same Activity
            //finish(); //finish Activity.
            ((Activity)context).finish();
        }
        else if(result.equals("Cannot Add more than 100 Items")){
            Toast.makeText(context," Cannot Add more than 100 Items ",Toast.LENGTH_SHORT).show();
            System.out.println(result);
        }
        else
        {
            System.out.println(result);
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        }


    }

}

class AsyncDataClassGetItemsRemaining extends AsyncTask<String, String, String> {

    private Context context;
    private TextView Item_remaining;
    private ProgressDialog pdia;

    public AsyncDataClassGetItemsRemaining(Context context,TextView Item_remaining)
    {
        this.context = context;
        this.Item_remaining=Item_remaining;
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

            //String link="http://192.168.1.2/Reminder/Customer/ItemRemaining.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/ItemRemaining.php";
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
        if(result.equals("table does not exist")) {
            this.Item_remaining.setText("50");
            System.out.println(result);
        }
        else
        {
            System.out.println(result);
            this.Item_remaining.setText(result);

        }


    }

}
