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

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int item) {
        return messages.get(item);
    }

    @Override
    public long getItemId(int id_item) {
        return id_item;
    }


    @Override
    public View getView(int index_message, View change_view, ViewGroup viewGroup) {
        message_Interpreter interpreter = new message_Interpreter();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(index_message);


        if (message.isCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            change_view = messageInflater.inflate(R.layout.my_message, null);
            interpreter.messageBody = (TextView) change_view.findViewById(R.id.message_body);
            change_view.setTag(interpreter);
            interpreter.messageBody.setText(message.getBody());
        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            change_view = messageInflater.inflate(R.layout.send_message, null);
            change_view.setTag(interpreter);

            interpreter.username = (TextView) change_view.findViewById(R.id.name);
            interpreter.messageBody = (TextView) change_view.findViewById(R.id.message_body);
            interpreter.user_color = (View) change_view.findViewById(R.id.user_color);

            interpreter.username.setText(message.getUserName());
            interpreter.messageBody.setText(message.getBody());
            GradientDrawable draw_user_color = (GradientDrawable) interpreter.user_color.getBackground();
            draw_user_color.setColor(Color.parseColor("White"));
        }

        return change_view;
    }

}

class message_Interpreter {
    public View user_color;
    public TextView username;
    public TextView messageBody;
}
