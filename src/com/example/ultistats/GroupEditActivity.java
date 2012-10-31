package com.example.ultistats;

import android.app.ActionBar;
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
	
	private String mGroupId;
	private EditText mGroupNameEditText;
	private ListView mCurrentPlayerListView;
	private ListView mOtherPlayerListView;
    private SimpleCursorAdapter mGroupAdapter;
    private SimpleCursorAdapter mGroupExclusiveAdapter;
    
    //Whether it is a new group being created
    private boolean mNewGroupFlag = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_edit);

        mGroupId = getOrCreateGroup();
        mGroupNameEditText = (EditText) findViewById(R.id.edit_group_name);
        
        Bundle bundle = new Bundle();
        bundle.putString(Group.GROUP_ID_COLUMN, mGroupId);

        getSupportLoaderManager().initLoader(Group.GROUP_CODE, bundle, this);
        getSupportLoaderManager().initLoader(Group.GROUP_EXCLUSIVE_CODE, bundle, this);

        setupGroupName();
        setupAdapter();
        bindPlayerClick();
    }
    
    public void onDestroy() {
    	//They were in the middle of making a group and exited, so remove the entries from the database
    	super.onDestroy();
    	if (mNewGroupFlag) {
			getContentResolver().delete(Group.DELETE_GROUP_URI, null, new String[]{ mGroupId });
    	}
    }

    /**
     * Gets the group id of the intent that started this activity. Otherwise create a new group and gets the new id.
     * @return The id of the group that was in the intent or the newly created group.
     */
    public String getOrCreateGroup() {
        Intent intent = getIntent();
        String intentGroupId = intent.getStringExtra(Group.GROUP_ID_COLUMN);

        if (intentGroupId != null) {
            return intentGroupId;
        } else {
            mNewGroupFlag = true;
            Uri insertedUri = getContentResolver().insert(Group.NEW_URI, null);
            String mNewGroupId = insertedUri.getLastPathSegment();
            return mNewGroupId;
        }
    }

    public void setupGroupName() {
        Cursor cursor = getContentResolver().query(
                Uri.withAppendedPath(Group.GROUP_NAME_URI, mGroupId), null, null, null, null);
        if (cursor.moveToFirst()) {
            String mGroupName = cursor.getString(cursor.getColumnIndex(Group.GROUP_NAME_COLUMN));
            mGroupNameEditText.setText(mGroupName);
        }
    }

    public void setupAdapter() {
        //The columns that should be bound to the UI
        String[] columns = new String[] { Player.FIRST_NAME_COLUMN, Player.LAST_NAME_COLUMN, Player.NUMBER_COLUMN };
        //The textviews that will display the data
        int[] to = new int[] { R.id.fname, R.id.lname, R.id.number };
        
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        mGroupAdapter = new SimpleCursorAdapter(
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?
        
        mGroupExclusiveAdapter = new SimpleCursorAdapter(
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?

        mCurrentPlayerListView = (ListView) findViewById(R.id.current_players);
        mCurrentPlayerListView.setAdapter(mGroupAdapter);
        
        mOtherPlayerListView = (ListView) findViewById(R.id.other_players);
        mOtherPlayerListView.setAdapter(mGroupExclusiveAdapter);
    }
    
    /**************************************************************************
     * Click Actions **********************************************************
     **************************************************************************/
    public void bindPlayerClick() {
	    mCurrentPlayerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				int deleted = getContentResolver().delete(
			        Group.DELETE_PLAYER_FROM_GROUP_URI, null, new String[]{String.valueOf(id), mGroupId});
		    }     
	    });
	    
	    mOtherPlayerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	ContentValues newPlayerGroupValues = new ContentValues();
				newPlayerGroupValues.put(Group.GROUP_ID_JOIN_COLUMN, mGroupId);
				newPlayerGroupValues.put(Player.PLAYER_ID_JOIN_COLUMN, String.valueOf(id));
				getContentResolver().insert(
			        Group.INSERT_PLAYER_INTO_GROUP_URI, newPlayerGroupValues);
		    }     
	    });
    }
    
    /**************************************************************************
     * Menu Actions ***********************************************************
     **************************************************************************/
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.group_edit_menu, menu);
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
            case R.id.group_save:
            	saveGroup(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void saveGroup(MenuItem item) {
    	ContentValues groupValues = new ContentValues();
    	
    	String mGroupName = mGroupNameEditText.getText().toString();
    	
    	if (mGroupName.length() == 0) {
    		mGroupNameEditText.setError("Group name required");
    		mGroupNameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
    		    @Override
    		    public void onFocusChange(View v, boolean hasFocus) {
    		    	mGroupNameEditText.setError(null);
    		    }
    		});
    		return;
    	}
    	
		groupValues.put(Group.GROUP_NAME_COLUMN, mGroupName);

    	String selectionClause = "_id = ?";
    	String[] selectionArgs = {mGroupId};
    	getContentResolver().update(
    		Uri.withAppendedPath(Group.CONTENT_URI, mGroupId), groupValues, selectionClause, selectionArgs
    	); 
    	
    	//When saving a group, don't call onDestroy()
    	mNewGroupFlag = false;
    	finish();
    }

    /**************************************************************************
     * Loader Functions *******************************************************
     **************************************************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
    	String mGroupId = bundle.getString(Group.GROUP_ID_COLUMN);
        CursorLoader cursorLoader;
        switch (id) {
            case Group.GROUP_CODE:
                cursorLoader = new CursorLoader(getApplicationContext(),
			        Uri.withAppendedPath(Group.GROUP_URI, mGroupId), null, null, null, null);
                break;
            case Group.GROUP_EXCLUSIVE_CODE:
                cursorLoader = new CursorLoader(getApplicationContext(),
			        Uri.withAppendedPath(Group.GROUP_EXCLUSIVE_URI, mGroupId), null, null, null, null);
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
        	mGroupAdapter.swapCursor(cursor);
        else if (loader.getId() == Group.GROUP_EXCLUSIVE_CODE) 
        	mGroupExclusiveAdapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
    	mGroupAdapter.swapCursor(null);
    	mGroupExclusiveAdapter.swapCursor(null);
    }
}
