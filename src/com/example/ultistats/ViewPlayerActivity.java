package com.example.ultistats;

import com.example.ultistats.model.Base;
import com.example.ultistats.model.Player;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class ViewPlayerActivity extends FragmentActivity implements
	LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_example);


        //    	  DatabaseHelper dH = new DatabaseHelper(this);
        //    	  dH.getWritableDatabase();

        Cursor cursor = getContentResolver().query(Player.CONTENT_URI, new String[] {
            "fname", "lname", "_id"
        }, null, null, null);

        // some code
        // the desired columns to be bound
        String[] columns = new String[] {
            "_id", "fname", "lname"
        };
        // the XML defined views which the data will be bound to
        int[] to = new int[] {
            R.id.name_entry, R.id.number_entry
        };

        // create the adapter using the cursor pointing to the desired data as well as the layout information
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(
        	this, R.layout.list_example_entry, cursor, columns, to, 0);
        	
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        //        String data = "";
        //
        //        if (cursor.moveToFirst()) {
        //            do {
        //                Log.i(cursor.getString(0), cursor.getString(0));
        //                Log.i(cursor.getString(2), cursor.getString(2));
        //                data += cursor.getString(0);
        //                data += ',';
        //                data += cursor.getString(1);
        //                data += ',';
        //                data += cursor.getString(2);
        //                data += '\n';
        //            } while (cursor.moveToNext());
        //        }
        //
        //        TextView a = new TextView(this);
        //        a.setText("noob");
        //        setContentView(a);
    }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}