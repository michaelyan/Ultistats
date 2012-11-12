package com.example.ultistats;

import java.io.*;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import com.example.ultistats.model.Base;

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

<<<<<<< HEAD
//        Base base = new Base(this);
//        base.copyDatabase();
        
//        Player p = new Player(this);
//        Cursor cursor = p.listPlayers();
//        
//        String data = "";
//        
//    	if (cursor.moveToFirst()) {
//            do {
//                Log.d(cursor.getString(0), cursor.getString(0));
//                Log.d(cursor.getString(0), cursor.getString(0));
//                data += cursor.getString(1);
//                data += ',';
//                data += cursor.getString(2);
//                data += '\n';
//            } while (cursor.moveToNext());
//        }
        
    	TextView a = new TextView(this);

    	a.setText("nooabo33");
    	setContentView(a);
=======
        Base base = new Base(this);
        base.copyDatabase();

        setContentView(R.layout.home_activity);
>>>>>>> yan
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    public void playerGroupActivity(View view) {
        startActivity(new Intent(this, PlayerGroupActivity.class));
    }
}
