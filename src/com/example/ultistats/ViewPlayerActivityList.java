package com.example.ultistats;

import com.example.ultistats.model.Base;
import com.example.ultistats.model.Player;

import android.net.Uri;
import android.os.Bundle;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewPlayerActivityList extends FragmentActivity implements
	LoaderManager.LoaderCallbacks <Cursor> {


    private SimpleCursorAdapter mAdapter;
    private ListView listView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_example);

        //Copy the database to the phone
        Base b = new Base(this);
        b.copyDatabase();

        String[] columns = new String[] {
            "fname", "lname", "number"
        };
        int[] to = new int[] {
            R.id.fname, R.id.lname, R.id.number_entry
        };
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        mAdapter = new SimpleCursorAdapter(
	        this, R.layout.list_example_entry, null, columns, to, 0); //what flags?

        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                String selection = String.valueOf(id);
                Toast.makeText(getApplicationContext(), selection, Toast.LENGTH_LONG).show();
            }
        });

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader <Cursor> onCreateLoader(int arg0, Bundle arg1) {
    	//What data to get
        CursorLoader cursorLoader = new CursorLoader(this,
	        Uri.withAppendedPath(Player.CONTENT_URI, "all"), null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}