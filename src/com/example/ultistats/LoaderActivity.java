package com.example.ultistats;

import android.os.Bundle;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ListView;

public class LoaderActivity  extends FragmentActivity implements
	LoaderManager.LoaderCallbacks <Cursor> {

    protected SimpleCursorAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Copy the database to the phone
//        Base b = new Base(this);
//        b.copyDatabase();
    }

    @Override
    public Loader <Cursor> onCreateLoader(int arg0, Bundle arg1) {
    	return null;
    }

    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
    }
}