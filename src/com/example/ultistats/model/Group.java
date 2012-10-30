package com.example.ultistats.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.ultistats.DatabaseHelper;

public class Group extends Base {

	private SQLiteDatabase db;
	
	//Must be the same name as the full class path
	private static final String AUTHORITY = "com.example.ultistats.model.Group";
	private static final String GROUP_BASE_PATH = "groups";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + GROUP_BASE_PATH);
	
	public static final String NUMBER = "/#";
	public static final String ALL = "/all";
	public static final String PLAYERS = "/players";
	public static final String GROUP = "";
	public static final String GROUP_NAME = "/group_name";
	public static final String GROUP_EXCLUSIVE = "/group_exclusive";
	public static final String NEW = "/new";
	public static final String DELETE_GROUP = "/delete_group";
	public static final String DELETE_PLAYER_FROM_GROUP = "/delete_player_from_group";
	public static final String INSERT_PLAYER_INTO_GROUP = "/insert_player_into_group";
	
	public static final int ALL_CODE = 1;
	public static final int PLAYERS_CODE = 2;
	public static final int GROUP_CODE = 3;
	public static final int GROUP_NAME_CODE = 4;
	public static final int GROUP_EXCLUSIVE_CODE = 5;
	public static final int NEW_CODE = 6;
	public static final int DELETE_GROUP_CODE = 7;
	public static final int INSERT_PLAYER_INTO_GROUP_CODE = 8;
	public static final int DELETE_PLAYER_FROM_GROUP_CODE = 9;
	
	public static final Uri ALL_URI = Uri.withAppendedPath(Group.CONTENT_URI, ALL);
	public static final Uri PLAYERS_URI = Uri.withAppendedPath(Group.CONTENT_URI, PLAYERS);
	public static final Uri GROUP_URI = Group.CONTENT_URI;
	public static final Uri GROUP_NAME_URI = Uri.withAppendedPath(Group.CONTENT_URI, GROUP_NAME);
	public static final Uri GROUP_EXCLUSIVE_URI = Uri.withAppendedPath(Group.CONTENT_URI, GROUP_EXCLUSIVE);
	public static final Uri NEW_URI = Uri.withAppendedPath(Group.CONTENT_URI, NEW);
	public static final Uri DELETE_GROUP_URI = Uri.withAppendedPath(Group.CONTENT_URI, DELETE_GROUP);
	public static final Uri DELETE_PLAYER_FROM_GROUP_URI = Uri.withAppendedPath(Group.CONTENT_URI, DELETE_PLAYER_FROM_GROUP);
	public static final Uri INSERT_PLAYER_INTO_GROUP_URI = Uri.withAppendedPath(Group.CONTENT_URI, INSERT_PLAYER_INTO_GROUP);
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + ALL, ALL_CODE); //Get all the groups (without players)
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + PLAYERS, PLAYERS_CODE); //Get all the players and their group info
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + NUMBER, GROUP_CODE); //Get a single group and all its players
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + GROUP_NAME + NUMBER, GROUP_NAME_CODE); //Get a single group, but just it's name
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + GROUP_EXCLUSIVE + NUMBER, GROUP_EXCLUSIVE_CODE); //Get a single group and all its players
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + NEW, NEW_CODE); //New group
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + DELETE_GROUP, DELETE_GROUP_CODE); //Remove a group and all its associated players
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + DELETE_PLAYER_FROM_GROUP, DELETE_PLAYER_FROM_GROUP_CODE); //Remove a player from a group
	    sURIMatcher.addURI(AUTHORITY, GROUP_BASE_PATH + INSERT_PLAYER_INTO_GROUP, INSERT_PLAYER_INTO_GROUP_CODE); //Remove a player from a group
	}
	
	private static final String TABLE_NAME = "tbl_group";
	private static final String JOIN_TABLE_NAME = "tbl_player_group";
	public static final String GROUP_ID_COLUMN = "group_id";
	public static final String GROUP_NAME_COLUMN = "group_name";

	//Wrapper class for groups
	public static class GroupRow {
		private int _id;
		private String groupName;
		
		public GroupRow() {}
		
		public GroupRow(int _id, String groupName) {
			this._id = _id;
			this.groupName = groupName;
		}

		public int getId() {
			return _id;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupId(int _id) {
			this._id = _id;
		}

		public void setGroupNname(String groupName) {
			this.groupName = groupName;
		}
	}
	
	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext()).getWritableDatabase();
		return true;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Uri insert(Uri uri, ContentValues values) {
	    int uriType = sURIMatcher.match(uri);
	    long id = 0;
	    switch (uriType) {
	        case NEW_CODE:
	        	String nullColumnHack = "group_name";
	            id = db.insert(TABLE_NAME, nullColumnHack, values);
	            break;
	        case INSERT_PLAYER_INTO_GROUP_CODE:
	            id = db.insert(JOIN_TABLE_NAME, null, values);
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(Group.CONTENT_URI, null);
	    
	    return Uri.withAppendedPath(Group.CONTENT_URI, String.valueOf(id));
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    int rowsUpdated = 0;
	    switch (uriType) {
	        case GROUP_CODE:
	            rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(Group.CONTENT_URI, null);
	    return rowsUpdated;
	}
	
	@Override
	public int delete (Uri uri, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    int rowsUpdated = 0;
	    String query;
	    switch (uriType) {
		    case DELETE_GROUP_CODE:
		    	query = "" +
		    		"DELETE FROM tbl_group " +
		    		"WHERE tbl_group._id = ?";
		    	db.execSQL(query, selectionArgs);
		    	query = "" +
		    		"DELETE FROM tbl_player_group " +
		    		"WHERE tbl_player_group.group_id = ?";
		    	db.execSQL(query, selectionArgs);
		    	break;
	        case DELETE_PLAYER_FROM_GROUP_CODE:
	        	selection = "player_id=? AND group_id=?";
	            rowsUpdated = db.delete(JOIN_TABLE_NAME, selection, selectionArgs);
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(Group.CONTENT_URI, null);
	    return rowsUpdated;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
	        String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
	    int uriType = sURIMatcher.match(uri);
	    String query;
	    
	    switch(uriType) {
	    case ALL_CODE:
	    	query = "SELECT _id, group_name from tbl_group";
	    	cursor = db.rawQuery(query, null);
	        break;
	    case PLAYERS_CODE:
	    	query = "" + 
		    	"SELECT tbl_player._id, tbl_player.fname, tbl_player.lname, tbl_group._id as group_id "  +  
				"FROM tbl_player " +
				"JOIN tbl_player_group on tbl_player._id = tbl_player_group.player_id " +
				"JOIN tbl_group on tbl_group._id = tbl_player_group.group_id " +
				"ORDER BY group_name DESC";
	    	cursor = db.rawQuery(query, null);
	        break;
	    case GROUP_CODE:
	    	query = "" + 
		    	"SELECT tbl_player._id, tbl_player.fname, tbl_player.lname, tbl_player.number, " +
				       "tbl_group._id as group_id, tbl_group.group_name "  +  
				"FROM tbl_player " +
				"JOIN tbl_player_group on tbl_player._id = tbl_player_group.player_id " +
				"JOIN tbl_group on tbl_group._id = tbl_player_group.group_id " +
				"WHERE group_id = ? " +
				"ORDER BY group_name DESC";
	    	cursor = db.rawQuery(query, new String[]{uri.getLastPathSegment()});
	        break;
	    case GROUP_NAME_CODE:
    		query = "SELECT _id, group_name " +
    				"FROM tbl_group " +
    				"WHERE _id = ?";
	    	cursor = db.rawQuery(query, new String[]{uri.getLastPathSegment()});
	    	break;
	    case GROUP_EXCLUSIVE_CODE:
	    	query = "" + 
		    	"SELECT tbl_player._id, tbl_player.fname, tbl_player.lname, tbl_player.number " +
	    		"FROM tbl_player " +
		    	"WHERE tbl_player._id NOT IN " +
		    	"(" +
			    	"SELECT tbl_player._id " +
					"FROM tbl_player " +
					"JOIN tbl_player_group on tbl_player._id = tbl_player_group.player_id " +
					"JOIN tbl_group on tbl_group._id = tbl_player_group.group_id " +
					"WHERE group_id = ? " +
				")";
	    	cursor = db.rawQuery(query, new String[]{uri.getLastPathSegment()});
	    	break;
	    default:
	        throw new IllegalArgumentException("Unknown URI");
	    }
	    
    	//Make the cursor listen for changes in the database
    	cursor.setNotificationUri(
    			getContext().getContentResolver(), Group.CONTENT_URI);
	    return cursor;
	}
}