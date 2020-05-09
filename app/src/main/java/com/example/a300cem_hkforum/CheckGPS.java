package com.example.a300cem_hkforum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.a300cem_hkforum.ui.home.HomeFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CheckGPS extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public String a;
    public static int REQUEST_LOCATION = 1;

    // member views
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mTimeText;
    protected TextView mOutput;
    protected TextView mTarget;
    protected Button start;
    protected ImageView bg;
    // member variables that hold location info
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected Geocoder mGeocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_gps);
        Executor executor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;
        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(CheckGPS.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.login))
                .setDescription(getString(R.string.finger))
                .setNegativeButtonText(getString(R.string.back))
                .build();


        bg = (ImageView)findViewById(R.id.welcome_bg);
        start = (Button)findViewById(R.id.start);
        start.setText(R.string.button_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                biometricPrompt.authenticate(promptInfo);


            }
        });
        start.setVisibility(View.GONE);
        // initialize views
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        mTimeText = (TextView) findViewById((R.id.time_text));
        mTarget = (TextView) findViewById(R.id.redirect);
        mOutput = (TextView) findViewById((R.id.output));


        // GoogleApiClient allows to connect to remote services, the two listeners are the first
        // two interfaces the current class implements
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // LocationReques sets how often etc the app receives location updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        // check if the current app has permission to access location of the device
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            // This ACCESS_COARSE_LOCATION corresponds to permission defined in manifest
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
                mTimeText.setText("Last known location");
            }

            // the last parameter specify the onLocationChanged listener
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    /*
     * This overriding method overrides ActivityCompat.OnRequestPermissionsResultCallback,
     * basically that is a method inherited.
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onConnected(null);
            }
        }
    }

    /*
     * Update UI on location change detected.
     * */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        mTimeText.setText(DateFormat.getTimeInstance().format(new Date()));
        mGeocoder = new Geocoder(this);
        try {
            // Only 1 address is needed here.
            List<Address> addresses = mGeocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);

            if (addresses.size() == 1) {
                Address address = addresses.get(0);
                checking(address.getAdminArea(),address.getCountryCode());
                StringBuilder addressLines = new StringBuilder();
                if (address.getMaxAddressLineIndex() > 0) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressLines.append(address.getAddressLine(i) + "\n");
                    }
                } else {
                    addressLines.append(address.getAddressLine(0));
                }
                mOutput.setText(addressLines);
            } else {
                mOutput.setText("WARNING! Geocoder returned more than 1 addresses!");
            }
        } catch (Exception e) {
            mOutput.setText("WARNING! Geocoder.getFromLocation() didn't work!");
        }
    }

public void checking(String CL, String C){
        String CLname;
        CLname = CL;
        if(CL.equals("香港島")){
            CL = "Hong Kong Island";
        } else if (CL.equals("九龍")){
            CL = "Kowloon";
        } else if (CL.equals("新界")){
            CL = "New Territories";
        }
        Bundle args = new Bundle();
        args.putString("key",CL);
        args.putString("name",CLname);
        HomeFragment.putA(args);
    if (C.equals("HK")){
        mTarget.setText(getString(R.string.will)+" "+ CLname + " "+ getString(R.string.title_forum));
        start.setVisibility(View.VISIBLE);
        if (CL.equals("Hong Kong Island")){
            bg.setImageResource(R.drawable.hki);
        }else if (CL.equals("Kowloon")){
            bg.setImageResource(R.drawable.kowloon);
        } else {
            bg.setImageResource(R.drawable.nt);
        }
    } else {
        mTarget.setText(getString(R.string.notHongKong));
        bg.setImageResource(R.drawable.hknight);
        start.setVisibility(View.GONE);
    }
}


}
