package com.example.ultistats.model;

import java.io.*;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class Base extends ContentProvider {

	private String DATABASE_NAME = "ultistats.sqlite";
    private Context context;
    
    public Base() {
    	super();
    }
    
    public Base(Context context) {
    	super();
        this.context = context;
    }
 
    public void copyDatabase()
    {
        try {
        	// Open your local db as the input stream
        	InputStream myInput = this.context.getAssets().open(DATABASE_NAME);

        	// Path to the just created empty db
        	String outFileName = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
        	
        	Log.i("outfilename", outFileName);

        	OutputStream myOutput = new FileOutputStream(outFileName);

        	// transfer bytes from the inputfile to the outputfile
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = myInput.read(buffer)) > 0) 
        	 {
        	     myOutput.write(buffer, 0, length);
        	 }

        	// Close the streams
        	myOutput.flush();
        	myOutput.close();
        	myInput.close();
        	} 
        	catch (Exception e) 
        	{
        	Log.e("error", e.toString());
        	}
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}

