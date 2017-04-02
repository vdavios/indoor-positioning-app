package com.bignerdranch.android.iamwatchingyou;



import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IAResourceManager;

public class IamWatchingYouActivity extends FragmentActivity {
    private static final String Fb_URL = "https://iamwatchingyou-aa347.firebaseio.com/";
    private static final String TAG = "IamWatchingYouApp";
    private Firebase myFb;
    private final int CODE = 12312;
    private Position mUsersPosition;
    private Records mRecord;
    private IALocationManager mLocationManager;
    private IALocationRequest mLocationRequest;
    private IAResourceManager mResourceManager;
    private IAResourceManager mFloorPlanManager;
    private ImageView mFloorPlanImage;
    private DownloadManager mDownloadManager;
    private Button mSettingsButton;
    private long mDownloadId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iam_watching_you);
        //Creating our settings button. Currently hidden from users ui.
        mSettingsButton = (Button) findViewById(R.id.GoToSettings_button);
        //Initializing firebase library with android context
        Firebase.setAndroidContext(this);
        //Creating a reference to the Firebase database we are going to use with this application.
        myFb = new Firebase(Fb_URL);
        //Creating a new instance of location manager class to access indoorAtlas services.
        mLocationManager = IALocationManager.create(this);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mFloorPlanManager = IAResourceManager.create(this);
        //exw dwsei stadar map sto indooratlas_floor_plan_id den exei noima afto pou kanw
        final String floorPlanId = getString(R.string.indooratlas_floor_plan_id);
        if(!TextUtils.isEmpty(floorPlanId)) {
            final IALocation location = IALocation.from(IARegion.floorPlan(floorPlanId));
            mLocationManager.setLocation(location);
        }
        //Checking the android version of our user
        //There is a different policy for requesting permission from mobiles using android version
        //6+. If the mobile phone of our user has Marshmallow or a latter
        // android version the we need to request permission on runtime.
        //@returns true then we check if we have the required permissions to proceed.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //if we don't have the permission then we ask for it from the user.
            if(!doWeHavePermissions()) {
                //Coarse location permission is the only dangerous permission that our application
                //is using and we need to ask on the runtime, according to Google.
                requestCoarseLocationPermission();
            }
        } else {
            locationRequest();
            //mLocationManager.requestLocationUpdates(locationRequest(), mLocationListener);

        }
    }
    //Callback for the result from requesting permissions. This method is invoked for every call
    //on requestPermissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //If shouldSowRequest returns true means that our user denied our request but
            //he/she didn't chose Don't ask me again option. So we create a toast to explain
            //why we require this permission.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(IamWatchingYouActivity.this,
                        R.string.coarseLocation_access_denied,
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                magicButton();
            }
        }
    }

    private IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation location) {
            Firebase tracking = myFb.child("position").child("time");
            //We create a position object with the latitude and the longitude of our user
            mUsersPosition = new Position(location.getLatitude(), location.getLongitude());
            //We create an object with the location passed as an object and the time of
            //we recorded this location
            mRecord = new Records(mUsersPosition, location.getTime());
            //we save that object to our database with a unique id
            tracking.push().setValue(mRecord);


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    private IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion region) {
            if(region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                String id = region.getId();
                Toast.makeText(IamWatchingYouActivity.this, id, Toast.LENGTH_SHORT)
                        .show();
                fetchFloorPlan(id);
            }
            @Override
            public void onExitRegion(){

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if(doWeHavePermissions()){
            mSettingsButton.setVisibility(View.GONE);
            mLocationManager.requestLocationUpdates(locationRequest(), mLocationListener);
            mLocationManager.registerRegionListener(mRegionListener);
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeLocationUpdates(mLocationListener);
        mLocationManager.unregisterRegionListener(mRegionListener);
        unregisterReceiver(onComplete);

    }

    /*Broadcast receiver for floor plan image download*/
    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if(id != mDownloadId) {
                Log.w(TAG, "Ignore unrelated download");
                return;
            }
            Log.w(TAG, "Image downlad completed");
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = mDownloadManager.query(q);

            if(c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    //process download
                    String filePath = c.getString(c.getColumnIndex(
                            DownloadManager.COLUMN_LOCAL_FILENAME
                    ))
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        mLocationManager.destroy();
        super.onDestroy();
    }

    private boolean doWeHavePermissions() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestCoarseLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                CODE);
    }


    private IALocationRequest locationRequest() {
        mLocationRequest = IALocationRequest.create();
        //location updates every 1 sec
        mLocationRequest.setFastestInterval(1000);
        return mLocationRequest;
    }

    private void goToAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void magicButton() {

        mSettingsButton.setVisibility(View.VISIBLE);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAppSettings();
            }
        });
    }
}
