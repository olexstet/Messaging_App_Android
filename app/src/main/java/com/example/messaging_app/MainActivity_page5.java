package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity_page5 extends AppCompatActivity {

    public DatabaseHelper myDataBase;
    public String contacted_User;
    public String contacted_Date;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    public static final String PREFERENCES_NAME = "dataStorage";
    private SharedPreferences mSharedPreferences;
    public String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page5);
    }

    @Override
    protected void onStart() {
        super.onStart();

        contacted_Date = getIntent().getStringExtra("contactDate");
        contacted_User = getIntent().getStringExtra("contactUser");
        System.out.println(contacted_Date +" " + contacted_User);
        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        username = mSharedPreferences.getString("username","0");

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.message_view_past);
        messagesView.setAdapter(messageAdapter);

        myDataBase = DatabaseHelper.getInstance(this);
        Cursor cursor = myDataBase.getMessages(contacted_Date, contacted_User);
        cursor.moveToFirst();
        int count = cursor.getCount();
        while(count > 0){
            String text = cursor.getString(3);
            System.out.println(text);
            int from = cursor.getInt(4);
            System.out.println(from);

            if(from == 0){
                Message message = new Message(text, username, true);
                messageAdapter.add(message);
            }
            if(from == 1){
                Message message = new Message(text, contacted_User, false);
                messageAdapter.add(message);
            }
            cursor.moveToNext();
            count -= 1;
        }
    }

}