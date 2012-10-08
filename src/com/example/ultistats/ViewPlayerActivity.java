package com.example.ultistats;

import com.example.ultistats.model.Player;

import android.os.Bundle;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ListView;

public class ViewPlayerActivity extends FragmentActivity implements
	LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_example);
        
        int[] to = new int[] {R.id.name_entry, R.id.number_entry};
        String[] columns = new String[] {"_id", "fname", "lname"};
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        mAdapter = new SimpleCursorAdapter(
        		this, R.layout.list_example_entry, null, columns, to, 0);
        	
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);
        
        getSupportLoaderManager().initLoader(0, null, this);
    }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		String[] projection = {"fname", "lname", "_id"};
		CursorLoader cursorLoader = new CursorLoader(this,
				Player.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);	
	}
}