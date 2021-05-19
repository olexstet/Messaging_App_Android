package com.example.messaging_app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity_page4 extends AppCompatActivity {


    private Button button_back;
    public static final Strategy STRATEGY = Strategy.P2P_POINT_TO_POINT;
    public static final String SERVICE_ID="MessageApp";
    private String endPointId_copy = "";
    private ImageButton send_Button;
    private Button back;
    private ImageView image1;
    private TextView text;
    private ImageView image2;
    private ImageView image3;
    private Button discover;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private LinearLayout layout;
    private LinearLayout layout2;
    public static final String PREFERENCES_NAME = "dataStorage";
    private SharedPreferences mSharedPreferences;
    public String username;
    public DatabaseHelper myDataBase;
    public Date date;
    public String contactedUsername;
    public Boolean contacted_message = false;
    public Boolean received_contacted_message = false;
    public Button button_restart;
    public String already_granted ="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataBase = DatabaseHelper.getInstance(this);

        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        username = mSharedPreferences.getString("username","0");
        endPointId_copy = getIntent().getStringExtra("endPoint");

        already_granted = mSharedPreferences.getString("all_permissions_granted","not_granted");
        System.out.println(already_granted);


        setContentView(R.layout.activity_main_page4);
        back = (Button)findViewById(R.id.button_back_page4);
        button_restart = (Button)findViewById(R.id.button_restart);
        button_restart.setVisibility(View.GONE);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        text = (TextView)findViewById(R.id.explaination);
        layout = (LinearLayout)findViewById(R.id.layout_Text);
        layout.setVisibility(View.GONE);
        layout2 = (LinearLayout)findViewById(R.id.layout_2);
        layout2.setVisibility(View.GONE);


        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.message_view);
        messagesView.setAdapter(messageAdapter);
        startAdvertising();

        System.out.println("Work_Here");


        button_back= (Button)findViewById(R.id.button_back_page4);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                openActivity2();
                finish();
            }
        });


        discover = (Button) findViewById(R.id.discover);
        discover.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                Nearby.getConnectionsClient(getApplicationContext()).stopAdvertising();
                if(already_granted == "not_granted"){
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("all_permissions_granted","granted");
                    editor.commit();
                    button_restart.setVisibility(View.VISIBLE);
                }
                else{
                    startDiscovery();
                }
            }
        });

        send_Button = (ImageButton) findViewById(R.id.send_button);
        send_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                TextView tw = (TextView)findViewById(R.id.editText);
                String text = tw.getText().toString();
                sendPayLoad(endPointId_copy,text);
            }
        });

        button_restart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                openActivity1();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        // Function to check and request permission
        checkPermission(Manifest.permission.BLUETOOTH, 100);
        checkPermission(Manifest.permission.BLUETOOTH_ADMIN, 101);
        checkPermission(Manifest.permission.ACCESS_WIFI_STATE, 102);
        checkPermission(Manifest.permission.CHANGE_WIFI_STATE, 103);
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 104);
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 105);
        System.out.println("Checked");

    }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity_page4.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity_page4.this, new String[] { permission }, requestCode);
        }

    }



    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity_page2.class);
        startActivity(intent);
        finish();
    }

    public void openActivity1(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void sendPayLoad(String endPointId, String str) {
        Payload bytesPayload;
        if(contacted_message == false){
            bytesPayload = Payload.fromBytes((username).getBytes());
            contacted_message = true;
            System.out.println(contactedUsername);
        }
        else{
            bytesPayload = Payload.fromBytes((str).getBytes());
            Message message = new Message(str, username, true);
            messageAdapter.add(message);
            messagesView.setSelection(messagesView.getCount() - 1);
            endPointId = endPointId_copy;
            System.out.println("Work"+ endPointId);

            //Insert Message data Into database
            myDataBase.addMessage(contactedUsername, str, date,0);
            System.out.println(contactedUsername);
        }

        Nearby.getConnectionsClient(MainActivity_page4.this).sendPayload(endPointId, bytesPayload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Send success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Send failure");
            }
        });
    }


    PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            String received_message =  new String(payload.asBytes());
            if(received_contacted_message == false){
                contactedUsername = received_message;
                received_contacted_message = true;
                System.out.println(contactedUsername);
            }
            else{
                //Insert Message data Into database
                myDataBase.addMessage(contactedUsername,received_message, date,1);
                Message message = new Message(received_message,contactedUsername, false);
                messageAdapter.add(message);
                messagesView.setSelection(messagesView.getCount() - 1);
                System.out.println(received_message);
                System.out.println(contactedUsername);
            }
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                System.out.println("Send");
            }
        }
    };



    private void startAdvertising() {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(getApplicationContext())
                .startAdvertising(
                        "Device B", SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            // We're advertising!
                            System.out.println("Advertising");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            // We were unable to start advertising.
                            System.out.println("Not Advertising");
                        });
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {

                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo info) {
                    System.out.println("Work");

                    new AlertDialog.Builder(MainActivity_page4.this)
                            .setTitle("Accept connection to " + info.getEndpointName())
                            .setMessage("Confirm the code matches on both devices:" + info.getAuthenticationToken())
                            .setPositiveButton(
                                    "Accept",
                                    (DialogInterface dialog, int which) ->
                                            // The user confirmed, so we can accept the connection.
                                            Nearby.getConnectionsClient(getApplicationContext())
                                                    .acceptConnection(endpointId, payloadCallback))
                            .setNegativeButton(
                                    android.R.string.cancel,
                                    (DialogInterface dialog, int which) ->
                                            // The user canceled, so we should reject the connection.
                                            Nearby.getConnectionsClient(getApplicationContext()).rejectConnection(endpointId)).show();
                    endPointId_copy = endpointId;
                    System.out.println(endpointId);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            // We're connected! Can now start sending and receiving data.
                            System.out.println("Connected");

                            back.setVisibility(View.GONE);
                            image1.setVisibility(View.GONE);
                            image2.setVisibility(View.GONE);
                            image3.setVisibility(View.GONE);
                            text.setVisibility(View.GONE);
                            discover.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.VISIBLE);
                            sendPayLoad(endPointId_copy,"");

                            date = Calendar.getInstance().getTime();


                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            // The connection broke before it was able to be accepted.
                            System.out.println("Brake");
                            break;
                        default:
                            // Unknown status code
                    }
                }



                @Override
                public void onDisconnected(String endpointId) {
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.
                }
            };

    private void startDiscovery() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(getApplicationContext())
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            // We're discovering!
                            System.out.println("Discovered");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            // We're unable to start discovering.
                            System.out.println("Not Discovered");
                        });
    }

    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    // An endpoint was found. We request a connection to it.
                    System.out.println("Work1");
                    Nearby.getConnectionsClient(MainActivity_page4.this)
                            .requestConnection("Device A", endpointId, connectionLifecycleCallback)
                            .addOnSuccessListener(
                                    (Void unused) -> {
                                        // We successfully requested a connection. Now both sides
                                        // must accept before the connection is established.
                                        System.out.println("Success Connection");
                                    })
                            .addOnFailureListener(
                                    (Exception e) -> {
                                        // Nearby Connections failed to request the connection.
                                        System.out.println("Failed Connection");
                                    });
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    // A previously discovered endpoint has gone away.
                }
            };


}

