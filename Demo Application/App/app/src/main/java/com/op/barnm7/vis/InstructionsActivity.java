package com.op.barnm7.vis;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.controller.Controller;
import com.google.vr.sdk.controller.Controller.ConnectionStates;
import com.google.vr.sdk.controller.ControllerManager;
import com.google.vr.sdk.controller.ControllerManager.ApiStatus;

public class InstructionsActivity extends AppCompatActivity {

    public final static int CONTROLLER_SYNC = 13;
    public final static int BLUETOOTH_REQUEST = 12;
    private Button syncButton;
    private TextView connection_text;
    private TextView instructions_text;
    private ImageView animationView;

    //Handler for controller
    private Handler uiHandler = new Handler();

    //Controller objects
    private ControllerManager controllerManager;
    private Controller controller;
    private AnimationDrawable animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.App);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        fullscreenSetup();
        setUpComponents();
        setUpDayDream();
        checkBluetoothEnabled();
    }

    public void showBluetoothButton()
    {
        animationView.setVisibility(View.INVISIBLE);
        animation.stop();

        if (syncButton.getVisibility() == View.INVISIBLE)
        {
            syncButton.setVisibility(View.VISIBLE);
        }
    }

    public void fullscreenSetup()
    {
        setContentView(R.layout.instructions);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void setUpComponents()
    {
        syncButton = findViewById(R.id.button);
        syncButton.setOnClickListener(new SyncButtonHandler());
        connection_text = findViewById(R.id.textView3);
        instructions_text = findViewById(R.id.textView2);
        animationView = (ImageView) findViewById(R.id.imageView2);
        animationView.setBackgroundResource(R.drawable.controller_turn_on);
        animation = (AnimationDrawable) animationView.getBackground();
        showAnimation();
    }

    public void showAnimation()
    {
        if (syncButton.getVisibility() == View.VISIBLE)
        {
            syncButton.setVisibility(View.INVISIBLE);
        }
        if (animationView.getVisibility() == View.INVISIBLE)
        {
            animationView.setVisibility(View.VISIBLE);
        }
    }

    public void checkBluetoothEnabled()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            AlertDialog dialog = createDialog("This device does not support bluetooth.");
            dialog.show();
        }
        else
        {
            // If bluetooth is disabled start intent to enable it
            if (!mBluetoothAdapter.isEnabled())
            {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, BLUETOOTH_REQUEST);
            }
        }
    }

    public void setUpDayDream()
    {
        // Set up controller
        EventListener listener = new EventListener();
        controllerManager = new ControllerManager(this, listener);
        controller = controllerManager.getController();
        controller.setEventListener(listener);
        boolean enabled = AndroidCompat.setVrModeEnabled(this, true);

        if (!enabled)
        {
            AlertDialog dialog = createDialog("Daydream is not compatible with this phone.");
            dialog.show();
        }
    }

    public AlertDialog createDialog(String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).setNegativeButton("Exit", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                finishAffinity();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class SyncButtonHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent();
            intent.setClassName("com.google.vr.vrcore", "com.google.vr.vrcore.daydream.PairingActivity");
            startActivityForResult(intent, CONTROLLER_SYNC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case BLUETOOTH_REQUEST:
                if (resultCode == RESULT_OK)
                {
                    Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
                }
                break;
            case CONTROLLER_SYNC:
                showAnimation();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpComponents();
        animation.start();
        controllerManager.start();
    }

    @Override
    protected void onStop() {
        controllerManager.stop();
        super.onStop();
    }

    private class EventListener extends Controller.EventListener implements ControllerManager.EventListener, Runnable {

        // The status of the overall controller API. This is primarily used for error handling since
        // it rarely changes.
        private String apiStatus;
        // The state of a specific Controller connection.
        private int controllerState = ConnectionStates.DISCONNECTED;
        private Boolean clicked = false;
        private Boolean changed = false;

        private final String connectedText = "Press the click button to begin.";
        private final String disconnectedText = "Cannot connect Daydream controller. Press the bluetooth button below to create a connection.";
        private final String scanningText = "Press and hold the Daydream button on your controller.";


        @Override
        public void onApiStatusChanged(int state) {
            apiStatus = ApiStatus.toString(state);
            uiHandler.post(this);
        }

        @Override
        public void onConnectionStateChanged(int state) {
            controllerState = state;
            changed = false;

            uiHandler.post(this);
        }

        @Override
        public void onRecentered() {
            // In a real GVR application, this would have implicitly called recenterHeadTracker().
            // Most apps don't care about this, but apps that want to implement custom behavior when a
            // recentering occurs should use this callback.
        }

        @Override
        public void onUpdate() {
            uiHandler.post(this);
        }


        public void changeAnimation(int resource)
        {
            if (!changed)
            {
                changed = true;
                animation.stop();
                animationView.setBackgroundResource(resource);
                animation = (AnimationDrawable) animationView.getBackground();
                animation.start();
            }
        }

        // Update the various TextViews in the UI thread.
        @Override
        public void run() {

            controller.update();
            connection_text.setText(ConnectionStates.toString(controllerState));

            // Launches VR activity
            if (controller.clickButtonState)
            {
                if (!clicked)
                {
                    Intent intent = new Intent(InstructionsActivity.this, MainActivity.class);
                    /** request permission via start activity for result */
                    startActivity(intent);
                }
                clicked = true;
            }
            else
            {
                clicked = false;
            }

            // Handles user interface
            switch (controllerState)
            {
                case ConnectionStates.CONNECTED:
                    connection_text.setTextColor(Color.GREEN);
                    instructions_text.setText(connectedText);
                    changeAnimation(R.drawable.controller_click);
                    break;
                case ConnectionStates.CONNECTING:
                    connection_text.setTextColor(Color.YELLOW);
                    instructions_text.setText("");
                    break;
                case ConnectionStates.DISCONNECTED:
                    connection_text.setTextColor(Color.RED);
                    instructions_text.setText(disconnectedText);
                    showBluetoothButton();
                    break;
                case ConnectionStates.SCANNING:
                    connection_text.setTextColor(Color.BLUE);
                    instructions_text.setText(scanningText);
                    break;
            }
        }
    }
}
