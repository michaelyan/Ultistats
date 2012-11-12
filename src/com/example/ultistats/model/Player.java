package com.example.ultistats.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.ultistats.DatabaseHelper;

public class Player extends Base {

	private SQLiteDatabase db;
	
	private static final String AUTHORITY = "com.example.ultistats.model.Player";
	private static final String PLAYER_BASE_PATH = "player";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + PLAYER_BASE_PATH);

	private static final String ALL = "/all";
	private static final String PLAYER = "/player";
	private static final String NEW = "/new";
    private static final String DELETE = "/delete";

	public static final int ALL_CODE = 1;
	public static final int PLAYER_CODE = 2;
	public static final int NEW_CODE = 3;
    public static final int DELETE_CODE = 4;

	public static final Uri ALL_URI = Uri.withAppendedPath(Player.CONTENT_URI, ALL);
    public static final Uri PLAYER_URI = Uri.withAppendedPath(Player.CONTENT_URI, PLAYER);
	public static final Uri NEW_URI = Uri.withAppendedPath(Player.CONTENT_URI, NEW);
    public static final Uri DELETE_URI = Uri.withAppendedPath(Player.CONTENT_URI, DELETE);

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + ALL, ALL_CODE);
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + PLAYER, PLAYER_CODE);
	    sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + NEW, NEW_CODE);
        sURIMatcher.addURI(AUTHORITY, PLAYER_BASE_PATH + DELETE, DELETE_CODE);
	}
	
	private static final String TABLE_NAME = "tbl_player";
	public static final String PLAYER_ID_COLUMN = "_id";
    public static final String PLAYER_ID_JOIN_COLUMN = "player_id";
	public static final String FIRST_NAME_COLUMN = "fname";
	public static final String LAST_NAME_COLUMN = "lname";
    public static final String NICKNAME_COLUMN = "nickname";
	public static final String NUMBER_COLUMN = "number";
	
	
	//A class for wrapping around database rows
	public static class PlayerRow {
		private int _id;
		private String fname;
		private String lname;
        private String nickname;
        private int number;

		public PlayerRow(int _id, String fname, String lname, String nickname, int number) {
			this._id = _id;
			this.fname = fname;
			this.lname = lname;
            this.nickname = nickname;
            this.number = number;
		}

		public int getId() { return _id; }

		public String getFname() { return fname; }

		public String getLname() { return lname; }

        public String getNickname() {
            if (!("").equals(nickname))
                return " \"" + nickname + "\" ";
            else
                return " ";
        }

	}

	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext()).getWritableDatabase();
		return true;
	}

    @Override
    public int delete (Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;
        String query;
        switch (uriType) {
            case DELETE_CODE:
                query = "" +
                        "UPDATE tbl_player " +
                        "SET active = 0 " +
                        "WHERE _id = ?";
                db.execSQL(query, selectionArgs);
                rowsUpdated = 1;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(Player.CONTENT_URI, null);
        return rowsUpdated;
	}

	public Uri insert(Uri uri, ContentValues values) {
	    int uriType = sURIMatcher.match(uri);
	    long id;
	    switch (uriType) {
	        case NEW_CODE:
	            id = db.insert(TABLE_NAME, null, values);
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(Player.CONTENT_URI, null);

	    return ContentUris.withAppendedId(Player.CONTENT_URI, id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    int rowsUpdated = 0;
	    switch (uriType) {
	        case PLAYER_CODE:
	            rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(Player.CONTENT_URI, null);
	    return rowsUpdated;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
	        String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
        String query;
	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case ALL_CODE:
            query =
                "SELECT * " +
                "FROM tbl_player " +
                "WHERE tbl_player.active = 1";
	    	cursor = db.rawQuery(query, null);
	        break;
	    case PLAYER_CODE:
	    	//Since the last segment has no spaces, it will turn the string into an array of string with one element
            query =
                "SELECT * FROM tbl_player " +
                "WHERE _id = ? " +
                "AND tbl_player.active = 1";
	    	cursor = db.rawQuery(query, selectionArgs);
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
    	//Make the cursor listen for changes in the database
    	cursor.setNotificationUri(
    			getContext().getContentResolver(), Player.CONTENT_URI);
    	
	    return cursor;
	}
}