package com.example.ultistats;

import com.example.ultistats.model.Group;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;

public class GroupEditActivity extends FragmentActivity {
	
	private String groupId;
	private EditText groupNameEditText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.group_edit);
        
        groupId = intent.getStringExtra(GroupListActivity.GROUP_ID);
        groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
        
        //Creating a new group
        if (groupId == null)
        	return;
        
        Cursor cursor = getContentResolver().query(
	        Uri.withAppendedPath(Group.CONTENT_URI, groupId), null, null, null, null);
        
        cursor.moveToFirst();
        cursor.close();
    }
    
    public void saveGroup(View view) {
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

    	if (groupId == null) {
	    	getContentResolver().insert(
	    		Uri.withAppendedPath(Group.CONTENT_URI, Group.NEW_URI), groupValues
    		);
    	} else {
	    	String selectionClause = "_id = ?";
	    	String[] selectionArgs = {groupId};
	    	getContentResolver().update(
	    		Uri.withAppendedPath(Group.CONTENT_URI, groupId), groupValues, selectionClause, selectionArgs 
	    	); 
    	}
    	
    	finish();
    }
}
