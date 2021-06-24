package com.huawei.shopmate.ui.DashBoard.MapnSite;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.huawei.shopmate.R;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.SupportMapFragment;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapView extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList allsites=new ArrayList<>();
    private SupportMapFragment mSupportMapFragment;
    private HuaweiMap hMap;
    private static final String TAG = "Map";
    Double focus_lat;
    Double focus_lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        allsites= (ArrayList<String>) getIntent().getSerializableExtra("allsites");
        this.focus_lat=(Double) getIntent().getDoubleExtra("lat",0);
        this.focus_lon=(Double) getIntent().getDoubleExtra("lon",0);

        for (int i=0;i<allsites.size();i++){
            System.out.println(allsites.get(i).toString());
        }
        System.out.println("-------"+this.focus_lat);
        System.out.println("-------"+this.focus_lon);
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mSupportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        Log.d("TAG", "onMapReady: ");
        hMap = huaweiMap;
        CameraPosition build = new CameraPosition.Builder().target(new LatLng(this.focus_lat, this.focus_lon)).zoom(15).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(build);
        Toast.makeText(this, hMap.getCameraPosition().toString(), Toast.LENGTH_LONG).show();
        hMap.moveCamera(cameraUpdate);

        for (int i=0;i<allsites.size();i++){
            MarkerOptions mk=new MarkerOptions();
            mk.position(new LatLng((Double) ((ArrayList)allsites.get(i)).get(4),(Double) ((ArrayList)allsites.get(i)).get(5)));
            mk.title((String)((ArrayList)allsites.get(i)).get(0));
            mk.icon(BitmapDescriptorFactory.fromResource(R.drawable.newshop));
            hMap.addMarker(mk);

        }

        MarkerOptions mk1=new MarkerOptions();
        mk1.position(new LatLng(this.focus_lat,this.focus_lon));
        mk1.title("I am here");
        mk1.icon(BitmapDescriptorFactory.fromResource(R.drawable.human));
        hMap.addMarker(mk1);


        CircleOptions co=new CircleOptions();
        co.center(new LatLng(this.focus_lat,this.focus_lon));
        co.radius(3200);
        co.strokeColor(Color.RED);
        co.fillColor(0x1Afebf3c);
        hMap.addCircle(co);




        //hMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(this.focus_lat,this.focus_lon),100,100,100)));
    }

    @Override
    protected void onDestroy() {
        mSupportMapFragment.onDestroy();
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        mSupportMapFragment.onStop();
        super.onStop();
    }
    @Override
    protected void onPause() {
        mSupportMapFragment.onPause();
        super.onPause();
    }
    @Override
    protected void onResume() {
        mSupportMapFragment.onResume();
        super.onResume();
    }
    @Override
    protected void onStart() {
        mSupportMapFragment.onStart();
        super.onStart();
    }
    @Override
    public void onLowMemory() {
        mSupportMapFragment.onLowMemory();
        super.onLowMemory();
    }
}
