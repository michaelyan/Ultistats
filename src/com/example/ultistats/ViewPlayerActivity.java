package com.example.ultistats;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewPlayerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_player);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_player, menu);
        return true;
    }
}
