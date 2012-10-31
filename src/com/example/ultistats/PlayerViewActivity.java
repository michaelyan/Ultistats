package com.example.ultistats;

import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
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

public class PlayerViewActivity extends FragmentActivity {
    private String mPlayerId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mPlayerId = intent.getStringExtra(Player.PLAYER_ID_COLUMN);
        setContentView(R.layout.player_entry);
        
        Cursor cursor = getContentResolver().query(
	        Uri.withAppendedPath(Player.CONTENT_URI, mPlayerId), null, null, null, null);
        
        cursor.moveToFirst();
        String fname = cursor.getString(cursor.getColumnIndex("fname"));
        TextView fnameTextView = (TextView) findViewById(R.id.fname);
        fnameTextView.setText(fname);

        String lname = cursor.getString(cursor.getColumnIndex("lname"));
        TextView lnameTextView = (TextView) findViewById(R.id.lname);
        lnameTextView.setText(lname);

        String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
        TextView nicknameTextView = (TextView) findViewById(R.id.nickname);
        nicknameTextView.setText(nickname);

        String number = cursor.getString(cursor.getColumnIndex("number"));
        TextView numberTextView = (TextView) findViewById(R.id.number);
        numberTextView.setText(number);
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
            case R.id.player_edit:
                Intent intent = new Intent(this, PlayerEditActivity.class);
                intent.putExtra(Player.PLAYER_ID_COLUMN, String.valueOf(mPlayerId));
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}