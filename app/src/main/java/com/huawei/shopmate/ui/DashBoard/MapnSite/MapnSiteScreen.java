/*
*       Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

package com.huawei.shopmate.ui.DashBoard.MapnSite;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.shopmate.R;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.huawei.shopmate.ui.DashBoard.List.ListMenu;
import com.huawei.shopmate.ui.DashBoard.Scan.ScanScreen;

/**
 * Example of Using requestLocationUpdates and removeLocationUpdates.
 * Requests a location update and calls back on the specified Looper thread. This method requires that the requester
 * process exist for continuous callback.
 * If you still want to receive the callback after the process is killed, see requestLocationUpdates (LocationRequest
 * request,PendingIntent callbackIntent)
 *
 * @since 2020-5-11
 */
public class MapnSiteScreen extends AppCompatActivity implements OnClickListener {
    public static final String TAG = "LocationUpdatesCallback";

    LocationCallback mLocationCallback;

    LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest locationSettingsRequest;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1=new Intent(MapnSiteScreen.this, ListMenu.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        overridePendingTransition(0,0);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MapnSiteScreen.this,"Go to settings and allow location in app permissions if not enabled",Toast.LENGTH_LONG).show();

        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.item3);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.item1:
                    Intent intent1=new Intent(MapnSiteScreen.this, ListMenu.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    overridePendingTransition(0,0);

                    break;
                case R.id.item2:
                    Intent intent2=new Intent(MapnSiteScreen.this, ScanScreen.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                    overridePendingTransition(0,0);
                    break;
                case R.id.item3:

                    break;
            }
            return true;
        });


        findViewById(R.id.location_requestLocationUpdatesWithCallback).setOnClickListener(this);
        //findViewById(R.id.location_removeLocationUpdatesWithCallback).setOnClickListener(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationRequest = new LocationRequest();
        // Sets the interval for location update (unit: Millisecond)
        mLocationRequest.setInterval(5000);
        // Sets the priority
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        locationSettingsRequest = builder.build();
// Check the device location settings.
        requestPermissions();

    }

    private void getlocation(){
        Task<Location> task = mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    Log.i("#################","NULLLL");
                    Toast.makeText(MapnSiteScreen.this,"We cant Collect Location",Toast.LENGTH_LONG).show();
                    return;
                }
                String str="LastLocation:\n\nLatitude: "+
                        location.getLatitude()+"\n"+
                        "Longitude: "+location.getLongitude()+"\n";
                Log.i("@@@@@@@@@@@@@@@@",str);
                Intent myIntent = new Intent(MapnSiteScreen.this, SiteKitVIew.class);

                Bundle b = new Bundle();
                b.putDouble("lat",location.getLatitude());//6.0410021
                b.putDouble("lon",location.getLongitude());//80.2055641
                myIntent.putExtras(b); //Put your id to your next Intent
                MapnSiteScreen.this.startActivity(myIntent);
                overridePendingTransition(0,0);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
            }
        });
        task.addOnCompleteListener(command -> {});
    }
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.location_requestLocationUpdatesWithCallback:
                    requestPermissions();
                    getlocation();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "RequestLocationUpdatesWithCallbackActivity Exception:" + e);
        }
    }

    public void requestPermissions(){
        mSettingsClient.checkLocationSettings(locationSettingsRequest)
// Define callback for success in checking the device location settings.
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
// Initiate location requests when the location settings meet the requirements.
                        Toast.makeText(MapnSiteScreen.this,"Location setting is successful",Toast.LENGTH_LONG).show();
                        //txt_result.setText("Location setting is successful");
                    }
                })
// Define callback for failure in checking the device location settings.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
// Device location settings do not meet the requirements.
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
// Call startResolutionForResult to display a pop-up asking the user to enable related permission.
                                    rae.startResolutionForResult(MapnSiteScreen.this, 0);
                                } catch (IntentSender.SendIntentException sie) {
// ...
                                }
                                break;
                        }
                    }
                });
    }
}