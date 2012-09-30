package com.example.ultistats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

public class ManagePlayers extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.list_manage, container, false);
    	//GET LIST FROM DB
    	String[] items = new String[]{"Item1","It2","Itegjm3","Itemadfhd4", "I5"};
    	GridView grid = (GridView) view.findViewById(R.id.manage_grid);
    	ArrayCheckBoxAdapter ad = new ArrayCheckBoxAdapter(getActivity(), android.R.layout.simple_list_item_checked, items);
        grid.setAdapter(ad);
        return view;
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
