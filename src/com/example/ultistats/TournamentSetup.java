package com.example.ultistats;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class TournamentSetup extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tournament_setup, container, false);
    }

	public void create(View view) {
		String name = ((EditText) getView().findViewById(R.id.text_tournament_name)).getText().toString();
		String scoring_team = ((EditText) getView().findViewById(R.id.text_scoring_team)).getText().toString();
		String date = ((EditText) getView().findViewById(R.id.text_tournament_date)).getText().toString();
		Date format = null;
		try {
			format = new SimpleDateFormat("MM/dd/yy").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.i("Out:  ", name + " " + scoring_team + " " + format.getTime());
	}

}
