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
        setContentView(R.layout.activity_main_register); // set layout
        myDB = new DatabaseHelper(this); // Fetch sql database

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE); // Get shared preferences based on the name
        username_text = (EditText)findViewById(R.id.etUsername); // TextView where the user enter his username

        // Button allow to save the username in shared preferences and then pass to Menu (MainActivity_page2)
        button_register = (Button)findViewById(R.id.button_register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreferences.edit(); // allow editing sharedPreferences
                editor.putString("username", username_text.getText().toString()); // Set data to sharedPreferences with editor
                editor.commit(); // save changes
                openActivity2(); // Pass to MainActivity_page2
                finish();  // finish this activity
            }
        });
    }

    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity_page2.class); // Create new intent for new activity
        startActivity(intent); // start new activity
    }

}