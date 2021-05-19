package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;

import android.os.Bundle;

public class MainActivity_register extends AppCompatActivity {

    private Button button_register;
    private EditText username_text;
    public static final String PREFERENCES_NAME = "dataStorage";
    private SharedPreferences mSharedPreferences;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        myDB = new DatabaseHelper(this);
        System.out.println("Created");

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        username_text = (EditText)findViewById(R.id.etUsername);
        button_register = (Button)findViewById(R.id.button_register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("username", username_text.getText().toString());
                editor.commit();
                openActivity2();
                finish();
            }
        });
    }

    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity_page2.class);
        startActivity(intent);
    }

}