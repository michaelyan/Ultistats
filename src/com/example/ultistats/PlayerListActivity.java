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
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlayerListActivity extends LoaderActivity {

	//When you click on a player, store the id of the clicked player here so you can pass it on
	public static final String PLAYER_ID = "com.example.ultistats.player_id";
    private ListView listView;
    private ActionMode mActionMode;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list);

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
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?

        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
//        listView.setClickable(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra(PLAYER_ID, String.valueOf(id));
                startActivity(intent);
            }
        });
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
              if (mActionMode != null)
                return false;
              
              mActionMode = PlayerListActivity.this.startActionMode(mActionModeCallback);
              mActionMode.setTag(String.valueOf(id));
              view.setSelected(true);
              return true;
			}
          });
    }
    
    //How to get ids of current item clicked on?
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
          MenuInflater inflater = mode.getMenuInflater();
          inflater.inflate(R.menu.player_actions, menu);
          return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
          return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
          switch (item.getItemId()) {
          case R.id.player_edit:
                Intent intent = new Intent(getApplicationContext(), PlayerEditActivity.class);
                intent.putExtra(PLAYER_ID, (String) mode.getTag());
                startActivity(intent);
        	  
        	  Log.i("The id is ", String.valueOf(mode.getTag()));
            Toast.makeText(PlayerListActivity.this, "Selected player with id " + mode.getTag(),
                Toast.LENGTH_LONG).show();
            mode.finish();
            return true;
          default:
            return false;
          }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
          mActionMode = null;
        }
      };

    //Happens when you click on the button
    public void displayGroups(View view) {
    	Intent intent = new Intent(this, GroupListActivity.class);
    	startActivity(intent);
    }
    
    //Loader functions
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
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