package com.huawei.shopmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.huawei.shopmate.SQLiteManager.SQLite;
import com.huawei.shopmate.ui.DashBoard.List.ListMenu;
import com.huawei.shopmate.ui.DashBoard.Scan.ScanScreen;
import com.huawei.shopmate.ui.LogIn.LogInScreen;
import com.huawei.shopmate.ui.LogIn.LogInScreen;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SQLite db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        findViewById(R.id.button1).setOnClickListener(this);


        db = new SQLite(this);
        SQLiteDatabase mydb = db.getReadableDatabase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Intent intent1=new Intent(this, LogInScreen.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                overridePendingTransition(0,0);
                break;
            default:
                break;
        }

    }
}