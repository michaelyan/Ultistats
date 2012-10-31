package com.example.ultistats;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.view.ActionMode;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.MenuInflater;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import com.example.ultistats.model.Group;
import com.example.ultistats.model.Player;
import com.example.ultistats.model.Player.PlayerRow;

public class GroupListActivity extends FragmentActivity implements LoaderCallbacks<Cursor>  {

    private ExpandableAdapter mAdapter;
    private ExpandableListView mGroupListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);
        mAdapter = new ExpandableAdapter();

        //Asynchronously load the data  for the player list
        getSupportLoaderManager().initLoader(Group.ALL_CODE, null, this);
        getSupportLoaderManager().initLoader(Group.PLAYERS_CODE, null, this);

        mGroupListView = (ExpandableListView) findViewById(R.id.group_list);
        bindPlayerClick();
        bindItemLongClick();

    }

    /**************************************************************************
     * Click Actions **********************************************************
     **************************************************************************/
    public void bindPlayerClick() {
        mGroupListView.setOnChildClickListener(new OnChildClickListener() {@Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
            		int childPosition, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerViewActivity.class);
                intent.putExtra(Player.PLAYER_ID_COLUMN, String.valueOf(id));
                startActivity(intent);
                return false;
            }
        });
    }
    
    public void bindItemLongClick() {
        mGroupListView.setOnItemLongClickListener(new OnItemLongClickListener() {
        	private ActionMode actionMode;
        	@Override
            public boolean onItemLongClick(AdapterView <?> mAdapter, View view, int position, long id) {
                if (actionMode != null)
                	return false;

                String groupId = String.valueOf(mGroupListView.getPackedPositionGroup(id));
                actionMode = GroupListActivity.this.startActionMode(actionModeCallback);
                actionMode.setTag(String.valueOf(groupId));
                view.setSelected(true);
                return true;
            }

		    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
		        @Override
		        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		            MenuInflater inflater = mode.getMenuInflater();
		            inflater.inflate(R.menu.group_list_edit_menu, menu);
		            return true;
		        }

		        @Override
		        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		            return false;
		        }

		        @Override
		        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		            switch (item.getItemId()) {
		                case R.id.group_edit:
		                    Intent intent = new Intent(getApplicationContext(), GroupEditActivity.class);
		                    intent.putExtra(Group.GROUP_ID_COLUMN, (String) mode.getTag());
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
        getMenuInflater().inflate(R.menu.group_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_group:
            	newGroup(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void newGroup(MenuItem item) {
    	Intent intent = new Intent(this, GroupEditActivity.class);
    	startActivity(intent);
    }

    /**************************************************************************
     * The ExpandableAdapter class ********************************************
     **************************************************************************/
    public class ExpandableAdapter extends BaseExpandableListAdapter {
        private ArrayList<Group.GroupRow> groups = new ArrayList<Group.GroupRow>();
        private SparseArray<ArrayList<Player.PlayerRow>> playerGroupHashMap = new SparseArray<ArrayList<Player.PlayerRow>>();

        private Cursor groupCursor;
        private Cursor playerCursor;

        public void setGroupCursor(Cursor cursor) {
            this.groupCursor = cursor;
            groupCursor.moveToFirst();
        }

        public void setPlayerCursor(Cursor cursor) {
            this.playerCursor = cursor;
            playerCursor.moveToFirst();
        }

        public void closeGroupCursor() {
            this.groupCursor.close();
        }

        public void closePlayerCursor() {
            this.playerCursor.close();
        }

        public boolean ready() {
            return groupCursor != null && playerCursor != null;
        }

        public void setupGroups() {
        	groups.clear();
            groupCursor.moveToFirst();
            while (groupCursor.isAfterLast() == false) {
            	int _id = groupCursor.getInt(groupCursor.getColumnIndex("_id"));
            	String groupName = groupCursor.getString(groupCursor.getColumnIndex("group_name"));
            	
            	Group.GroupRow gr = new Group.GroupRow(_id, groupName);
            	groups.add(gr);
            	//Add the group names to the HashMap
            	playerGroupHashMap.put(_id, new ArrayList<Player.PlayerRow>());
                groupCursor.moveToNext();
            }
        }

        public void setupPlayers() {
            playerCursor.moveToFirst();
            while (!playerCursor.isAfterLast()) {
                int _id = playerCursor.getInt(playerCursor.getColumnIndex("_id"));
                Integer group_id = Integer.valueOf(playerCursor.getInt(playerCursor.getColumnIndex("group_id")));
                String fname = playerCursor.getString(playerCursor.getColumnIndex("fname"));
                String lname = playerCursor.getString(playerCursor.getColumnIndex("lname"));

                Player.PlayerRow pr = new Player.PlayerRow(_id, fname, lname);
            	playerGroupHashMap.get(group_id).add(pr);
                playerCursor.moveToNext();
            }
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
        View convertView, ViewGroup parent) {

            TextView view = new TextView(getApplicationContext());
            view.setText(getGroup(groupPosition));
            return view;
        }

        public View getChildView(int groupPosition, int childPosition,
        		boolean isLastChild, View convertView, ViewGroup parent) {
            TextView view = new TextView(getApplicationContext());
            PlayerRow pr = getChild(groupPosition, childPosition);
            String name = pr.getFname() + pr.getLname();
            view.setText(name);
            return view;
        }

        public String getGroup(int groupPosition) {
            String group = groups.get(groupPosition).getGroupName();
            return group;
        }

        public PlayerRow getChild(int groupPosition, int childPosition) {
        	int groupId = (int) getGroupId(groupPosition);
            PlayerRow player = playerGroupHashMap.get(groupId).get(childPosition);
            return player;
        }

        public int getGroupCount() {
            return groups.size();
        }

        public int getChildrenCount(int groupPosition) {
        	int groupId = (int) getGroupId(groupPosition);
            return playerGroupHashMap.get(groupId).size();
        }

        public long getGroupId(int groupPosition) {
        	Log.i("group id is", String.valueOf(groups.get(groupPosition).getId()));
        	return groups.get(groupPosition).getId();
        }

        public long getChildId(int groupPosition, int childPosition) {
        	int groupId = (int) getGroupId(groupPosition);
            PlayerRow player = playerGroupHashMap.get(groupId).get(childPosition);
            return player.getId();
        }

        //What are these
        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    /**************************************************************************
     * Loader Functions *******************************************************
     **************************************************************************/
    @Override
    public Loader <Cursor> onCreateLoader(int id, Bundle bundle) {
        CursorLoader cursorLoader;
        switch (id) {
            case Group.ALL_CODE:
                cursorLoader = new CursorLoader(getApplicationContext(),
	                Group.ALL_URI, null, null, null, null);
                break;
            case Group.PLAYERS_CODE:
                cursorLoader = new CursorLoader(getApplicationContext(),
	                Group.PLAYERS_URI, null, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
        if (loader.getId() == Group.ALL_CODE) 
        	mAdapter.setGroupCursor(cursor);
        else if (loader.getId() == Group.PLAYERS_CODE) 
        	mAdapter.setPlayerCursor(cursor);

        if (mAdapter.ready()) {
            mAdapter.setupGroups();
            mAdapter.setupPlayers();
            mGroupListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        //How to remove cursor error?
        mAdapter.closeGroupCursor();
        mAdapter.closePlayerCursor();
    }
}