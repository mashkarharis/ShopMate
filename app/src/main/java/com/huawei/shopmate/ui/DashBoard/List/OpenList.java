package com.huawei.shopmate.ui.DashBoard.List;
import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.shopmate.R;
import com.huawei.shopmate.SQLiteManager.SQLite;

public class OpenList extends Activity {
    private Spinner dropdown;
    private ListView lvItem;

    private ImageView qr;

    private Button delete;
    private Button share;

    private ArrayList<String> itemArrey;
    private ArrayAdapter<String> itemAdapter;

    private ArrayList<String> spinArrey;
    private ArrayAdapter spinadapter;

    int selected=0;

    ArrayList alllists;
    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        finish();
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_list_layout);
        setUpView();

    }

    private void setUpView() {
        qr=(ImageView) this.findViewById(R.id.qr);
        qr.setVisibility(View.GONE);
        delete=(Button) this.findViewById(R.id.delete_list);
        share=(Button) this.findViewById(R.id.share_list);
        dropdown = (Spinner) this.findViewById(R.id.spinner1);
        lvItem = (ListView)this.findViewById(R.id.listView_open);
        alllists = new ArrayList<>();

        itemArrey = new ArrayList<String>();
        itemArrey.clear();

        spinArrey = new ArrayList<String>();
        spinArrey.clear();

        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArrey);
        lvItem.setAdapter(itemAdapter);

        spinadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinArrey);
        dropdown.setAdapter(spinadapter);

        filldropdown();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String toQR = ((ArrayList) alllists.get(selected)).get(2).toString() + "";
                    System.out.println("QR-" + toQR);
                    generateQR(toQR);
                }catch (Exception ex){
                    System.out.println(ex.toString());
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String epoch = ((ArrayList) alllists.get(selected)).get(0).toString()+"";
                    System.out.println(epoch);
                    SQLite sqLite = new SQLite(OpenList.this);
                    SQLiteDatabase mydb = sqLite.getWritableDatabase();
                    mydb.execSQL("delete from LISTTABLE where epoch="+epoch+";");
                    finish();
                }catch (Exception ex){System.out.println("////"+ex.toString());}
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemArrey.clear();
                qr.setVisibility(View.GONE);
                String items=((ArrayList)alllists.get(position)).get(2).toString();
                System.out.println("^^^^^^"+items);
                itemArrey.addAll(StringtoArray(items));
                itemAdapter.notifyDataSetChanged();
                selected=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        } );

    }

    private void generateQR(String toQR) {
        int type = HmsScan.QRCODE_SCAN_TYPE;
        int width = 400;
        int height = 400;
        HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.BLACK).setBitmapMargin(5).create();
        try {
            // If the HmsBuildBitmapOption object is not constructed, set options to null.
            Bitmap qrBitmap = ScanUtil.buildBitmap(toQR, type, width, height, options);
            qr.setVisibility(View.VISIBLE);
            qr.setImageBitmap(qrBitmap);
            sharePalette(qrBitmap);


        } catch (WriterException e) {
            Log.w("buildBitmap", e);
        }
    }
    private void sharePalette(Bitmap bitmap) {

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "palette", "share palette");
        Uri bitmapUri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        startActivity(Intent.createChooser(intent, "Share"));
        Toast.makeText(this,"Saved to gallery" , Toast.LENGTH_SHORT).show();
    }

    private ArrayList StringtoArray(String items) {
        ArrayList<String> arr=new ArrayList<>();
        String trimmed=items.substring(1,items.length()-2);
        System.out.println("####"+trimmed);
        String[] splitted=trimmed.split("@");
        for (int i=0;i<splitted.length;i++){
            arr.add(splitted[i]);
            System.out.println("$#"+splitted[i]);
        }
        return arr;
    }

    private void filldropdown() {
        SQLite sqlite=new SQLite(this);
        SQLiteDatabase mydb=sqlite.getReadableDatabase();



        String selectQuery = "SELECT  * FROM LISTTABLE";

        Cursor cursor = mydb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                spinArrey.add(cursor.getString(1));
                ArrayList<String> arr=new ArrayList<>();
                arr.add(cursor.getDouble(0)+"");
                arr.add(cursor.getString(1));
                arr.add(cursor.getString(2));
                // Adding contact to list
                alllists.add(arr);
            } while (cursor.moveToNext());
        }

        spinadapter.notifyDataSetChanged();
    }


}