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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;

public class GroupEditActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
	
	private String groupId;
	private EditText groupNameEditText;
	private ListView playerListView;
    private SimpleCursorAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.group_edit);
        
        groupId = intent.getStringExtra(GroupListActivity.GROUP_ID);
        if (groupId == null)
        	return;
        
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);
        
        getSupportLoaderManager().initLoader(Group.GROUP_CODE, bundle, this);
        setupAdapter();
    }
    
    //PUT THE RIGHT VIEWS IN HERE
    public void setupAdapter() {
        //The columns that should be bound to the UI
    	//don't hardcode this
        String[] columns = new String[] { Player.FIRST_NAME_COLUMN, Player.LAST_NAME_COLUMN, Player.NUMBER_COLUMN };
        //The textviews that will display the data
        int[] to = new int[] { R.id.fname, R.id.lname, R.id.number };
        
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        adapter = new SimpleCursorAdapter(
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?

        playerListView = (ListView) findViewById(android.R.id.list);
        playerListView.setAdapter(adapter);
    }
    
    /**************************************************************************
     * Menu Actions ***********************************************************
     **************************************************************************/
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.i("menu", "created");
        getMenuInflater().inflate(R.menu.group_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
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
    	
        groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
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
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
	        Uri.withAppendedPath(Group.CONTENT_URI, groupId), null, null, null, null);
        return cursorLoader;
    }
    
    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
    	adapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        adapter.swapCursor(null);
    }
}
