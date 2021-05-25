package com.example.messaging_app;

import android.Manifest;
import android.app.Activity;
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


    // Useful variables
    private Button button_back;
    public static final Strategy STRATEGY = Strategy.P2P_POINT_TO_POINT; // Strategy used for connection
    public static final String SERVICE_ID="MessageApp"; // Identification of the app
    private String endPointId_copy = ""; // Remember endPointId
    public static final String PREFERENCES_NAME = "dataStorage"; // Name of Preference database
    private SharedPreferences mSharedPreferences;
    private MessageAdapter messageAdapter; // Used for dapting messages
    public String username; // Name of user
    public DatabaseHelper myDataBase; // SQL database helper class
    public Date date; // Used for fetching the current date
    public String contactedUsername; // Name of user with which you chated
    public Boolean contacted_message = false; // Check if contacted message is sent
    public Boolean received_contacted_message = false; // Check if contacted message is received
    public String already_granted =""; // Check if all permissions are granted


    // User view components
    private ImageButton send_Button;
    private Button back; // Return to menu
    private Button discover; // Discover new contacts
    public Button button_restart; // Restart activity, go to menu


    private TextView text; // Writing text as message

    // Fun images
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;


    private ListView messagesView; // Contain messages
    // Layouts
    private LinearLayout layout;
    private LinearLayout layout2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataBase = DatabaseHelper.getInstance(this); // Get SQL database (already existed. If not exist create one)


        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE); // Get sharedPreferences
        username = mSharedPreferences.getString("username","0"); // Get username from sharedPreferences

        already_granted = mSharedPreferences.getString("all_permissions_granted","not_granted"); // Check if the permissions was been previously granted


        // Find all elements in the view and set their visibility
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


        messageAdapter = new MessageAdapter(this); // Create a new MessageAdapter for displaying the messaging correctly in the listView
        messagesView = (ListView) findViewById(R.id.message_view);
        messagesView.setAdapter(messageAdapter); // Assign messageAdapter to messagesView(ListView)  such that to add new messages

        startAdvertising(); // Start advertising for allowing income request for messaging


        // Return to Menu if back button pressed
        button_back= (Button)findViewById(R.id.button_back_page4);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                openActivity2();
                finish(); // finish current activity
            }
        });


        // Start discovering when button is pushed and end advertising. In addition check permissions
        discover = (Button) findViewById(R.id.discover);
        discover.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                Nearby.getConnectionsClient(getApplicationContext()).stopAdvertising(); // Stop advertising
                if(already_granted == "not_granted"){ // Check if the permissions are previously granted, if not perform below
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("all_permissions_granted","granted"); // Set that all permissions are granted
                    editor.commit();
                    button_restart.setVisibility(View.VISIBLE); // Show restart button
                }
                else{
                    startDiscovery(); // Start discovering new contacts
                }
            }
        });

        // Send message button, allow to send the message by taking from the TextView the written message
        send_Button = (ImageButton) findViewById(R.id.send_button);
        send_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                TextView tw = (TextView)findViewById(R.id.editText); // Get the TextView
                String text = tw.getText().toString(); // get message and convert to string
                sendPayLoad(endPointId_copy,text); // Use sendPayLoad function for sending the message by passing as parameters the endPointId (destination) and the message as a string
            }
        });

        button_restart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                openActivity1(); // Close current activity and go to the first activity(MainActivity)
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

    // Allow to check a permission
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity_page4.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity_page4.this, new String[] { permission }, requestCode); // Request permission
        }

    }


    // Pass to Menu (MainActivity_page2)
    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity_page2.class);
        startActivity(intent);
        finish();
    }

    // Pass to welcome page (MainActivity)
    public void openActivity1(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Allow to send small data through Wifi and Bluetooth
    private void sendPayLoad(String endPointId, String str) {
        Payload bytesPayload; // Payload has to be sent as bytes
        if(contacted_message == false){ // If the contacted message is not yet send (message which contain the username)
            bytesPayload = Payload.fromBytes((username).getBytes()); // Convert String username to bytes and then create payload
            contacted_message = true; // Set that contacted message is send
            //System.out.println(contactedUsername);
        }
        else{
            bytesPayload = Payload.fromBytes((str).getBytes()); // Convert message to payload
            Message message = new Message(str, username, true); // Create message by using Message class and pass parameters to the constructor
            messageAdapter.add(message); // Add message to adapter in order to display the message
            messagesView.setSelection(messagesView.getCount() - 1); // Go to the last message such that the last message is displayed in the list view
            endPointId = endPointId_copy; // Set endPointId

            //Insert Message data Into database
            myDataBase.addMessage(contactedUsername, str, date,0);

        }

        // Send payload
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

    // Fetch payloads
    PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            String received_message =  new String(payload.asBytes());
            if(received_contacted_message == false){  // If contacted message is not yet arrived, the new message arrived is the contacted message
                contactedUsername = received_message; // contacted username is the message itself
                received_contacted_message = true;    // the contacted message was been received, set to true
            }
            else{
                //Insert Message data Into database
                myDataBase.addMessage(contactedUsername,received_message, date,1); // 1 because arrived from another user
                Message message = new Message(received_message,contactedUsername, false); // create a message
                messageAdapter.add(message); // add message to the adapter
                messagesView.setSelection(messagesView.getCount() - 1); // display last message in the listView
            }
        }

        // Allow to see that the message is send
        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                System.out.println("Send");
            }
        }
    };


    // Advertise in order to be able to obtain request from other devices for messaging
    private void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build(); // Set options and the strategy applied
        Nearby.getConnectionsClient(getApplicationContext())
                .startAdvertising( // Start advertising
                        "Device B", SERVICE_ID, connectionLifecycleCallback, advertisingOptions)// In order to be able advertise pass service id, connectionLifeCycleCallback parameter and options
                .addOnSuccessListener( // If advertise success
                        (Void unused) -> {
                            // We're advertising!
                            System.out.println("Advertising");
                        })
                .addOnFailureListener(  // If advertise fails
                        (Exception e) -> {
                            // We were unable to start advertising.
                            System.out.println("Not Advertising");
                        });
    }

    // Used for connection establishment
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {

                // Initiate connection between devices
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo info) {

                    // Display accept or reject window
                    new AlertDialog.Builder(MainActivity_page4.this)
                            .setTitle("Accept connection to " + info.getEndpointName()) // Title of dialog
                            .setMessage("Confirm the code matches on both devices:" + info.getAuthenticationToken()) // message + token
                            .setPositiveButton(
                                    "Accept",  // text for accept connection
                                    (DialogInterface dialog, int which) ->
                                            // The user confirmed, so we can accept the connection.
                                            Nearby.getConnectionsClient(getApplicationContext()) // Set connection
                                                    .acceptConnection(endpointId, payloadCallback))
                            .setNegativeButton(
                                    android.R.string.cancel,
                                    (DialogInterface dialog, int which) ->
                                            // The user canceled, so we should reject the connection.
                                            Nearby.getConnectionsClient(getApplicationContext()).rejectConnection(endpointId)).show();  // reject connection
                    endPointId_copy = endpointId; // remember endPointId
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            // We're connected! Can now start sending and receiving data.

                            // Set visible elements for messaging
                            back.setVisibility(View.GONE);
                            image1.setVisibility(View.GONE);
                            image2.setVisibility(View.GONE);
                            image3.setVisibility(View.GONE);
                            text.setVisibility(View.GONE);
                            discover.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.VISIBLE);
                            sendPayLoad(endPointId_copy,""); // set the contacted message as the username
                            date = Calendar.getInstance().getTime(); // Get current time and date

                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            break;
                        default:
                            // Unknown status code
                    }
                }



                @Override
                public void onDisconnected(String endpointId) {
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.

                    System.out.println("Disconnected");
                }
            };

    // Start discovery for finding new user for messaging
    private void startDiscovery() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build(); // set options
        Nearby.getConnectionsClient(getApplicationContext())
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions) // set connection properties
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

    // Used for discovering phones
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    // An endpoint was found. We request a connection to it.

                    Nearby.getConnectionsClient(MainActivity_page4.this)
                            .requestConnection("Device A", endpointId, connectionLifecycleCallback) // set request connection parameters
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

                    System.out.println("End connection");
                }
            };


}

