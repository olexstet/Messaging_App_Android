package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity_page5 extends AppCompatActivity {

    public DatabaseHelper myDataBase; // Class for editing or request data from the database
    public String contacted_User;
    public String contacted_Date;
    private MessageAdapter messageAdapter; // adapter for the listView
    private ListView messagesView; // listView for displaying messages
    public static final String PREFERENCES_NAME = "dataStorage"; // Name of sharedPreference
    private SharedPreferences mSharedPreferences; // SharedPreferences
    public String username; // Name if the current user



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page5);
    }

    @Override
    protected void onStart() {
        super.onStart();

        contacted_Date = getIntent().getStringExtra("contactDate"); // Get data from previous activity
        contacted_User = getIntent().getStringExtra("contactUser"); // Get contacted username from previous activity

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE); // Initialize sharedPreferences
        username = mSharedPreferences.getString("username","0");   // obtain username from sharedPreferences

        messageAdapter = new MessageAdapter(this);  // Create adapter for handling messages
        messagesView = (ListView) findViewById(R.id.message_view_past);
        messagesView.setAdapter(messageAdapter);  // Add adapter to the listView for displaying messages on the listView

        myDataBase = DatabaseHelper.getInstance(this); // Get SQL database
        Cursor cursor = myDataBase.getMessages(contacted_Date, contacted_User); // Obtain all messages from the database for a certain contacted user and the contacted date
        cursor.moveToFirst(); // Go to first entry
        int count = cursor.getCount(); // Count the number of entries
        while(count > 0){ // If there are some entries in the cursor
            String text = cursor.getString(3); // Get message body
            int from = cursor.getInt(4); // Get integer which indicates to which user the message belong

            if(from == 0){ // If belongs to the current user
                Message message = new Message(text, username, true); // Create new Message and say that the sender is the current user with true
                messageAdapter.add(message); // add message to the adapter
            }
            if(from == 1){ // If message doesn't belong to the current user
                Message message = new Message(text, contacted_User, false); // Create message and indicates with false that it doesn't belong to the current user
                messageAdapter.add(message); // add message to the adapter
            }
            cursor.moveToNext(); // move to next entry (or raw)
            count -= 1; // reduce count by 1 in order to indicate that we processed one entry (raw)
        }
    }

}