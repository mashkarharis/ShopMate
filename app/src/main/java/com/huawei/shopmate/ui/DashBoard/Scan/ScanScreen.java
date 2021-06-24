package com.huawei.shopmate.ui.DashBoard.Scan;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.shopmate.R;
import com.huawei.shopmate.SQLiteManager.SQLite;
import com.huawei.shopmate.ui.DashBoard.List.ListMenu;
import com.huawei.shopmate.ui.DashBoard.MapnSite.MapnSiteScreen;

public class ScanScreen extends AppCompatActivity implements View.OnClickListener{
    public static final int DEFAULT_VIEW = 0x22;
    private static final int REQUEST_CODE_SCAN = 0X01;
    private TextView resulttext;
    private EditText name;
    private ImageButton save_list;

    String itemlist="";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1=new Intent(ScanScreen.this, ListMenu.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_screen_layout);

        resulttext=(TextView) findViewById(R.id.resulttext);
        name=(EditText) findViewById(R.id.name_for_list);
        save_list=(ImageButton)  findViewById(R.id.addtolist);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                name.setText("");
            }
        });
        resulttext.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        save_list.setVisibility(View.INVISIBLE);


        findViewById(R.id.scan_enable_button).setOnClickListener(this);
        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.item2);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.item1:
                    Intent intent1=new Intent(ScanScreen.this, ListMenu.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    overridePendingTransition(0,0);


                    break;
                case R.id.item2:
                    break;
                case R.id.item3:
                    Intent intent=new Intent(ScanScreen.this, MapnSiteScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
            }
            return true;
        });

        save_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().length()<1){
                    name.setError("Add Name");
                    return;
                }
                if(itemlist.length()<=1){
                    name.setError("Please Rescan");
                    return;
                }
                try{
                    SQLite sqlite=new SQLite(ScanScreen.this);
                    SQLiteDatabase mydb=sqlite.getWritableDatabase();
                    ContentValues cn=new ContentValues();
                    cn.put("epoch",System.currentTimeMillis());
                    cn.put("listname",name.getText().toString().trim());
                    cn.put("itemstring",itemlist);
                    mydb.insert("LISTTABLE",null,cn);
                    Toast.makeText(ScanScreen.this, "LIST ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();


                }catch (Exception ex){
                    Toast.makeText(ScanScreen.this, ex.toString(), Toast.LENGTH_SHORT).show();

                }finally {
                    resulttext.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    save_list.setVisibility(View.INVISIBLE);
                    itemlist="";
                }
            }
        });
    }
    public void newViewBtnClick(View view){
        resulttext.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        save_list.setVisibility(View.INVISIBLE);
        itemlist="";
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                DEFAULT_VIEW
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null || grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (requestCode == DEFAULT_VIEW) {
            //A new screen will open to scan barcode and the control will go to Scan kit SDK. HmsScan.CODE128_SCAN_TYPE
            ScanUtil.startScan(ScanScreen.this, REQUEST_CODE_SCAN, null);
            //Once the barcode is detected the control will again come back to parent application inside onActivityResult()method.}}
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN) {
            Object obj = data.getParcelableExtra(ScanUtil.RESULT);
            //result u will get
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    //Toast.makeText(this, ((HmsScan) obj).getOriginalValue(), Toast.LENGTH_SHORT).show();
                    //displayStringTextView.setText(((HmsScan) obj).getOriginalValue());
                    processstring(((HmsScan) obj).getOriginalValue());
                }
                return;
            }
        }
    }
    private int toitems(String s){
        boolean cond1=s.contains("@");
        boolean cond2=s.contains("{");
        boolean cond3=s.contains("}");
        boolean cond4=s.charAt(0)=='{';
        boolean cond5=s.charAt(s.length()-1)=='}';
        if(cond1&&cond2&&cond3&&cond4&&cond5){
            int count=0;
            for (int i=0;i<s.length();i++){
                if (s.charAt(i)=='@'){
                    count+=1;
                }
            }
            return count;
        }
        else{
            return -1;
        }
    }

    private void processstring(String originalValue) {
        int items=toitems(originalValue);
        if(items>0){
            resulttext.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            save_list.setVisibility(View.VISIBLE);
            resulttext.setText(items+" Items Found");
            itemlist=originalValue;
        }else{
            resulttext.setVisibility(View.VISIBLE);
            name.setVisibility(View.INVISIBLE);
            save_list.setVisibility(View.INVISIBLE);
            resulttext.setText("Invalid QR");
            itemlist="";
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_enable_button:
                newViewBtnClick(view);
                break;
            default:
                break;
        }

    }


}
