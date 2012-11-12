package com.example.ultistats;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import com.example.ultistats.model.Player;

import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
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
                Player.PLAYER_URI, null, null, new String[]{ mPlayerId }, null);
        
        cursor.moveToFirst();
        String fname = cursor.getString(cursor.getColumnIndex(Player.FIRST_NAME_COLUMN));
        TextView fnameTextView = (TextView) findViewById(R.id.fname);
        fnameTextView.setText(fname);

        String lname = cursor.getString(cursor.getColumnIndex(Player.LAST_NAME_COLUMN));
        TextView lnameTextView = (TextView) findViewById(R.id.lname);
        lnameTextView.setText(lname);

        String nickname = cursor.getString(cursor.getColumnIndex(Player.NICKNAME_COLUMN));
        TextView nicknameTextView = (TextView) findViewById(R.id.nickname);
        nicknameTextView.setText(nickname);

        String number = cursor.getString(cursor.getColumnIndex(Player.NUMBER_COLUMN));
        TextView numberTextView = (TextView) findViewById(R.id.number);
        numberTextView.setText(number);
    }

    /**************************************************************************
     * Menu Actions ***********************************************************
     **************************************************************************/
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.player_edit_menu_3, menu);
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
                return true;
            case R.id.player_edit:
                Intent intent = new Intent(this, PlayerEditActivity.class);
                intent.putExtra(Player.PLAYER_ID_COLUMN, String.valueOf(mPlayerId));
                startActivity(intent);
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