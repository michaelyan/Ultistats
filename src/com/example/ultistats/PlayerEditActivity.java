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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PlayerEditActivity extends FragmentActivity {
	
	private String playerId;
	private EditText editFnameEditText;
	private EditText editLnameEditText;
	private EditText editNumberEditText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        playerId = intent.getStringExtra(PlayerListActivity.PLAYER_ID);
        setContentView(R.layout.player_edit);
        
        Cursor cursor = getContentResolver().query(
	        Uri.withAppendedPath(Player.CONTENT_URI, playerId), null, null, null, null);
        
        cursor.moveToFirst();
        String fname = cursor.getString(cursor.getColumnIndex("fname"));
        editFnameEditText = (EditText) findViewById(R.id.edit_fname);
        editFnameEditText.setText(fname);
        
        String lname = cursor.getString(cursor.getColumnIndex("lname"));
        editLnameEditText = (EditText) findViewById(R.id.edit_lname);
        editLnameEditText.setText(lname);
        
        String number = cursor.getString(cursor.getColumnIndex("number"));
        editNumberEditText = (EditText) findViewById(R.id.edit_number);
        editNumberEditText.setText(number);
        
        cursor.close();
    }
    
    public int savePlayer(View view) {
//        getContentResolver().insert(
//	        Uri.withAppendedPath(Player.CONTENT_URI, playerID), null, null, null, null);
    	// Defines an object to contain the updated values
    	ContentValues updateValues = new ContentValues();
    	int rowsUpdated;

		updateValues.put("fname", editFnameEditText.getText().toString());
		updateValues.put("lname", editLnameEditText.getText().toString());
		updateValues.put("number", editNumberEditText.getText().toString());
		
    	// Defines selection criteria for the rows you want to update
    	String selectionClause = "_id = ?";
    	String[] selectionArgs = {playerId};

    	// Defines a variable to contain the number of updated rows
    	/*
    	 * Sets the updated value and updates the selected words.
    	 */

    	rowsUpdated = getContentResolver().update(
    	    Player.CONTENT_URI,   // the user dictionary content URI
    	    updateValues,                       // the columns to update
    	    selectionClause,                    // the column to select on
    	    selectionArgs                      // the value to compare to
    	); 
    	
    	return rowsUpdated;
    }
}
