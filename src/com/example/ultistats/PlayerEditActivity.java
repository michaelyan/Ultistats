package com.example.ultistats;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import com.example.ultistats.model.Player;

public class PlayerEditActivity extends FragmentActivity {
	
	private String mPlayerId;
	private EditText mFnameEditText;
	private EditText mLnameEditText;
    private EditText mNicknameEditText;
	private EditText mNumberEditText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.player_edit);
        
        mPlayerId = intent.getStringExtra(Player.PLAYER_ID_COLUMN);
        mFnameEditText = (EditText) findViewById(R.id.edit_fname);
        mLnameEditText = (EditText) findViewById(R.id.edit_lname);
        mNicknameEditText = (EditText) findViewById(R.id.edit_nickname);
        mNumberEditText = (EditText) findViewById(R.id.edit_number);
        
        //Creating a new player
        if (mPlayerId == null) {
            return;
        //Populate the edit text fields with the player info
        } else {
            Cursor cursor = getContentResolver().query(
                    Player.PLAYER_URI, null, null, new String[]{ mPlayerId }, null);

            cursor.moveToFirst();
            String fname = cursor.getString(cursor.getColumnIndex(Player.FIRST_NAME_COLUMN));
            mFnameEditText.setText(fname);

            String lname = cursor.getString(cursor.getColumnIndex(Player.LAST_NAME_COLUMN));
            mLnameEditText.setText(lname);

            String nickname = cursor.getString(cursor.getColumnIndex(Player.NICKNAME_COLUMN));
            mNicknameEditText.setText(nickname);

            String number = cursor.getString(cursor.getColumnIndex(Player.NUMBER_COLUMN));
            mNumberEditText.setText(number);

            cursor.close();
        }
    }
    
    public void savePlayer(View view) {
    	ContentValues playerValues = new ContentValues();

    	String fname = mFnameEditText.getText().toString();
    	String lname = mLnameEditText.getText().toString();
        String nickname = mNicknameEditText.getText().toString();
    	String number = mNumberEditText.getText().toString();

    	if (fname.length() == 0 && lname.length() == 0) {
    		mFnameEditText.setError("First name required");
    		mFnameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
    		    @Override
    		    public void onFocusChange(View v, boolean hasFocus) {
    		    	mFnameEditText.setError(null);
    		    }
    		});
    		return;
    	}
    	
		playerValues.put(Player.FIRST_NAME_COLUMN, fname); 
		playerValues.put(Player.LAST_NAME_COLUMN, lname);
        playerValues.put(Player.NICKNAME_COLUMN, nickname);
		playerValues.put(Player.NUMBER_COLUMN, number);

    	if (mPlayerId == null)
	    	getContentResolver().insert(Player.NEW_URI, playerValues);
    	else  {
	    	String selectionClause = "_id = ?";
	    	String[] selectionArgs = {mPlayerId};
	    	getContentResolver().update(
	    		Player.PLAYER_URI, playerValues, selectionClause, selectionArgs
	    	); 
    	}
    	
    	finish();
    }

    public static void deletePlayer(final String playerId, AlertDialog.Builder builder, final Activity activity) {
        builder
            .setTitle(R.string.player_delete_confirm_title)
            .setMessage(R.string.player_delete_confirm_message)
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activity.getContentResolver().delete(
                            Player.DELETE_URI, null, new String[]{playerId});
                    Intent parentActivityIntent = new Intent(activity, PlayerGroupActivity.class);
                    activity.startActivity(parentActivityIntent);
                }
            })
            .show();

    }

    /**************************************************************************
     * Menu Actions ***********************************************************
     **************************************************************************/
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.player_edit_menu_2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentActivityIntent = new Intent(this, PlayerGroupActivity.class);
                startActivity(parentActivityIntent);
                finish();
                return true;
            case R.id.player_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                PlayerEditActivity.deletePlayer(mPlayerId, builder, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
