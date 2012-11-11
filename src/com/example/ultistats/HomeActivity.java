package com.example.ultistats;

import java.io.*;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import com.example.ultistats.model.Base;

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Base base = new Base(this);
        base.copyDatabase();

        setContentView(R.layout.home_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    public void playerGroupActivity(View view) {
        startActivity(new Intent(this, PlayerGroupActivity.class));
    }
}
