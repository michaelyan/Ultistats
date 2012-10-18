package com.example.ultistats;

import com.example.ultistats.model.Player;
import android.net.Uri;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.TextView;

public class PlayerEditActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String playerID = intent.getStringExtra(PlayerListActivity.PLAYER_ID);
        setContentView(R.layout.player_edit);
        
        Cursor cursor = getContentResolver().query(
	        Uri.withAppendedPath(Player.CONTENT_URI, playerID), null, null, null, null);
        
        cursor.moveToFirst();
        String fname = cursor.getString(cursor.getColumnIndex("fname"));
        TextView editFnameTextView = (TextView) findViewById(R.id.edit_fname);
        editFnameTextView.setText(fname);
        
        String lname = cursor.getString(cursor.getColumnIndex("lname"));
        TextView editLnameTextView = (TextView) findViewById(R.id.edit_lname);
        editLnameTextView.setText(lname);
        
        String number = cursor.getString(cursor.getColumnIndex("number"));
        TextView editNumberTextView = (TextView) findViewById(R.id.edit_number);
        editNumberTextView.setText(number);
    }
    
    public void savePlayer() {
//        getContentResolver().insert(
//	        Uri.withAppendedPath(Player.CONTENT_URI, playerID), null, null, null, null);
    	// Defines an object to contain the updated values
    	ContentValues mUpdateValues = new ContentValues();

    	// Defines selection criteria for the rows you want to update
    	String mSelectionClause = "_id = ?";
    	String[] mSelectionArgs = {"en_%"};

    	// Defines a variable to contain the number of updated rows
    	int mRowsUpdated = 0;
    	/*
    	 * Sets the updated value and updates the selected words.
    	 */
    	mUpdateValues.putNull(UserDictionary.Words.LOCALE);

    	mRowsUpdated = getContentResolver().update(
    	    UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
    	    mUpdateValues                       // the columns to update
    	    mSelectionClause                    // the column to select on
    	    mSelectionArgs                      // the value to compare to
    	); 
    }
}
