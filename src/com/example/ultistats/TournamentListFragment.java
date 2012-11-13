package com.example.ultistats;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.*;
import com.example.ultistats.model.Player;

import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.database.Cursor;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import com.example.ultistats.model.Tournament;

public class TournamentListFragment extends BaseListFragment implements LoaderCallbacks<Cursor> {

    private ListView mTournamentListView;
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
        return inflater.inflate(R.layout.player_group, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //fail
        mTournamentListView = (ListView) getView().findViewById(R.id.player_edit);
        mTournamentListView.setAdapter(mAdapter);

        bindPlayerClick();
        bindPlayerLongClick();
    }

    /**************************************************************************
     * Event Handlers *********************************************************
     **************************************************************************/
    public void bindPlayerClick() {
        mTournamentListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PlayerViewActivity.class);
                intent.putExtra(Player.PLAYER_ID_COLUMN, String.valueOf(id));
                startActivity(intent);
            }
        });
    }

    //Binds the actions to a long player click. Make sure you also change this function in GroupListFragment
    public void bindPlayerLongClick() {
        mTournamentListView.setOnItemLongClickListener(new OnItemLongClickListener() {
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
                    inflater.inflate(R.menu.player_edit_menu_1, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    final String playerId = (String) mode.getTag();
                    switch (item.getItemId()) {
                        case R.id.player_view: {
                            Intent intent = new Intent(getActivity().getApplicationContext(), PlayerViewActivity.class);
                            intent.putExtra(Player.PLAYER_ID_COLUMN, playerId);
                            startActivity(intent);
                            mode.finish();
                            return true;
                        }
                        case R.id.player_edit: {
                            Intent intent = new Intent(getActivity().getApplicationContext(), PlayerEditActivity.class);
                            intent.putExtra(Player.PLAYER_ID_COLUMN, playerId);
                            startActivity(intent);
                            mode.finish();
                            return true;
                        }
                        case R.id.player_delete:
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            PlayerEditActivity.deletePlayer(playerId, builder, getActivity());
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
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        inflater.inflate(R.menu.player_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToHomeActivity();
                return true;
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
                Tournament.ALL_URI, null, null, null, null);
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