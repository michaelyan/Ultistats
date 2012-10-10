package com.example.ultistats;

import com.example.ultistats.model.Base;
import com.example.ultistats.model.Player;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
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

public class PlayerListActivity extends LoaderActivity {

	//When you click on a player, store the id of the clicked player here so you can pass it on
	public static final String PLAYER_ID = "com.example.ultistats.player_id";
    private ListView listView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_example);

        //Asynchronously load the data  for the player list
        getSupportLoaderManager().initLoader(0, null, this);
        
        //Copy the database to the phone
        Base b = new Base(this);
        b.copyDatabase();

        String[] columns = new String[] {
            "fname", "lname", "number"
        };
        int[] to = new int[] {
            R.id.fname, R.id.lname, R.id.number
        };
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        adapter = new SimpleCursorAdapter(
	        this, R.layout.list_example_entry, null, columns, to, 0); //what flags?

        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
//        listView.setClickable(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
//                String selection = String.valueOf(id);
//                Toast.makeText(getApplicationContext(), selection, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra(PLAYER_ID, String.valueOf(id));
                startActivity(intent);
            }
        });

    }
    
    public void displayGroups(View view) {
    	Intent intent = new Intent(this, GroupListActivity.class);
    	startActivity(intent);
    }

    @Override
    public Loader <Cursor> onCreateLoader(int arg0, Bundle arg1) {
    	//What data to get
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
	        Uri.withAppendedPath(Player.CONTENT_URI, "all"), null, null, null, null);
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