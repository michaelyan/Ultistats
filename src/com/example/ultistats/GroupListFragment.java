package com.example.ultistats;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
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

public class GroupListFragment extends Fragment implements LoaderCallbacks<Cursor>  {

    private ExpandableAdapter adapter;
    private ExpandableListView groupListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ExpandableAdapter();
        setHasOptionsMenu(true);

        //Asynchronously load the data  for the player list
        getLoaderManager().initLoader(Group.ALL_CODE, null, this);
        getLoaderManager().initLoader(Group.PLAYERS_CODE, null, this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_group_list, container, false);
    } 
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        groupListView = (ExpandableListView) getView().findViewById(R.id.group_list);
        bindPlayerClick();
        bindItemLongClick();
    }
    
    /**************************************************************************
     * Event Handlers *********************************************************
     **************************************************************************/
    public void bindPlayerClick() {
        groupListView.setOnChildClickListener(new OnChildClickListener() {@Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
            		int childPosition, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PlayerViewActivity.class);
                intent.putExtra(Player.PLAYER_ID_COLUMN, String.valueOf(id));
                startActivity(intent);
                return false;
            }
        });
    }

    public void bindItemLongClick() {
        groupListView.setOnItemLongClickListener(new OnItemLongClickListener() {
        	private ActionMode actionMode;

        	@Override
            public boolean onItemLongClick(AdapterView <?> adapter, View view, int position, long id) {
                if (actionMode != null)
                 	return false;

                //Only allow long clicks on groups
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    String playerId = String.valueOf(groupListView.getPackedPositionGroup(id));
                    actionMode = getActivity().startActionMode(groupActionModeCallback);
                    actionMode.setTag(String.valueOf(playerId));
                    view.setSelected(true);
                    return true;
                }
                return false;
            }

            private ActionMode.Callback groupActionModeCallback = new ActionMode.Callback() {
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
                            Intent intent = new Intent(getActivity(), GroupEditActivity.class);
                            intent.putExtra(Group.GROUP_ID_COLUMN, (String) mode.getTag());
                            startActivity(intent);
                            mode.finish();
                            return true;
                        case R.id.group_delete:
                            final String tmp = (String) mode.getTag();
                            new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.group_delete_confirm_message)
                                    .setNegativeButton(R.string.no, null)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            getActivity().getContentResolver().delete(
                                                    Group.DELETE_GROUP_URI, null, new String[]{ tmp });
                                        }
                                    })
                                    .show();
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
     * Custom Adapter *********************************************************
     **************************************************************************/
    private class ExpandableAdapter extends BaseExpandableListAdapter {
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
            	int _id = groupCursor.getInt(groupCursor.getColumnIndex(Group.GROUP_ID_COLUMN));
            	String groupName = groupCursor.getString(groupCursor.getColumnIndex(Group.GROUP_NAME_COLUMN));

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
                int _id = playerCursor.getInt(playerCursor.getColumnIndex(Group.GROUP_ID_COLUMN));
                int group_id = playerCursor.getInt(playerCursor.getColumnIndex(Group.GROUP_ID_JOIN_COLUMN));
                String fname = playerCursor.getString(playerCursor.getColumnIndex(Player.FIRST_NAME_COLUMN));
                String lname = playerCursor.getString(playerCursor.getColumnIndex(Player.LAST_NAME_COLUMN));
                String nickname = playerCursor.getString(playerCursor.getColumnIndex(Player.NICKNAME_COLUMN));
                int number = playerCursor.getInt(playerCursor.getColumnIndex(Player.NUMBER_COLUMN));

                Player.PlayerRow pr = new Player.PlayerRow(_id, fname, lname, nickname, number);
                playerGroupHashMap.get(group_id).add(pr);
                playerCursor.moveToNext();
            }
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
        View convertView, ViewGroup parent) {

            TextView view = new TextView(getActivity());
            view.setText(getGroup(groupPosition));
            return view;
        }

        public View getChildView(int groupPosition, int childPosition,
        		boolean isLastChild, View convertView, ViewGroup parent) {
            TextView view = new TextView(getActivity());
            PlayerRow pr = getChild(groupPosition, childPosition);
            String name = pr.getFname() + pr.getNickname() + pr.getLname();
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
     * Menu Actions ***********************************************************
     **************************************************************************/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_list_menu, menu);
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
    	Intent intent = new Intent(getActivity(), GroupEditActivity.class);
    	startActivity(intent);
    } 
    
    /**************************************************************************
     * Loader Functions *******************************************************
     **************************************************************************/
    @Override
    public Loader <Cursor> onCreateLoader(int id, Bundle bundle) {
        CursorLoader cursorLoader;
        switch (id) {
            case Group.ALL_CODE:
                cursorLoader = new CursorLoader(getActivity().getApplicationContext(),
	                Group.ALL_URI, null, null, null, null);
                break;
            case Group.PLAYERS_CODE:
                cursorLoader = new CursorLoader(getActivity(),
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
        	adapter.setGroupCursor(cursor);
        else if (loader.getId() == Group.PLAYERS_CODE) 
        	adapter.setPlayerCursor(cursor);

        if (adapter.ready()) {
            adapter.setupGroups();
            adapter.setupPlayers();
            groupListView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        //How to remove cursor error?
        Log.i("On Loader Reset", String.valueOf(loader.getId()));
        adapter.closeGroupCursor();
        adapter.closePlayerCursor();
    }
}