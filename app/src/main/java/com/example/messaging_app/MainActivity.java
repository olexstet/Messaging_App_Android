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
    public static final String PREFERENCES_NAME = "dataStorage";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean exist_username = mSharedPreferences.contains("username");

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                if(exist_username){
                    openActivity2();
                }
                else{
                    openActivity_Register();
                }
                finish();

            }
        });

    }

    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity_page2.class);
        startActivity(intent);
    }

    public void openActivity_Register(){
        Intent intent = new Intent(this, MainActivity_register.class);
        startActivity(intent);
    }


}