package com.example.ultistats.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.ultistats.DatabaseHelper;

public class Game extends Base {

    private SQLiteDatabase db;

    private static final String AUTHORITY = "com.example.ultistats.model.Game";
    private static final String GAME_BASE_PATH = "game";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + GAME_BASE_PATH);

    private static final String ALL = "/all";
    private static final String GAME = "/game";
    private static final String NEW = "/new";
    private static final String DELETE = "/delete";

    public static final int ALL_CODE = 1;
    public static final int GAME_CODE = 2;
    public static final int NEW_CODE = 3;
    public static final int DELETE_CODE = 4;

    public static final Uri ALL_URI = Uri.withAppendedPath(Game.CONTENT_URI, ALL);
    public static final Uri GAME_URI = Uri.withAppendedPath(Game.CONTENT_URI, GAME);
    public static final Uri NEW_URI = Uri.withAppendedPath(Game.CONTENT_URI, NEW);
    public static final Uri DELETE_URI = Uri.withAppendedPath(Game.CONTENT_URI, DELETE);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, GAME_BASE_PATH + ALL, ALL_CODE);
        sURIMatcher.addURI(AUTHORITY, GAME_BASE_PATH + GAME, GAME_CODE);
        sURIMatcher.addURI(AUTHORITY, GAME_BASE_PATH + NEW, NEW_CODE);
        sURIMatcher.addURI(AUTHORITY, GAME_BASE_PATH + DELETE, DELETE_CODE);
    }

    private static final String TABLE_NAME = "tbl_game";
    public static final String GAME_ID_COLUMN = "_id";


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
            case GAME_CODE:
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
                                "FROM tbl_game";
                cursor = db.rawQuery(query, null);
                break;
            case GAME_CODE:
                query =
                        "SELECT * FROM tbl_game" +
                                "WHERE _id = ?";
                cursor = db.rawQuery(query, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        //Make the cursor listen for changes in the database
        cursor.setNotificationUri(
                getContext().getContentResolver(), Game.CONTENT_URI);

        return cursor;
    }

}

