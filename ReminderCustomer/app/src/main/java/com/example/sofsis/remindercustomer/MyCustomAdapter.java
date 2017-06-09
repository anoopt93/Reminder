package com.example.sofsis.remindercustomer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SOFSIS on 03/29/2017.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
   private Context context;
    String ID,Item,Date,Phone;

    public MyCustomAdapter(ArrayList<HashMap<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        //Handle TextView and display string from your list
       // TextView txtID = (TextView)view.findViewById(R.id.Id);
        TextView txtItem = (TextView)view.findViewById(R.id.Item);
        TextView txtDate = (TextView)view.findViewById(R.id.Date);
        //String Value = getItem(position).toString();

        HashMap<String, Object> obj = (HashMap<String, Object>)(getItem(position));
        String ID = (String) obj.get("ID");
        String Item = (String) obj.get("Item");
        String Date = (String) obj.get("Date");

        //txtID.setText(ID);
        txtItem.setText(Item);
        txtDate.setText(Date);

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                HashMap<String, Object> obj = (HashMap<String, Object>)(getItem(position));
                final String ID = (String) obj.get("ID");
                final String Item = (String) obj.get("Item");

                /*
                SharedPreferences prefCustomer =context.getSharedPreferences("Customer_Details",MODE_PRIVATE);
                Phone = prefCustomer.getString("Phone", "");
                Context ctx = parent.getContext();

               AsyncDataClassCheckDate asyncRequestObject = new AsyncDataClassCheckDate(ctx,Phone,ID,Item);
               asyncRequestObject.execute(Phone,ID,Item);
               */

                //list.remove(position); //or some other task
                //notifyDataSetChanged();

                final Context ctx = parent.getContext();
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ctx,"Button Pressed",Toast.LENGTH_SHORT).show();
                        SharedPreferences prefCustomer =context.getSharedPreferences("Customer_Details",MODE_PRIVATE);
                        Phone = prefCustomer.getString("Phone", "");
                        AsyncDataClassCheckDate asyncRequestObject = new AsyncDataClassCheckDate(ctx,Phone,ID,Item);
                        asyncRequestObject.execute(Phone,ID,Item);

                        //list.remove(position);
                        //notifyDataSetChanged();

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });
        return view;
    }


}
class AsyncDataClassCheckDate extends AsyncTask<String, String, String> {

    private Context ctx;
    private String phone,ID,item;
    private ProgressDialog pdia;

    public AsyncDataClassCheckDate(Context ctx, String phone, String ID, String item) {
        this.ctx=ctx;
        this.phone=phone;
        this.ID=ID;
        this.item=item;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        pdia = new ProgressDialog(ctx);
        pdia.setMessage("Loading...");
        pdia.show();
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            String phone = (String) arg0[0];
            String id = (String) arg0[1];
            String item = (String) arg0[2];

            //String link = "http://192.168.1.2/Reminder/Customer/CheckDate.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/CheckDate.php";
            String data = URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("item", "UTF-8") + "=" +
                    URLEncoder.encode(item, "UTF-8");


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

        pdia.dismiss();
        System.out.println(result);
        if(result.equals("False")) {
            Toast.makeText(ctx, "Item Cannot Delete. Alert already started !", Toast.LENGTH_SHORT).show();
        }
        else if(result.equals("True")) {
            AsyncDataClassDeleteItems asyncRequestObject = new AsyncDataClassDeleteItems(ctx);
            asyncRequestObject.execute(this.phone,this.ID,this.item);

        }


    }
}
class AsyncDataClassDeleteItems extends AsyncTask<String, String, String> {
    private  Context ctx;
    private ProgressDialog pdia;

    public AsyncDataClassDeleteItems(Context ctx) {
        this.ctx=ctx;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        pdia = new ProgressDialog(ctx);
        pdia.setMessage("Loading...");
        pdia.show();
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            String phone = (String) arg0[0];
            String id = (String) arg0[1];
            String item = (String) arg0[2];

            //String link = "http://192.168.1.2/Reminder/Customer/DeleteItem.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/DeleteItem.php";
            String data = URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("item", "UTF-8") + "=" +
                    URLEncoder.encode(item, "UTF-8");

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

        pdia.dismiss();
        System.out.println(result);
        Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();

        Intent refresh = new Intent(ctx, ViewItems.class);
        ctx.startActivity(refresh);
        ((Activity)ctx).finish();


    }
}
