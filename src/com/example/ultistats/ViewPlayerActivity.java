package com.example.ultistats;

import com.example.ultistats.model.Base;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewPlayerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_player);
        
        Base base = new Base(this);
        base.copyDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_player, menu);
        return true;
    }
}
