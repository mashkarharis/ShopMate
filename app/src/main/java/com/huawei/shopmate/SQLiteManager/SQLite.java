package com.huawei.shopmate.SQLiteManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.huawei.shopmate.MainActivity;

import java.time.Instant;
import java.util.Date;

public class SQLite  extends SQLiteOpenHelper {




    // init
    //create list 1->2
    //delete list 2->1

    //add item
    //delete item
    public static final int DB_VERSION=1;
    public static final String DB_NAME="shopmate.db";
    public static final String TABLE_NAME_FOR_LIST="LISTTABLE";

    public static final String ITEMTABLE_C1="epoch";
    public static final String ITEMTABLE_C2="listname";
    public static final String ITEMTABLE_C3="itemstring";


    private static final String SQL_CREATE_ENTRIES_1 =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FOR_LIST +
                    " ( epoch INTEGER PRIMARY KEY," +
                    "   listname TEXT," +
                    " itemstring TEXT );";

    public SQLite(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        System.out.println("OKKKKKKKKKKKKKKK1");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_ENTRIES_1);

            System.out.println("OKKKKKKKKKKKKKKK3");
        }
        catch (Exception ex){
            System.out.println("EX1212211"+ex.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


//    public void additem(String dhal, String cooking, MainActivity mainActivity) {
//        try {
//            SQLiteDatabase mydb = (new SQLite(mainActivity)).getWritableDatabase();
//            ContentValues cn = new ContentValues();
//            cn.put(ITEMTABLE_C1, dhal);
//            cn.put(ITEMTABLE_C2, System.currentTimeMillis());
//            cn.put(ITEMTABLE_C3, cooking);
//
//            mydb.insert(TABLE_NAME_FOR_ITEM, null, cn);
//        }catch (Exception ex){
//            System.out.println(ex.toString());
//        }
//    }
}
