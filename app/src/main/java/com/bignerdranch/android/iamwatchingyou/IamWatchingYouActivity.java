package com.bignerdranch.android.iamwatchingyou;



import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;

import java.io.File;

public class IamWatchingYouActivity extends FragmentActivity {
    private static final String Fb_URL = "https://iamwatchingyou-aa347.firebaseio.com/";
    private static final String TAG = "IamWatchingYouApp ";
    //Creating a reference to the Firebase database we are going to use with this application.
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference("User_Records");
    private final int CODE = 12312;
    private Position mUsersPosition;
    private Records mRecord;
    private IATask<IAFloorPlan> mPendingAsyncResult;
    private IALocationManager mLocationManager;
    private IALocationManager mLocationManagerTestingNull;
    private IALocationRequest mLocationRequest;
    private IAFloorPlan mFloorPlan;
    private IAResourceManager mFloorPlanManager;
    private BlueDotView mFloorPlanImage;
    private DownloadManager mDownloadManager;
    private Button mSettingsButton;
    private long mDownloadId;
    private static final float dotRadius = 1.0f;

    private IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation location) {

            Log.d(TAG, "Latitude " + location.getLatitude());
            Log.d(TAG, "Longitude "+ location.getLongitude());


            //We create a position object with the latitude and the longitude of our user
            mUsersPosition = new Position(location.getLatitude(), location.getLongitude());
            //We create an object with the location passed as an object and the time of
            //we recorded this location
            mRecord = new Records(mUsersPosition, location.getTime());
            //we save that object to our database with a unique id
            myRef.push().setValue(mRecord);

        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //N/A
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iam_watching_you);

        //Creating our settings button. Currently hidden from users ui.
        mSettingsButton = (Button) findViewById(R.id.GoToSettings_button);
        //Creating a new instance of location manager class to access indoorAtlas services.
        mLocationManager = IALocationManager.create(this);
        //Checking the android version of our user
        //There is a different policy for requesting permission from mobiles using android version
        //6+. If the mobile phone of our user has Marshmallow or a latter
        // android version the we need to request permission on runtime.
        //@returns true then we check if we have the required permissions to proceed.

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, " onResume");
        requestFineLocationPermission();
        Log.d(TAG, " insidePermissions " );
        mSettingsButton.setVisibility(View.GONE);
        //location updates every 1sec
        Log.d(TAG, " going for location updates with manager "+mLocationManager.toString() );
        Log.d(TAG, " mlocationListener "+ mLocationListener.toString());
        mLocationManager.requestLocationUpdates(IALocationRequest.create()
                .setFastestInterval(1000), mLocationListener);
        Log.d(TAG, " oeo");



    }

    @Override
    protected void onPause() {
        super.onPause();

        mLocationManager.removeLocationUpdates(mLocationListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManager.destroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState");
    }


    //We are making a request, if we don't already have, to access users fine location
    private void requestFineLocationPermission() {
        Log.d(TAG, "Requesting Fine Location Permission ");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "You already have permission what am I doing here? ");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODE);
        }
    }

    //Callback for the result from requesting permissions. This method is invoked for every call
    //on requestPermissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults ) {

        switch (requestCode) {
            case CODE :
                if (grantResults.length == 0 || grantResults[0]
                        == PackageManager.PERMISSION_DENIED) {

                    Toast.makeText(this,"please give us access to your location through settings",Toast.LENGTH_LONG)
                            .show();
                    finish();

                } else {
                    mLocationManager.requestLocationUpdates(IALocationRequest.create().
                            setFastestInterval(1000), mLocationListener);
                }
                Log.d(TAG,"grant permission");
                break;
        }

    }

}
