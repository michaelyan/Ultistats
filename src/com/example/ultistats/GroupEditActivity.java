package com.example.ultistats;

import com.example.ultistats.model.Group;
import com.example.ultistats.model.Player;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;

public class GroupEditActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
	
	private String groupId;
	private EditText groupNameEditText;
	private ListView currentPlayerListView;
	private ListView otherPlayerListView;
    private SimpleCursorAdapter groupAdapter;
    private SimpleCursorAdapter groupExclusiveAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.group_edit);
        
        groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
        
        groupId = intent.getStringExtra(GroupListActivity.GROUP_ID);
        if (groupId == null)
        	return;
        
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);
        
        Cursor cursor = getContentResolver().query(
	        Uri.withAppendedPath(Group.GROUP_NAME_URI, groupId), null, null, null, null);
        
        cursor.moveToFirst();
        String groupName = cursor.getString(cursor.getColumnIndex(Group.GROUP_NAME_COLUMN));
        groupNameEditText.setText(groupName);
        
        getSupportLoaderManager().initLoader(Group.GROUP_CODE, bundle, this);
        getSupportLoaderManager().initLoader(Group.GROUP_EXCLUSIVE_CODE, bundle, this);
        
        setupAdapter();
        bindPlayerClick();
    }
    
    public void setupAdapter() {
        //The columns that should be bound to the UI
        String[] columns = new String[] { Player.FIRST_NAME_COLUMN, Player.LAST_NAME_COLUMN, Player.NUMBER_COLUMN };
        //The textviews that will display the data
        int[] to = new int[] { R.id.fname, R.id.lname, R.id.number };
        
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        groupAdapter = new SimpleCursorAdapter(
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?
        
        groupExclusiveAdapter = new SimpleCursorAdapter(
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?

        currentPlayerListView = (ListView) findViewById(R.id.current_players);
        currentPlayerListView.setAdapter(groupAdapter);
        
        otherPlayerListView = (ListView) findViewById(R.id.other_players);
        otherPlayerListView.setAdapter(groupExclusiveAdapter);
    }
    
    /**************************************************************************
     * Click Actions **********************************************************
     **************************************************************************/
    public void bindPlayerClick() {
	    currentPlayerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				int deleted = getContentResolver().delete(
			        Group.DELETE_PLAYER_FROM_GROUP_URI, "player_id=? AND group_id=?", new String[]{String.valueOf(id), groupId});
		    }     
	    });
	    
	    otherPlayerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	ContentValues newPlayerGroupValues = new ContentValues();
				newPlayerGroupValues.put(Group.GROUP_ID_COLUMN, groupId); 
				newPlayerGroupValues.put(Player.PLAYER_ID_COLUMN, String.valueOf(id)); 
				getContentResolver().insert(
			        Group.INSERT_PLAYER_INTO_GROUP_URI, newPlayerGroupValues);
		    }     
	    });
    }
    
    /**************************************************************************
     * Menu Actions ***********************************************************
     **************************************************************************/
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_save:
            	saveGroup(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void saveGroup(MenuItem item) {
    	ContentValues groupValues = new ContentValues();
    	
    	String groupName = groupNameEditText.getText().toString();
    	
    	if (groupName.length() == 0) {
    		groupNameEditText.setError("Group name required");
    		groupNameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
    		    @Override
    		    public void onFocusChange(View v, boolean hasFocus) {
    		    	groupNameEditText.setError(null);
    		    }
    		});
    		return;
    	}
    	
		groupValues.put(Group.GROUP_NAME_COLUMN, groupName); 

    	if (groupId == null)
	    	getContentResolver().insert(Group.NEW_URI, groupValues);
    	else {
	    	String selectionClause = "_id = ?";
	    	String[] selectionArgs = {groupId};
	    	getContentResolver().update(
	    		Uri.withAppendedPath(Group.CONTENT_URI, groupId), groupValues, selectionClause, selectionArgs 
	    	); 
    	}
    	
    	finish();
    }
    

    /**************************************************************************
     * Loader Functions *******************************************************
     **************************************************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
    	String groupId = bundle.getString("groupId");
        CursorLoader cursorLoader;
        switch (id) {
            case Group.GROUP_CODE:
                cursorLoader = new CursorLoader(getApplicationContext(),
			        Uri.withAppendedPath(Group.GROUP_URI, groupId), null, null, null, null);
                break;
            case Group.GROUP_EXCLUSIVE_CODE:
                cursorLoader = new CursorLoader(getApplicationContext(),
			        Uri.withAppendedPath(Group.GROUP_EXCLUSIVE_URI, groupId), null, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }
    
    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
    	//if ready
        if (loader.getId() == Group.GROUP_CODE) 
        	groupAdapter.swapCursor(cursor);
        else if (loader.getId() == Group.GROUP_EXCLUSIVE_CODE) 
        	groupExclusiveAdapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
    	groupAdapter.swapCursor(null);
    	groupExclusiveAdapter.swapCursor(null);
    }
}
