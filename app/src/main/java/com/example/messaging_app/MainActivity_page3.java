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

    public DatabaseHelper myDataBase;
    private ListView message_list;
    public String contacted_Username = "";
    public String contacted_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page3);

        myDataBase = DatabaseHelper.getInstance(this);
        message_list = (ListView)findViewById(R.id.messageListView);
        Cursor cursor = myDataBase.getDateAndSendUser();
        ArrayList<String> data = new ArrayList<>();
        while(cursor.moveToNext()){
            String info = cursor.getString(1) + ": " + cursor.getString(0);
            data.add(info);
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        message_list.setAdapter(adapter);

        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String text = adapterView.getItemAtPosition(position).toString();
                Boolean record_contacted_username = false;
                int count = 0;
                for(int i = 0; i < text.length(); i++){
                    if(record_contacted_username == true && count > 0){ // count > 0 because first character will be a white space
                        contacted_date += text.charAt(i);
                    }
                    if(record_contacted_username == true){
                        count += 1;
                    }
                    if(text.charAt(i) == ":".charAt(0)){
                        record_contacted_username = true;
                    }
                    if(record_contacted_username == false){
                        contacted_Username += text.charAt(i);
                    }
                }
                openActivity5();
            }
        });
    }


    public void openActivity5(){
        Intent intent = new Intent(this, MainActivity_page5.class);
        intent.putExtra("contactDate",contacted_date);
        intent.putExtra("contactUser",contacted_Username);
        contacted_Username = "";
        contacted_date = "";
        startActivity(intent);

    }



}

