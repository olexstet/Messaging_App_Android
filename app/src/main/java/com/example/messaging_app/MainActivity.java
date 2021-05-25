package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;


import android.os.Bundle;

import java.util.function.Predicate;

public class MainActivity extends AppCompatActivity {

    private Button button;
    public static final String PREFERENCES_NAME = "dataStorage"; // Name of preference database
    private SharedPreferences mSharedPreferences; // sharedPreference variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE); // initialize shared preferences
        boolean exist_username = mSharedPreferences.contains("username"); // Check if username field exists in sharedPreferences

        // Start Messaging button
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                //If username field exists go to menu
                if(exist_username){
                    openActivity2();
                }
                else{
                    // If username doesn't exist go to register page
                    openActivity_Register();
                }
                finish();

            }
        });

    }

    // Pass to MainActivity_page2
    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity_page2.class); // Create intent for MainAtivity_page2
        startActivity(intent); // start activity
    }

    // Pass to register page (MainActivity_register)
    public void openActivity_Register(){
        Intent intent = new Intent(this, MainActivity_register.class);
        startActivity(intent);
    }


}