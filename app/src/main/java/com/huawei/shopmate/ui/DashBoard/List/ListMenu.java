package com.huawei.shopmate.ui.DashBoard.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.shopmate.R;
import com.huawei.shopmate.ui.DashBoard.MapnSite.MapnSiteScreen;
import com.huawei.shopmate.ui.DashBoard.Scan.ScanScreen;

public class
ListMenu extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_menu_layout);
        findViewById(R.id.new_list).setOnClickListener(this);
        findViewById(R.id.open_list).setOnClickListener(this);
        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.item1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1:
                        break;

                    case R.id.item2:
                        Intent intent1=new Intent(ListMenu.this, ScanScreen.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        //startActivity(new Intent(ListMenu.this, ScanScreen.class));

                        break;
                    case R.id.item3:
                        Intent intent2=new Intent(ListMenu.this, MapnSiteScreen.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_list:
                startActivity(new Intent(this, NewList.class));
                overridePendingTransition(0,0);
                break;
            case R.id.open_list:
                startActivity(new Intent(this, OpenList.class));
                overridePendingTransition(0,0);
                break;
            default:
                break;
        }

    }
}
