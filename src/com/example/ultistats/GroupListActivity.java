package com.example.ultistats;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.ultistats.model.Group;

public class GroupListActivity extends LoaderActivity {
	
	private ExpandableAdapter adapter;
	private ExpandableListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		adapter = new ExpandableAdapter();
		
        //Asynchronously load the data  for the player list
        getSupportLoaderManager().initLoader(0, null, this);

        /*How do you know that the cursor has results before setting the adapter?*/
        
//		list.setGroupIndicator(null);
	}

	public class ExpandableAdapter extends BaseExpandableListAdapter {
		
		private String[] groups = { "People Names", "Dog Names", "Cat Names",
				"Fish Names" };
		private String[][] children = { { "Arnold", "Johnson" }, { "Ace" }, { "Fluffy" },
				{ "Goldy" } };
		private Cursor cursor;

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}
		
		public void setCursor(Cursor cursor) {
			this.cursor = cursor;
			cursor.moveToFirst();
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView foo = new TextView(getApplicationContext());
//			String text = groups[groupPosition];
			String text = cursor.getString(0);
			foo.setText(text);
			return foo;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView foo = new TextView(getApplicationContext());
			String text = children[groupPosition][childPosition];
			foo.setText(text);
			return foo;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
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
    	//What data to get
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
	        Uri.withAppendedPath(Group.CONTENT_URI, "all"), null, null, null, null);
        return cursorLoader;
    }
    
    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
    	adapter.setCursor(cursor);
		list = (ExpandableListView) findViewById(R.id.list_group);
		list.setAdapter(adapter);
    }
    
    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
    }
}