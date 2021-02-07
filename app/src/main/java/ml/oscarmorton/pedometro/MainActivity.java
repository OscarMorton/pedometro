package ml.oscarmorton.pedometro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FASTEST_UPDATE_INTERVAL = 5;
    private static final int PERMISION_FIND_LOCATION = 101;

    private TextView tvStepCounter;
    private TextView tvLatitude, tvLongitude, tvAltitude, tvSpeed, tvAddress;
    private Button bStartWalk;

    private SensorManager sensorManager;
    private Sensor mStepCounter;
    private boolean isCounterSensorPresent;
    private int stepCount;
    private FragmentList frgList;


    // Location stuff
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        stepCount = 0;

        // IDs
        tvStepCounter = findViewById(R.id.tvStepCounter);
        bStartWalk = findViewById(R.id.bStartWalk);
        tvLatitude = findViewById(R.id.tvLatitud);
        tvLongitude = findViewById(R.id.tvLongitud);
        tvAltitude = findViewById(R.id.tvAltitude);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvAddress = findViewById(R.id.tvAddress);

        tvStepCounter.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        tvLatitude.setVisibility(View.GONE);
        tvAltitude.setVisibility(View.GONE);
        tvLongitude.setVisibility(View.GONE);
        tvSpeed.setVisibility(View.GONE);

        // This will keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //Getting the sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Location request initialize
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        // If we using maximum accuracy, how often do we need to update.
        locationRequest.setFastestInterval(1000 * FASTEST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //If the device does not have a step counter, we inform the user
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            isCounterSensorPresent = true;


        } else {
            tvStepCounter.setText("This device does not have a step conter");
            isCounterSensorPresent = false;

        }

        bStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stepCount = 0;
                tvStepCounter.setText(String.valueOf(stepCount));
                tvStepCounter.setVisibility(View.VISIBLE);
                tvAddress.setVisibility(View.VISIBLE);
                tvLatitude.setVisibility(View.VISIBLE);
                tvAltitude.setVisibility(View.VISIBLE);
                tvLongitude.setVisibility(View.VISIBLE);
                tvSpeed.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Walk started", Toast.LENGTH_SHORT).show();


            }
        });

        updateGPS();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // there will be only 1 item, so an if is enough
        if (item.getItemId() == R.id.item1) {
            frgList = (FragmentList) getSupportFragmentManager().findFragmentById(R.id.FrgList);

            Intent i = new Intent(this, WalksActivity.class);

            startActivity(i);


        }

        return super.onOptionsItemSelected(item);
    }

    // Sensor stuff Below

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            tvStepCounter.setText(String.valueOf(stepCount));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            sensorManager.unregisterListener(this, mStepCounter);
        }
    }

    // Location stuff below

    private void updateGPS() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        // if we have permision from location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUI(location);

                }

            });

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISION_FIND_LOCATION);
            }
        }

    }

    // Updates the TV to tell you where you are
    private void updateUI(Location location) {
        // TODO
        tvLatitude.setText(String.valueOf(location.getLatitude()));
        tvLongitude.setText(String.valueOf(location.getLongitude()));

        // Some devices do not have a altitude sensor. Same thing for speed.
        if (location.hasAltitude()) {
            tvAltitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tvAltitude.setText("Not Available");

        }
        if (location.hasSpeed()) {
            tvSpeed.setText(String.valueOf(location.getSpeed()));
        } else {
            tvSpeed.setText("Not Available");

        }


        // Geocode converts the location into an address
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                tvAddress.setText("You are walking in " + addresses.get(0).getLocality() );

        }catch (Exception e){
            tvAddress.setText("Have a good walk");

        }


    }

    // Tells the program to trigger a methord after permision granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISION_FIND_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "This app requires permision for location", Toast.LENGTH_SHORT).show();
                }
        }

    }
}
