package com.example.ultistats;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class GroupListActivity extends Activity {
	
	private ExpadableAdapter mAdapter;
	private ExpandableListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_group);
		mAdapter = new ExpadableAdapter();

		list = (ExpandableListView) findViewById(R.id.list_group);
		list.setAdapter(mAdapter);
		list.setGroupIndicator(null);

		list.setOnGroupExpandListener(new OnGroupExpandListener() {

			public void onGroupExpand(int groupPosition) {
				int len = mAdapter.getGroupCount();
				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						list.collapseGroup(i);
					}
				}
			}
		});

		list.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
                                 //causes out of bounds exception. has to be done on Collapse for some reason.

				// int len = mAdapter.getGroupCount();
				// for (int i = 0; i < len; i++) {
				// if (i != groupPosition) {
				// parent.collapseGroup(i);
				// }
				// }

				return false;
			}

		});

	}

	public class ExpadableAdapter extends BaseExpandableListAdapter {

		private String[] groups = { "People Names", "Dog Names", "Cat Names",
				"Fish Names" };
		private String[][] children = { { "Arnold" }, { "Ace" }, { "Fluffy" },
				{ "Goldy" } };

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView foo = new TextView(getApplicationContext());
			String text = "[" + groupPosition + "," + childPosition + "]";
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

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView foo = new TextView(getApplicationContext());
			String text = "[" + groupPosition + "]";
			foo.setText(text);
			return foo;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

}