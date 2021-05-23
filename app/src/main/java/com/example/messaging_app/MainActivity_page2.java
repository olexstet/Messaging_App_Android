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

    private Button button_contacts;
    private Button button_find_contacts;
    private TextView username_string;
    public static final String PREFERENCES_NAME = "dataStorage";
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page2);

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        username_string = (TextView) findViewById(R.id.username_text);
        String username = mSharedPreferences.getString("username","0");
        username_string.setText(username);



        button_contacts = (Button)findViewById(R.id.button_contacts);
        button_contacts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                openActivity3();
            }
        });

        button_find_contacts = (Button)findViewById(R.id.button_find_contacts);
        button_find_contacts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                openActivity4();
            }
        });

    }




    public void openActivity3(){
        Intent intent = new Intent(this, MainActivity_page3.class);
        startActivity(intent);
    }

    public void openActivity4(){
        Intent intent = new Intent(this, MainActivity_page4.class);
        intent.putExtra("startAdvert",false);
        startActivity(intent);
    }


}