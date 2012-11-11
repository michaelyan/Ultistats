package com.example.ultistats.model;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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


}
