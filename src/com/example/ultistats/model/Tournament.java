package com.example.ultistats.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.ultistats.DatabaseHelper;

public class Tournament extends Base {

    private SQLiteDatabase db;

    private static final String AUTHORITY = "com.example.ultistats.model.Tournament";
    private static final String TOURNAMENT_BASE_PATH = "tournament";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TOURNAMENT_BASE_PATH);

    private static final String ALL = "/all";
    private static final String TOURNAMENT = "/tournament";
    private static final String NEW = "/new";
    private static final String DELETE = "/delete";

    public static final int ALL_CODE = 1;
    public static final int TOURNAMENT_CODE = 2;
    public static final int NEW_CODE = 3;
    public static final int DELETE_CODE = 4;

    public static final Uri ALL_URI = Uri.withAppendedPath(Tournament.CONTENT_URI, ALL);
    public static final Uri TOURNAMENT_URI = Uri.withAppendedPath(Tournament.CONTENT_URI, TOURNAMENT);
    public static final Uri NEW_URI = Uri.withAppendedPath(Tournament.CONTENT_URI, NEW);
    public static final Uri DELETE_URI = Uri.withAppendedPath(Tournament.CONTENT_URI, DELETE);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, TOURNAMENT_BASE_PATH + ALL, ALL_CODE);
        sURIMatcher.addURI(AUTHORITY, TOURNAMENT_BASE_PATH + TOURNAMENT, TOURNAMENT_CODE);
        sURIMatcher.addURI(AUTHORITY, TOURNAMENT_BASE_PATH + NEW, NEW_CODE);
        sURIMatcher.addURI(AUTHORITY, TOURNAMENT_BASE_PATH + DELETE, DELETE_CODE);
    }

    private static final String TABLE_NAME = "tbl_tournament";
    public static final String TOURNAMENT_ID_COLUMN = "_id";


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
                        "UPDATE tbl_tournament " +
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
            case TOURNAMENT_CODE:
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
                        "FROM tbl_tournament";
                cursor = db.rawQuery(query, null);
                break;
            case TOURNAMENT_CODE:
                query =
                        "SELECT * FROM tbl_tournament" +
                        "WHERE _id = ?";
                cursor = db.rawQuery(query, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        //Make the cursor listen for changes in the database
        cursor.setNotificationUri(
                getContext().getContentResolver(), Tournament.CONTENT_URI);

        return cursor;
    }

}
