package com.example.messaging_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>(); // list of messages
    Context context; // remember the context

    public MessageAdapter(Context context) {
        this.context = context;
    }

    // add a new message to the list of messages
    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size(); // return the number of messages
    }

    @Override
    public Object getItem(int pos) {
        return messages.get(pos); // return a message at a certain position
    }

    @Override
    public long getItemId(int id_item) {
        return id_item; // return the id of the item
    }


    @Override
    public View getView(int index_message, View change_view, ViewGroup viewGroup) {
        message_Interpreter interpreter = new message_Interpreter(); // Create new interpreter of the message
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE); // get LayoutInflater
        Message message = messages.get(index_message); // get message with index of message


        if (message.isCurrentUser()) { // If the message belong to the current user
            change_view = messageInflater.inflate(R.layout.my_message, null);
            interpreter.messageBody = (TextView) change_view.findViewById(R.id.message_body); // get message_body textView where the message has to be placed
            change_view.setTag(interpreter);  // set Tag
            interpreter.messageBody.setText(message.getBody()); // Set message to the textView
        } else {
            change_view = messageInflater.inflate(R.layout.send_message, null);
            change_view.setTag(interpreter); // set Tag

            interpreter.username = (TextView) change_view.findViewById(R.id.name); // Get username TextView
            interpreter.messageBody = (TextView) change_view.findViewById(R.id.message_body); // Get messageBody TextView
            interpreter.user_color = (View) change_view.findViewById(R.id.user_color); // Get view for setting the color

            interpreter.username.setText(message.getUserName()); // set contacted username
            interpreter.messageBody.setText(message.getBody()); // set text of the message
            GradientDrawable draw_user_color = (GradientDrawable) interpreter.user_color.getBackground(); // Draw circle
            draw_user_color.setColor(Color.parseColor("White")); // set Color
        }

        return change_view; // return new changeView with messages
    }

}

// Allow to set the data to the views
class message_Interpreter {
    public View user_color;
    public TextView username;
    public TextView messageBody;
}
