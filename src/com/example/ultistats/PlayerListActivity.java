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
        getSupportLoaderManager().initLoader(0, null, this);
        
        //Copy the database to the phone
        Base b = new Base(this);
        b.copyDatabase();
        
        setupAdapter();
	    bindItemClick();
	    bindItemLongClick();
	}
    
    public void setupAdapter() {
        //The columns that should be bound to the UI
    	//don't hardcode this
        String[] columns = new String[] { "fname", "lname", "number" };
        //The textviews that will display the data
        int[] to = new int[] { R.id.fname, R.id.lname, R.id.number };
        
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        adapter = new SimpleCursorAdapter(
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?

        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }
    
    public void bindItemClick() {
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra(PLAYER_ID, String.valueOf(id));
                startActivity(intent);
            }
        });
    }
    
    public void bindItemLongClick() {
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
        	
		    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
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

		        @Override
		        public void onDestroyActionMode(ActionMode mode) {
		            actionMode = null;
		        }
		    };
	        	
        	@Override
            public boolean onItemLongClick(AdapterView <?> adapter, View view, int position, long id) {
                if (actionMode != null)
                	return false;

                actionMode = PlayerListActivity.this.startActionMode(actionModeCallback);
                actionMode.setTag(String.valueOf(id));
                view.setSelected(true);
                return true;
            }
        });
    }
    
    /**************************************************************************
     * Menu Actions ***********************************************************
     **************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.player_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    public void displayGroups(View view) {
    	Intent intent = new Intent(this, GroupListActivity.class);
    	startActivity(intent);
    }

    /**************************************************************************
     * Loader Functions *******************************************************
     **************************************************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
	        Uri.withAppendedPath(Player.CONTENT_URI, Player.ALL_URI), null, null, null, null);
        return cursorLoader;
    }
    
    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
    	//Make the cursor listen for changes in the database
    	cursor.setNotificationUri(
//    			this.getContentResolver(), Uri.withAppendedPath(Player.CONTENT_URI, Player.ALL_URI));
    			this.getContentResolver(), Player.CONTENT_URI);
    	adapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        adapter.swapCursor(null);
    }
}