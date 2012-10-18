package com.example.ultistats;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.example.ultistats.model.Group;
import com.example.ultistats.model.Player;
import com.example.ultistats.model.Player.PlayerRow;

public class GroupListActivity extends LoaderActivity {

    private ExpandableAdapter adapter;
    private ExpandableListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);
        adapter = new ExpandableAdapter();

        //Asynchronously load the data  for the player list
        getSupportLoaderManager().initLoader(Group.ALL, null, this);
        getSupportLoaderManager().initLoader(Group.PLAYERS, null, this);

        listView = (ExpandableListView) findViewById(R.id.list_group);

        /*How do you know that the cursor has results before setting the adapter?*/
        listView.setOnChildClickListener(new OnChildClickListener() {@Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
            int childPosition, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra(PlayerListActivity.PLAYER_ID, String.valueOf(id));
                startActivity(intent);
                return false;
            }
        });
    }

    public class ExpandableAdapter extends BaseExpandableListAdapter {
        private ArrayList <String> groups = new ArrayList <String> ();
        private ArrayList <ArrayList<PlayerRow>> players = new ArrayList<ArrayList<PlayerRow>> ();

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
            groupCursor.moveToFirst();
            while (groupCursor.isAfterLast() == false) {
                groups.add(groupCursor.getString(groupCursor.getColumnIndex("group_name")));
                groupCursor.moveToNext();
            }
        }

        public void setupPlayers() {
            playerCursor.moveToFirst();
            int i = 0;
            players.add(new ArrayList <PlayerRow> ());

            while (!playerCursor.isAfterLast()) {
                int _id = playerCursor.getInt(playerCursor.getColumnIndex("_id"));
                String fname = playerCursor.getString(playerCursor.getColumnIndex("fname"));
                String lname = playerCursor.getString(playerCursor.getColumnIndex("lname"));

                Player.PlayerRow pr = new Player.PlayerRow(_id, fname, lname);
                players.get(i).add(pr);

                int prevGroupId = playerCursor.getInt(playerCursor.getColumnIndex("group_id"));
                playerCursor.moveToNext();
                if (!playerCursor.isAfterLast()) {
                    if (playerCursor.getInt(playerCursor.getColumnIndex("group_id")) != prevGroupId) {
                        i++;
                        players.add(new ArrayList <PlayerRow> ());
                    }
                }
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
            String group = groups.get(groupPosition);
            return group;
        }

        public PlayerRow getChild(int groupPosition, int childPosition) {
            PlayerRow player = players.get(groupPosition).get(childPosition);
            return player;
        }

        public int getGroupCount() {
            return groups.size();
        }

        public int getChildrenCount(int groupPosition) {
            groupCursor.moveToPosition(groupPosition);
            int childrenCount = groupCursor.getInt(groupCursor.getColumnIndex("player_count"));
            return childrenCount;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public long getChildId(int groupPosition, int childPosition) {
            PlayerRow player = players.get(groupPosition).get(childPosition);
            return player.getId();
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public Loader <Cursor> onCreateLoader(int id, Bundle bundle) {
        CursorLoader cursorLoader;
        switch (id) {
            case 1:
                cursorLoader = new CursorLoader(getApplicationContext(),
                Uri.withAppendedPath(Group.CONTENT_URI, Group.ALL_URI), null, null, null, null);
                break;
            case 2:
                cursorLoader = new CursorLoader(getApplicationContext(),
                Uri.withAppendedPath(Group.CONTENT_URI, Group.PLAYERS_URI), null, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
        if (loader.getId() == Group.ALL) adapter.setGroupCursor(cursor);
        else if (loader.getId() == Group.PLAYERS) adapter.setPlayerCursor(cursor);

        if (adapter.ready()) {
            adapter.setupGroups();
            adapter.setupPlayers();
            listView.setAdapter(adapter);
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