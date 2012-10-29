package com.example.ultistats;

import com.example.ultistats.model.Base;
import com.example.ultistats.model.Player;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
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

public class PlayerListActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
 //When you click on a player, this is the key of the value you are storing in the intent
	public static final String PLAYER_ID = "intent_player_id";
    private ListView playerListView;
    private SimpleCursorAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list);
        getSupportLoaderManager().initLoader(Player.ALL_CODE, null, this);
        
        //Copy the database to the phone
        Base b = new Base(this);
        b.copyDatabase();
        
        setupAdapter();
	    bindPlayerClick();
	    bindPlayerLongClick();
	}
    
    public void setupAdapter() {
        //The columns that should be bound to the UI
        String[] columns = new String[] { Player.FIRST_NAME_COLUMN, Player.LAST_NAME_COLUMN, Player.NUMBER_COLUMN };
        //The TextViews that will display the data
        int[] to = new int[] { R.id.fname, R.id.lname, R.id.number };
        
        // create the adapter using the cursor pointing to the desired data as well as the layout information
        adapter = new SimpleCursorAdapter(
	        this, R.layout.player_list_entry, null, columns, to, 0); //what flags?

        playerListView = (ListView) findViewById(android.R.id.list);
        playerListView.setAdapter(adapter);
        playerListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    
    /**************************************************************************
     * Click Actions **********************************************************
     **************************************************************************/
    public void bindPlayerClick() {
        playerListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra(PLAYER_ID, String.valueOf(id));
                startActivity(intent);
            }
        });
    }
    
    public void bindPlayerLongClick() {
        playerListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		    private ActionMode actionMode;
		    
        	@Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                if (actionMode != null)
                	return false;

                String playerId = String.valueOf(id);
                actionMode = PlayerListActivity.this.startActionMode(actionModeCallback);
                actionMode.setTag(playerId);
                view.setSelected(true);
                return true;
            }

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
	        Player.ALL_URI, null, null, null, null);
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