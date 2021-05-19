package com.example.messaging_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.DatabaseErrorHandler;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String tableName = "Messages";
    private static final String contacted_Username = "Contacted_Username";
    private static final String idMessage = "idMessage";
    private static final String message_Text = "Message_Text";
    private static final String message_date = "Date";
    private static final String message_Belong = "Message_From";
    private static DatabaseHelper instance;
    private int next_message_id = 0;


    public DatabaseHelper(Context context){
        super(context, tableName,null, 1);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        System.out.println("Instance returned");
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        System.out.println("Removed");
        String createTable = "CREATE TABLE IF NOT EXISTS " + tableName +"(" + idMessage +" Int," + message_date +" VARCHAR,"+ contacted_Username +" VARCHAR," +  message_Text +" VARCHAR, "+ message_Belong +" Int);";
        System.out.println(createTable);
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }



    public void addMessage(String contact_Name, String message_content, Date date, int from){
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Cursor cursor = this.getWritableDatabase().rawQuery("Select idMessage from Messages",null);
        cursor.moveToLast();
        if(cursor.getCount() > 0){
            next_message_id = cursor.getInt(0)+1;
        }
        else{
            next_message_id = 0;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(idMessage, next_message_id);
        contentValues.put(message_date, dateFormat.format(date));
        contentValues.put(contacted_Username, contact_Name);
        contentValues.put(message_Text, message_content);
        contentValues.put(message_Belong, from);

        Log.d("Database", "Added Message: " + contact_Name +"," +next_message_id + "," + message_content);

        long result = db.insert(tableName, null, contentValues);
        System.out.println(result);
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getDateAndSendUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT " + message_date +","+ contacted_Username + " FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getMessages(String date, String contacted_User){
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(date);
        System.out.println(contacted_User);
        String query = "SELECT * FROM " + tableName +" WHERE " + message_date + "='" + date + "' AND " + contacted_Username + "='" +contacted_User + "';";
        System.out.println(query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
