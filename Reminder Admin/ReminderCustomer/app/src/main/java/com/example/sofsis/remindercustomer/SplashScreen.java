package com.example.sofsis.remindercustomer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (AppStatus.getInstance(SplashScreen.this).isOnline()) {

           // Toast.makeText(this, "You are online!!!!", Toast.LENGTH_SHORT).show();

            SharedPreferences prefs = getSharedPreferences("Registration", MODE_PRIVATE);
            final String string = prefs.getString("Registered", "");

            SharedPreferences prefs2 = getSharedPreferences("LoggedIn", MODE_PRIVATE);
            final String string2 = prefs2.getString("Login", "");


            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    if(string.equals("1")&& string2.equals("1"))
                    {
                        Intent i = new Intent(SplashScreen.this, Main2Activity.class);
                        startActivity(i);
                    }
                    else if (string.equals("1")){
                        Intent i = new Intent(SplashScreen.this, Login.class);
                        startActivity(i);
                    }
                    else if (string2.equals("0")){
                        Intent i = new Intent(SplashScreen.this, Login.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(SplashScreen.this, Register.class);
                        startActivity(i);
                    }
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            Toast.makeText(this, "You are not online!!!!", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Connect to internet and Try again.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
