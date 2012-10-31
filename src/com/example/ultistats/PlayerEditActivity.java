package com.example.ultistats;

import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.ultistats.model.Player;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayerEditActivity extends FragmentActivity {
	
	private String playerId;
	private EditText fnameEditText;
	private EditText lnameEditText;
	private EditText numberEditText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.player_edit);
        
        playerId = intent.getStringExtra(PlayerViewActivity.PLAYER_ID);
        fnameEditText = (EditText) findViewById(R.id.edit_fname);
        lnameEditText = (EditText) findViewById(R.id.edit_lname);
        numberEditText = (EditText) findViewById(R.id.edit_number);
        
        //Creating a new player
        if (playerId == null)
        	return;
        
        Cursor cursor = getContentResolver().query(
	        Uri.withAppendedPath(Player.CONTENT_URI, playerId), null, null, null, null);
        
        cursor.moveToFirst();
        String fname = cursor.getString(cursor.getColumnIndex(Player.FIRST_NAME_COLUMN));
        fnameEditText.setText(fname);
        
        String lname = cursor.getString(cursor.getColumnIndex(Player.LAST_NAME_COLUMN));
        lnameEditText.setText(lname);
        
        String number = cursor.getString(cursor.getColumnIndex(Player.NUMBER_COLUMN));
        numberEditText.setText(number);
        
        cursor.close();
    }
    
    public void savePlayer(View view) {
    	ContentValues playerValues = new ContentValues();

    	String fname = fnameEditText.getText().toString();
    	String lname = lnameEditText.getText().toString();
    	String number = numberEditText.getText().toString();
    	
    	if (fname.length() == 0 && lname.length() == 0) {
    		fnameEditText.setError("First name required");
    		fnameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
    		    @Override
    		    public void onFocusChange(View v, boolean hasFocus) {
    		    	fnameEditText.setError(null);
    		    }
    		});
    		return;
    	}
    	
		playerValues.put(Player.FIRST_NAME_COLUMN, fname); 
		playerValues.put(Player.LAST_NAME_COLUMN, lname);
		playerValues.put(Player.NUMBER_COLUMN, number);
		
    	if (playerId == null)
	    	getContentResolver().insert(Player.NEW_URI, playerValues);
    	else  {
	    	String selectionClause = "_id = ?";
	    	String[] selectionArgs = {playerId};
	    	getContentResolver().update(
	    		Uri.withAppendedPath(Player.CONTENT_URI, playerId), playerValues, selectionClause, selectionArgs 
	    	); 
    	}
    	
    	finish();
    }

    /**************************************************************************
     * Menu Actions ***********************************************************
     **************************************************************************/
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.player_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentActivityIntent = new Intent(this, PlayerGroupActivity.class);
                parentActivityIntent.addFlags(
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentActivityIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
