package com.example.ultistats;

import com.example.ultistats.model.Player;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.TextView;

public class ViewPlayerActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String playerID = intent.getStringExtra(ViewPlayerListActivity.PLAYER_ID);
        setContentView(R.layout.list_example_entry);
        
        Cursor cursor = getContentResolver().query(
//	        Uri.withAppendedPath(Player.CONTENT_URI, playerID), null, null, null, null);
	        Uri.withAppendedPath(Player.CONTENT_URI, playerID), null, null, null, null);
        
        cursor.moveToFirst();
        String fname = cursor.getString(cursor.getColumnIndex("fname"));
        TextView fnameTextView = (TextView) findViewById(R.id.fname);
        fnameTextView.setText(fname);
        
        String lname = cursor.getString(cursor.getColumnIndex("lname"));
        TextView lnameTextView = (TextView) findViewById(R.id.lname);
        lnameTextView.setText(lname);
        
        String number = cursor.getString(cursor.getColumnIndex("number"));
        TextView numberTextView = (TextView) findViewById(R.id.number);
        numberTextView.setText(number);
    }
}