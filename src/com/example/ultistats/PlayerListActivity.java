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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlayerListActivity extends LoaderActivity {

	//When you click on a player, this is the key of the value you are storing in the intent
	public static final String PLAYER_ID = "intent_player_id";
    private ListView listView;
    private ActionMode actionMode;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list);

        //Asynchronously load the data  for the player list
        getSupportLoaderManager().initLoader(0, null, this);
        
        //Copy the database to the phone
//        Base b = new Base(this);
//        b.copyDatabase();

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
              if (actionMode != null)
                return false;
              
              actionMode = PlayerListActivity.this.startActionMode(actionModeCallback);
              actionMode.setTag(String.valueOf(id));
              view.setSelected(true);
              return true;
			}
          });
    }
    
    /**
     * Menu stuff
     */
    @Override
    //Create the action bar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.player_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_player:
            	newPlayer(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void newPlayer(MenuItem item) {
    	Intent intent = new Intent(this, PlayerEditActivity.class);
    	startActivity(intent);
    }
    
    //How to get ids of current item clicked on?
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
          MenuInflater inflater = mode.getMenuInflater();
          inflater.inflate(R.menu.player_list_edit_menu, menu);
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
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
        	actionMode = null;
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
	        Uri.withAppendedPath(Player.CONTENT_URI, Player.ALL_URI), null, null, null, null);
        return cursorLoader;
    }
    
    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
    	cursor.setNotificationUri(this.getContentResolver(), Player.CONTENT_URI);
    	adapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        adapter.swapCursor(null);
    }
}