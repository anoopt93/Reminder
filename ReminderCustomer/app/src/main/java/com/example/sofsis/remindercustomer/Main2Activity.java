package com.example.sofsis.remindercustomer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String strPhone, strPassword;
    ArcMenu arcMenuAndroid;
    FloatingActionButton fab1, fab2, fab3, fab4;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Reminder");

        SharedPreferences prefs = getSharedPreferences("LoggedIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Login", "1");
        editor.commit();

        SharedPreferences prefCustomer = getSharedPreferences("Customer_Details", MODE_PRIVATE);
        strPhone = prefCustomer.getString("Phone", "");
        strPassword = prefCustomer.getString("Password", "");


        fab1 = (FloatingActionButton) findViewById(R.id.fab_arc_menu_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_arc_menu_2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_arc_menu_3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab_arc_menu_4);


        arcMenuAndroid = (ArcMenu) findViewById(R.id.arcmenu_android_example_layout);
        arcMenuAndroid.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                //TODO something when menu is opened
                fab1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Main2Activity.this, AddItems.class);
                        startActivity(i);
                    }
                });
                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Main2Activity.this, ViewItems.class);
                        startActivity(i);
                    }
                });
                fab3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Main2Activity.this, MyAccount.class);
                        startActivity(i);
                    }
                });

            }

            @Override
            public void onMenuClosed() {
                //TODO something when menu is closed
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void DeleteAccount(View view) {
        final Context context = this;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete ?");
        alert.setMessage("Are you sure to delete your Account?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                AsyncDataClassDeleteAccount1 asyncRequestObject = new AsyncDataClassDeleteAccount1(context);
                asyncRequestObject.execute(strPhone, strPassword);
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

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap Back again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addItems) {
            Intent i = new Intent(Main2Activity.this, AddItems.class);
            startActivity(i);
        } else if (id == R.id.nav_viewItems) {
            Intent i = new Intent(Main2Activity.this, ViewItems.class);
            startActivity(i);
        } else if (id == R.id.nav_myAccount) {
            Intent i = new Intent(Main2Activity.this, MyAccount.class);
            startActivity(i);
        } else if (id == R.id.nav_website) {
            String url = "http://www.sofsisindia.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_email) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("message/rfc822");
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sofsisindia@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "Important Subject !!");
            email.putExtra(Intent.EXTRA_TEXT, "This is the Body of the Email...");
            try {
                startActivity(Intent.createChooser(email, "Pick an Email Client"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "There are no Email Clients Installed", Toast.LENGTH_SHORT).show();
            }
        } else if ( id ==R.id.nav_call) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:9745156008"));

            if (ActivityCompat.checkSelfPermission(Main2Activity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            startActivity(callIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
class AsyncDataClassDeleteAccount1 extends AsyncTask<String, String, String> {
    private Context context;
    private ProgressDialog pdia;

    public AsyncDataClassDeleteAccount1(Context context)
    {
        this.context= context;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        pdia = new ProgressDialog(context);
        pdia.setMessage("Loading...");
        pdia.show();
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            String phone = (String) arg0[0];
            String password = (String) arg0[1];

            //String link = "http://192.168.1.2/Reminder/Customer/DeleteAccount.php";
            String link="http://www.sofsisindia.com/Reminder/Customer/DeleteAccount.php";
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
        pdia.dismiss();

        System.out.println(result);

        if(result.equals("Deleted Successfully")){

            SharedPreferences prefs = context.getSharedPreferences("Registration", Context.MODE_PRIVATE);
            prefs.edit().clear().commit();
            SharedPreferences prefs2 = context.getSharedPreferences("LoggedIn", Context.MODE_PRIVATE);
            prefs2.edit().clear().commit();
            SharedPreferences prefs3 = context.getSharedPreferences("Customer_Details", Context.MODE_PRIVATE);
            prefs3.edit().clear().commit();

            Toast.makeText(context,"Account Deleted Succesfully",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, Register.class);
            context.startActivity(i);
            ((Activity)context).finish();

        }

    }
}

