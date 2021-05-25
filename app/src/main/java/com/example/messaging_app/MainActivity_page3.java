package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity_page3 extends AppCompatActivity {

    public DatabaseHelper myDataBase; // Database
    private ListView message_list;  // List view for displaying contacted users and the contacted time
    public String contacted_Username = "";
    public String contacted_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page3);

        myDataBase = DatabaseHelper.getInstance(this); // Fetch the sql database
        message_list = (ListView)findViewById(R.id.messageListView); // listView for items
        Cursor cursor = myDataBase.getDateAndSendUser(); // Get all date and sender by using getDateAndSendUser function from DatabaseHelper class
        ArrayList<String> data = new ArrayList<>(); // Array for taking data from the cursor
        while(cursor.moveToNext()){ // While cursor not at the end
            String info = cursor.getString(1) + ": " + cursor.getString(0); // Get username and date and put them into a format as user: date
            data.add(info); // Add string to array
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data); // Create adapter for the array, in order to display the data in the listView
        message_list.setAdapter(adapter); // Add to the listView an adapter

        //When user select an item, select the username and date for next activity
        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String text = adapterView.getItemAtPosition(position).toString();
                Boolean record_contacted_username = false;
                int count = 0;

                //Go through the string in order to fetch date and contacted username
                for(int i = 0; i < text.length(); i++){
                    // Obtain the date
                    if(record_contacted_username == true && count > 0){ // count > 0 because first character will be a white space
                        contacted_date += text.charAt(i);
                    }

                    //If contacted username obtain, set count to 1 in order to obtain the date
                    if(record_contacted_username == true){
                        count += 1;
                    }
                    // When ":" contacted username is obtained
                    if(text.charAt(i) == ":".charAt(0)){
                        record_contacted_username = true;
                    }
                    // While not ":" contacted characters in order to obtain the username.
                    if(record_contacted_username == false){
                        contacted_Username += text.charAt(i);
                    }
                }
                openActivity5(); // Go to new activity for displaying the messages
            }
        });
    }


    // Go to MainActivity_page5
    public void openActivity5(){
        Intent intent = new Intent(this, MainActivity_page5.class); // create new intent
        intent.putExtra("contactDate",contacted_date); // pass contacted date to next activity
        intent.putExtra("contactUser",contacted_Username);  // pass contacted username
        contacted_Username = ""; // Set string to empty in order to be able to fetch again the contacted username and date
        contacted_date = "";
        startActivity(intent); // Start new activity

    }



}

