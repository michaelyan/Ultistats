package com.example.ultistats;

import com.example.ultistats.model.Base;
import com.example.ultistats.model.Player;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlayerListFragment extends Fragment implements LoaderCallbacks<Cursor> {
	
    private ListView mPlayerListView;
    private SimpleCursorAdapter mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(Player.ALL_CODE, null, this);
    	setHasOptionsMenu(true);
        setupAdapter();
	}
    
    public void setupAdapter() {
        String[] columns = new String[] { Player.FIRST_NAME_COLUMN, Player.LAST_NAME_COLUMN, Player.NUMBER_COLUMN };
        //The TextViews that will display the data
        int[] to = new int[] { R.id.fname, R.id.lname, R.id.number };
        
        mAdapter = new SimpleCursorAdapter(
	        getActivity().getApplicationContext(), R.layout.player_list_entry, null, columns, to, 0); //what flags?
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_group_list, container, false);
    } 
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mPlayerListView = (ListView) getView().findViewById(R.id.player_list);
        mPlayerListView.setAdapter(mAdapter);
        
	    bindPlayerClick();
	    bindPlayerLongClick();
    }
    
    /**************************************************************************
     * Event Handlers *********************************************************
     **************************************************************************/
    public void bindPlayerClick() {
        mPlayerListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PlayerViewActivity.class);
                intent.putExtra(Player.PLAYER_ID_COLUMN, String.valueOf(id));
                startActivity(intent);
            }
        });
    }
    
    public void bindPlayerLongClick() {
        mPlayerListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		    private ActionMode actionMode;
		    
        	@Override
            public boolean onItemLongClick(AdapterView<?> mAdapter, View view, int position, long id) {
                if (actionMode != null)
                	return false;

                String playerId = String.valueOf(id);
                actionMode = getActivity().startActionMode(actionModeCallback);
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
		                    Intent intent = new Intent(getActivity().getApplicationContext(), PlayerEditActivity.class);
		                    intent.putExtra(Player.PLAYER_ID_COLUMN, (String) mode.getTag());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.player_list_menu, menu);
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
    	Intent intent = new Intent(getActivity(), PlayerEditActivity.class);
    	startActivity(intent);
    }

    /**************************************************************************
     * Loader Functions *******************************************************
     **************************************************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        CursorLoader cursorLoader = new CursorLoader(getActivity().getApplicationContext(),
	        Player.ALL_URI, null, null, null, null);
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