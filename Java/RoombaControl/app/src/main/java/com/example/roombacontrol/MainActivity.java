package com.example.roombacontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.roombacontrol.enums.Direction;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Roomba roomba;
    private SensorManager sensorManager;
    private Sensor sensor;

    private boolean tiltEnabled = false;

    private static final int TILT_THRESHOLD = 2;

    // UI Components
    private Button buttonTilt;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonForward;
    private Button buttonBackward;

    private EditText editTextIPAddress;
    private EditText editTextNickname;

    private ConstraintLayout constraintLayoutDriveControls;
    private ConstraintLayout constraintLayoutRoombaSelector;
    private ConstraintLayout constraintLayoutAddRoomba;

    private TextView textViewOutput;

    // Array of Roomba objects
    private ArrayList<Roomba> roombaSwarm;

    private int currentRoomba = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create HTTP request queue
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        // Create new roombaSwarm
        roombaSwarm = new ArrayList<>();

        // Add it to the roombaSwarm
        roombaSwarm.add(new Roomba("Initial"));

        // Register sensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize UI
        initUI();

        constraintLayoutAddRoomba.setVisibility(View.GONE);

        updateUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUI(){

        // Tie view components to the activity
        buttonTilt = findViewById(R.id.buttonTilt);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonForward = findViewById(R.id.buttonForward);
        buttonBackward = findViewById(R.id.buttonBackward);

        textViewOutput = findViewById(R.id.textViewCurrentRoomba);

        editTextIPAddress = findViewById(R.id.editTextIPAddress);
        editTextNickname = findViewById(R.id.editTextNickname);

        constraintLayoutDriveControls = findViewById(R.id.constranitLayoutDriveControls);
        constraintLayoutRoombaSelector = findViewById(R.id.constraintLayoutRoombaSelector);
        constraintLayoutAddRoomba = findViewById(R.id.constraintLayoutAdd);

        buttonTilt.setVisibility(View.GONE);

        // Register touchlisteners for L/R buttons
        /*buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        requestQueue.add(roomba.setDirection(Direction.LEFT));
                        break;
                    default: case MotionEvent.ACTION_UP:
                        requestQueue.add(roomba.setDirection(Direction.STOPPED));
                }
                return true;
            }
        });
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        requestQueue.add(roomba.setDirection(Direction.RIGHT));
                        break;
                    default: case MotionEvent.ACTION_UP:
                        requestQueue.add(roomba.setDirection(Direction.STOPPED));
                }
                return true;
            }
        });
        buttonForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        requestQueue.add(roomba.setDirection(Direction.FORWARD));
                        break;
                    default: case MotionEvent.ACTION_UP:
                        requestQueue.add(roomba.setDirection(Direction.STOPPED));
                }
                return true;
            }
        });
        buttonBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        requestQueue.add(roomba.setDirection(Direction.BACKWARD));
                        break;
                    default: case MotionEvent.ACTION_UP:
                        requestQueue.add(roomba.setDirection(Direction.STOPPED));
                }
                return true;
            }
        });*/
    }

    private void updateUI(){
        // Update UI to display current roomba
        textViewOutput.setText("Controlling Roomba "+(currentRoomba + 1)+"/"+roombaSwarm.size() +
                ": " + roombaSwarm.get(currentRoomba).getNickname());
    }

    public void onSensorChanged(SensorEvent event){

        // Check to see if tilt is available, if so make changes
        if(!tiltEnabled){
            return;
        }

        // Get sensor values
        float x = event.values[0];
        float y = event.values[1];

        // Get tilt direction
        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
                // Tilted right
                requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.RIGHT));
            }
            if (x > 0) {
                // Tilted left
                requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.LEFT));
            }
        } else {
            if (y < 0) {
                // Tilted up
                requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.FORWARD));
            }
            if (y > 0) {
                // Tilted down
                requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.BACKWARD));
            }
        }
        if (x > (-TILT_THRESHOLD) && x < (TILT_THRESHOLD) && y > (-TILT_THRESHOLD) && y < (TILT_THRESHOLD)) {
            // Not tilted, stop
            requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.STOPPED));
        }
    }

    public void onClickStop(View v){
        // Stop Roomba
        requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.STOPPED));
    }

    public void onClickForward(View v){
        // Set direction to forward
        requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.FORWARD));
    }

    public void onClickLeft(View v){
        // Set direction to Left
        requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.LEFT));
    }

    public void onClickRight(View v){
        // Set direction to Right
        requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.RIGHT));
    }

    public void onClickBackward(View v){
        // Set direction to backward
        requestQueue.add(roombaSwarm.get(currentRoomba).setDirection(Direction.BACKWARD));
    }

    public void onClickTilt(View v){

        // Take current tiltEnabled, flip it
        tiltEnabled = !tiltEnabled;

        // Set buttonTilt text, show/hide drivecontrols
        if (tiltEnabled) {
            buttonTilt.setText("Disable Tilt");
            // Disable drivecontrols
            constraintLayoutDriveControls.setVisibility(View.GONE);
        } else {
            buttonTilt.setText("Enable Tilt");
            // Disable drivecontrols
            constraintLayoutDriveControls.setVisibility(View.VISIBLE);
        }
    }

    public void onClickAddRoomba(View v){
        // Hide UI, show add roomba ui
        constraintLayoutRoombaSelector.setVisibility(View.GONE);
        constraintLayoutDriveControls.setVisibility(View.GONE);

        constraintLayoutAddRoomba.setVisibility(View.VISIBLE);
    }

    public void onClickAddRoombaSubmit(View v) {
        String address = editTextIPAddress.getText().toString();
        String nickname = editTextNickname.getText().toString();

        // Validate input
        if(address.isEmpty() || nickname.isEmpty()){
            Toast.makeText(this, "Please enter an IP and nickname", Toast.LENGTH_LONG).show();
            return;
        }

        // Input is valid, add a new Roomba
        try {
            roombaSwarm.add(new Roomba(address, nickname));
        } catch (UnknownHostException e) {
            // Address given was not valid, show toast
            Toast.makeText(this, "Please enter a valid IP address!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        constraintLayoutRoombaSelector.setVisibility(View.VISIBLE);
        constraintLayoutDriveControls.setVisibility(View.VISIBLE);
        constraintLayoutAddRoomba.setVisibility(View.GONE);
    }

    public void onClickNext(View v){
        currentRoomba++;
        if(currentRoomba >= roombaSwarm.size()){
            currentRoomba = 0;
        }
        updateUI();
    }

    public void onClickLast(View v){
        currentRoomba--;
        if(currentRoomba < 0){
            currentRoomba = roombaSwarm.size() - 1;
        }
        updateUI();
    }
}