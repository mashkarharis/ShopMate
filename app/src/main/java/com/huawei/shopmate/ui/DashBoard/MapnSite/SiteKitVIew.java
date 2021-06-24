package com.huawei.shopmate.ui.DashBoard.MapnSite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.huawei.shopmate.R;
import com.huawei.shopmate.model.SiteAdapter;
import com.huawei.shopmate.model.SiteData;
import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.AddressDetail;
import com.huawei.hms.site.api.model.Coordinate;
import com.huawei.hms.site.api.model.LocationType;
import com.huawei.hms.site.api.model.NearbySearchRequest;
import com.huawei.hms.site.api.model.NearbySearchResponse;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;
import com.huawei.shopmate.ui.DashBoard.List.ListMenu;

import java.util.ArrayList;
import java.util.List;

public class SiteKitVIew extends AppCompatActivity implements View.OnClickListener {
    Double latitude;
    Double longitude;
    private SearchService searchService;
    ArrayList allsites;

    private ArrayList<String> listItems=new ArrayList<String>();
    private ListView mMyListView;
    ArrayAdapter<String> adapter;


    private RecyclerView recyclerView;
    private SiteAdapter siteadapter;
    private List<SiteData> studentDataList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1=new Intent(this, MapnSiteScreen.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        overridePendingTransition(0,0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_kit_view);
        findViewById(R.id.GotoMap).setOnClickListener(this);
//        mMyListView=(ListView) findViewById(R.id.MyListView);
//        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
//        mMyListView.setAdapter(adapter);
//
//        adapter.notifyDataSetChanged();

        Bundle b = getIntent().getExtras();
        if(b != null) {
            this.latitude = b.getDouble("lat");
            this.longitude = b.getDouble("lon");
            System.out.println("LATITUDE : " + this.latitude);
            System.out.println("LONGITUDE : " + this.longitude);
        }else{
            Intent myIntent = new Intent(SiteKitVIew.this, MapnSiteScreen.class);
            SiteKitVIew.this.startActivity(myIntent);
            overridePendingTransition(0,0);
            finish();
        }
        this.allsites=new ArrayList();
        searchService = SearchServiceFactory.create(SiteKitVIew.this, getResources().getString(R.string.api_key));
        findPlaceAroundMe();




    }

    private void findPlaceAroundMe() {
        NearbySearchRequest request = new NearbySearchRequest();
        Coordinate location = new Coordinate(this.latitude,this.longitude);
        request.setLocation(location);
        request.setQuery("");
        request.setRadius(3000);
        request.setPoiType(LocationType.GROCERY_OR_SUPERMARKET);
        request.setLanguage("In");
        request.setPageIndex(1);
        request.setPageSize(15);
        // Create a search result listener.
        SearchResultListener<NearbySearchResponse> resultListener = new
                SearchResultListener<NearbySearchResponse>() {
                    // Return search results upon a successful search.
                    @Override
                    public void onSearchResult(NearbySearchResponse results) {
                        AddressDetail addressDetail;
                        List<Site> sites = results.getSites();
                        if (results == null || results.getTotalCount() <= 0 || sites == null || sites.size() <= 0) {

                            return;
                        }
                        for (Site site : sites) {
                            ArrayList arr=new ArrayList();
                            arr.add(site.getName().toString());
                            arr.add(site.getFormatAddress().toString());
                            arr.add(site.getPoi().getPhone().toString());
                            arr.add(site.getDistance());
                            arr.add(site.getLocation().getLat());
                            arr.add(site.getLocation().getLng());
                            //arr.add(site.getPoi().getOpeningHours().toString());
                            //arr.add(site.getPoi().getPhotoUrls().);
                            arr.add(site.getPoi().getRating());
                            //arr.add(site.getPoi().getWebsiteUrl().toString());
                            allsites.add(arr);
                            listItems.add(site.getName()+"\n"+site.getFormatAddress()+"\n"+site.getLocation().getLat()+"\n"+site.getLocation().getLng()+"\n"+site.getDistance());
                            Log.i("TAG", String.format("siteId: '%s', name: %s\r\n", site.getSiteId(), site.getName()));
                        }
                        recyclerView = findViewById(R.id.recycler_view);
                        siteadapter = new SiteAdapter(allsites);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(SiteKitVIew.this);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(siteadapter);

                        siteadapter.notifyDataSetChanged();
//
//                        adapter.notifyDataSetChanged();

                    }
                    // Return the result code and description upon a search exception.
                    @Override
                    public void onSearchError(SearchStatus status) {
                        Log.i("TAG", "Error : " + status.getErrorCode() + " " + status.getErrorMessage());
                    }
                };
// Call the nearby place search API.
        searchService.nearbySearch(request, resultListener);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.GotoMap:
                    Intent myIntent = new Intent(this, MapView.class);
                    myIntent.putExtra("lat",this.latitude);
                    myIntent.putExtra("lon",this.longitude);
                    myIntent.putExtra("allsites",allsites); //Put your id to your next Intent
                    startActivity(myIntent);
                    overridePendingTransition(0,0);
                default:
                    break;
            }
        } catch (Exception e) {
            Log.e("TAG", "RequestLocationUpdatesWithCallbackActivity Exception:" + e);
        }
    }
}