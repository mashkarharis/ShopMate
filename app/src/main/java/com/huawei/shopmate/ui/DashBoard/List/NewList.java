package com.huawei.shopmate.ui.DashBoard.List;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.huawei.shopmate.R;
import com.huawei.shopmate.SQLiteManager.SQLite;

public class NewList extends Activity {
    private TextView listid;
    private EditText listname;
    private ImageButton savebutton;
    private EditText etInput;
    private EditText qtInput;
    private Button btnAdd;
    private ListView lvItem;
    private ArrayList<String> itemArrey;
    private ArrayAdapter<String> itemAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlist);
        setUpView();

    }

    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        finish();
    }
    private void setUpView() {
        listid = (TextView)this.findViewById(R.id.listid);
        listname = (EditText)this.findViewById(R.id.listname);
        savebutton = (ImageButton)this.findViewById(R.id.savelist);
        etInput = (EditText)this.findViewById(R.id.editText_input);
        qtInput = (EditText)this.findViewById(R.id.qtyText_input);
        btnAdd = (Button)this.findViewById(R.id.button_add);
        lvItem = (ListView)this.findViewById(R.id.listView_items);

        listid.setText(String.valueOf(System.currentTimeMillis()));

        itemArrey = new ArrayList<String>();
        itemArrey.clear();

        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArrey);
        lvItem.setAdapter(itemAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItemList();
            }
        });


        savebutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(saveList()){
                    finish();
                }

            }
        });

        lvItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {

                itemArrey.remove(arg2);//where arg2 is position of item you click
                itemAdapter.notifyDataSetChanged();
                // Can't manage to remove an item here

                return false;
            }
        });
    }

    private boolean saveList() {
        try {
            String epoch = listid.getText().toString();
            String lsname = listname.getText().toString().trim();
            if (lsname.length() < 1) {
                listname.setError("Please Enter Valid List Name");
                return false;
            }
            if (itemArrey.size() < 1) {
                listname.setError("Empty List is not valid. Please Add Items");
                return false;
            }
            String itemstring = "{";
            for (String item : itemArrey) {
                itemstring = itemstring + item;
                itemstring = itemstring + "@";
            }
            itemstring = itemstring + "}";

            SQLite sqlite = new SQLite(this);
            SQLiteDatabase mydb = sqlite.getWritableDatabase();
            ContentValues cn = new ContentValues();
            cn.put("epoch", epoch);
            cn.put("listname", lsname);
            cn.put("itemstring", itemstring);
            mydb.insert("LISTTABLE", null, cn);
            return true;
        }catch (Exception ex){
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%"+ex.toString());
            return false;
        }

    }

    protected void addItemList() {
        if (isInputValid(etInput,qtInput)) {
            itemArrey.add(0,etInput.getText().toString()+" -> "+qtInput.getText().toString());
            etInput.setText("");
            qtInput.setText("");
            etInput.requestFocus();
            itemAdapter.notifyDataSetChanged();
        }
    }

    protected boolean isInputValid(EditText etInput2,EditText qtInput2) {
        if (etInput2.getText().toString().trim().length()<1 | etInput2.getText().toString().trim().contains("@") ) {
            etInput2.setError("Please Enter Valid Item");
            return false;
        } else {
            if (qtInput2.getText().toString().trim().length()<1 | qtInput2.getText().toString().trim().contains("@")) {
                qtInput2.setError("Please Enter Valid Qty");
                return false;
            }
            else {

                return true;
            }
        }
    }


}