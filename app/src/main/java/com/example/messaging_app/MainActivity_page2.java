package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;

import org.w3c.dom.Text;


public class MainActivity_page2 extends AppCompatActivity {

    // Button's names
    private Button button_contacts;
    private Button button_find_contacts;

    private TextView username_string; // Used for displaying username
    public static final String PREFERENCES_NAME = "dataStorage"; // Name of sharedPreferences
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page2); // set layout

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE); // Initialize sharedPreferences by finding the sharedPreference file with the PREFERENCES_NAME

        // Display username
        username_string = (TextView) findViewById(R.id.username_text); // fetch TextView
        String username = mSharedPreferences.getString("username","0"); // Get username of the user
        username_string.setText(username); // set in textView the username


        // Pass to MainActivity_page3 (Hostory)
        button_contacts = (Button)findViewById(R.id.button_contacts);
        button_contacts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                openActivity3();
            }
        });

        // Pass to MainActivity_page4 (Chating with other users)
        button_find_contacts = (Button)findViewById(R.id.button_find_contacts);
        button_find_contacts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                openActivity4();
            }
        });


    }



    // Open new activity MainActivity_page3
    public void openActivity3(){
        Intent intent = new Intent(this, MainActivity_page3.class);
        startActivity(intent);
    }

    // Open new activity MainActivity_page4
    public void openActivity4(){
        Intent intent = new Intent(this, MainActivity_page4.class);
        startActivity(intent);
    }


}