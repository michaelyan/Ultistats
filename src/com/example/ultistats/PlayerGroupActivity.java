package com.example.ultistats;

import com.example.ultistats.model.Base;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PlayerGroupActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
	            .setText("Players") //Replace with strings.xml
	            .setTabListener(new PlayerGroupTabListener<PlayerListFragment>(
	                    this, "artist", PlayerListFragment.class));
	    actionBar.addTab(tab);
//
	    tab = actionBar.newTab()
	        .setText("Groups")
	        .setTabListener(new PlayerGroupTabListener<GroupListFragment>(
	                this, "album", GroupListFragment.class));
	    actionBar.addTab(tab);
	}
    
    /**************************************************************************
     * Tab Listener Class *****************************************************
     **************************************************************************/
    private class PlayerGroupTabListener<T extends Object> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        
        public PlayerGroupTabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
}
