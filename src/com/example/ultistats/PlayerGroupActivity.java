package com.example.ultistats;

import com.example.ultistats.model.Base;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PlayerGroupActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // Notice that setContentView() is not used, because we use the root
	    // android.R.id.content as the container for each fragment

	    Base base = new Base(this);
	    base.copyDatabase();

	    // setup action bar for tabs
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

	    Tab tab = actionBar.newTab()
	            .setText(R.string.player_tab_title)
	            .setTabListener(new PlayerGroupTabListener<PlayerListFragment>(
	                    this, PlayerListFragment.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab()
	        .setText(R.string.group_tab_title)
	        .setTabListener(new PlayerGroupTabListener<GroupListFragment>(
	                this, GroupListFragment.class));
	    actionBar.addTab(tab);
	}

    /**************************************************************************
     * Tab Listener Class *****************************************************
     **************************************************************************/
    private class PlayerGroupTabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment fragment;
        private final Activity activity;
        private final Class<T> clz;

        public PlayerGroupTabListener(Activity activity, Class<T> clz) {
            this.activity = activity;
            this.clz = clz;
        }

        @Override
        public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (fragment == null) {
                // If not, instantiate and add it to the activity
                fragment = Fragment.instantiate(activity, clz.getName());
                ft.add(android.R.id.content, fragment);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(fragment);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            if (fragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(fragment);
            }
        }

        @Override
        public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
//	public void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//        setContentView(R.layout.player_group);
//    }
}
