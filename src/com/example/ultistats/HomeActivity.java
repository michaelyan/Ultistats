package com.example.ultistats;

import java.io.*;
import com.example.ultistats.model.Player;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Player p = new Player(this);
        Cursor cursor = p.listPlayers();
        
        String data = "";
        
    	if (cursor.moveToFirst()) {
            do {
                Log.d(cursor.getString(0), cursor.getString(0));
                Log.d(cursor.getString(0), cursor.getString(0));
                data += cursor.getString(1);
                data += ',';
                data += cursor.getString(2);
                data += '\n';
            } while (cursor.moveToNext());
        }
    	
    	TextView a = new TextView(this);
    	a.setText(data);
    	
//        setContentView(R.layout.activity_home);
    	setContentView(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    public void takeStats(View view) {
    	startActivity(new Intent(this, TakeStatsActivity.class));
    }
}
