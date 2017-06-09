package com.example.sofsis.remindercustomer;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.list;


public class ViewItems extends AppCompatActivity {

    TextView txtID,txtItem,txtDate;
    ImageButton BtnDelete;

    String myJSON;
    private static final String TAG_ID="ID" ;
    private static final String TAG_ITEM="Item";
    private static final String TAG_DATE="Date";
    private static final String TAG_RESULTS="result";
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
   // ListView list;

    String Phone,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //txtID=(TextView) findViewById(R.id.Id);
        txtItem=(TextView) findViewById(R.id.Item);
        txtDate=(TextView) findViewById(R.id.Date);
        BtnDelete=(ImageButton) findViewById(R.id.delete_btn);

       // list = (ListView) findViewById(R.id.list_view);
        personList = new ArrayList<HashMap<String,String>>();

        SharedPreferences prefCustomer =getSharedPreferences("Customer_Details",MODE_PRIVATE);
        Phone = prefCustomer.getString("Phone", "");
        Password = prefCustomer.getString("Password", "");
        getData();

    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_ITEM);
                String address = c.getString(TAG_DATE);

                HashMap<String,String> persons = new HashMap<String,String>();

                persons.put(TAG_ID,id);
                persons.put(TAG_ITEM,name);
                persons.put(TAG_DATE,address);

                personList.add(persons);

            }
            /*
             ListAdapter adapter = new SimpleAdapter(
                    ViewItems.this, personList, R.layout.list_item,
                    new String[]{TAG_ID,TAG_ITEM,TAG_DATE},
                    new int[]{R.id.Id, R.id.Item, R.id.Date}
            );
            list.setAdapter(adapter);
            */

            //instantiate custom adapter
            MyCustomAdapter adapter = new MyCustomAdapter(personList, this);

            //handle listview and assign adapter
            ListView lView = (ListView)findViewById(R.id.list_view);
            lView.setAdapter(adapter);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getData() {

        class AsyncDataClassDisplayItems extends AsyncTask<String, String, String> {
            private ProgressDialog pdia;
            protected void onPreExecute() {
                super.onPreExecute();
                pdia = new ProgressDialog(ViewItems.this);
                pdia.setMessage("Loading...");
                pdia.show();
            }

            @Override
            protected String doInBackground(String... arg0) {

                try {
                    String phone = (String) arg0[0];

                    //String link = "http://192.168.1.2/Reminder/Customer/ViewItem.php";
                    String link="http://www.sofsisindia.com/Reminder/Customer/ViewItem.php";
                    String data = URLEncoder.encode("phone", "UTF-8") + "=" +
                            URLEncoder.encode(phone, "UTF-8");


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
        g.execute(Phone);

    }

}



