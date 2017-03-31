package com.bignerdranch.android.iamwatchingyou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

public class IamWatchingYouActivity extends AppCompatActivity {
    private final int CODE_PERMISSIONS = 12325123;
    private IALocationManager mLocationManager;
    private static final String Fb_URL = "https://iamwatchingyou-aa347.firebaseio.com/";
    private Firebase myFb;
    private Position mUsersPosition;
    private Records mRecord;
    private PositionContainer pointsOfInterest;
    //Currently not using the correct keys for indoor atlas in manifest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iam_watching_you);
        myFb = new Firebase(Fb_URL);
        //Our singleton object with the points of interest
        pointsOfInterest = PositionContainer.getInstance();
        /**
         * Permission for android(6+).
         * The only "dangerous" permission we need is the coarse location.
         * We have to make this request in runtime. We also check if we need to explain
         * the reason we require this permission to the user. If the method returns true
         * then we throw a toast to the user explaining the reason we need coarse location.
         */

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(IamWatchingYouActivity.this,
                    R.string.corseLocation_access_denied, Toast.LENGTH_SHORT)
                    .show();
        } else {
            String[] myPermissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
            ActivityCompat.requestPermissions(this, myPermissions, CODE_PERMISSIONS);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults)
    {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED ) {
            //user denied access
            //take action. toast and then -> stop working.
        } else {

            //access granted by the user we can create our manager
            mLocationManager = IALocationManager.create(this);
        }


    }

    private IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            mUsersPosition = new Position(iaLocation.getLatitude(),
                    iaLocation.getLongitude());
            mRecord = new Records(mUsersPosition,iaLocation.getTime());

            Firebase position = myFb.child("latitude").child("longitude").child("time"); // check if it saves with unique id
            position.push().setValue(mRecord);
           String s = pointsOfInterest.isNeighbor(mUsersPosition);
            if(s.equals("example")) {
                //toast for example
            } else if (s.equals("2nd example")) {
                //toast for 2nd example
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(IALocationRequest.create(), mLocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeLocationUpdates(mLocationListener);


    }

    @Override
    protected void onDestroy() {
        mLocationManager.destroy();
        super.onDestroy();
    }


    private boolean doWheHavePermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestCoarseLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_COARSE_LOCATION}, CODE_PERMISSIONS);
    }
}
