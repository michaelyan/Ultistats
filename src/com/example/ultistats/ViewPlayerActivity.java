package com.example.ultistats;

import com.example.ultistats.model.Base;
import com.example.ultistats.model.Player;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ViewPlayerActivity extends Activity {

	@Override
      public void onCreate(Bundle savedInstanceState) {
    	  super.onCreate(savedInstanceState);
    	  
    	  Base b = new Base(this);
    	  b.copyDatabase();
    	  
//    	  DatabaseHelper dH = new DatabaseHelper(this);
//    	  dH.getWritableDatabase();
    	  
    	  Cursor cursor = getContentResolver().query(Player.CONTENT_URI, new String[] {"fname", "lname"}, null, null, null);
          
    String data = "";
    
	if (cursor.moveToFirst()) {
        do {
            Log.i(cursor.getString(0), cursor.getString(0));
            Log.i(cursor.getString(0), cursor.getString(0));
            data += cursor.getString(0);
            data += cursor.getString(1);
            data += ',';
            data += '\n';
        } while (cursor.moveToNext());
    }
    	  
    	  TextView a = new TextView(this);
    	  a.setText(data);
    	  setContentView(a);
      }
}
