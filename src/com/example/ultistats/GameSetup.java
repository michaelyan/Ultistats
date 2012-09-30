package com.example.ultistats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class GameSetup extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.game_setup, container, false);
    	Spinner spinner = (Spinner) view.findViewById(R.id.tournament_spinner);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
    	        R.array.tournament_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }

	public void create(View view) {
		String name = ((EditText) getView().findViewById(R.id.text_game_name)).getText().toString();
		String str_game_to = ((EditText) getView().findViewById(R.id.text_game_to)).getText().toString();
		int game_to = Integer.parseInt(str_game_to);
		String selection = (String) ((Spinner) getView().findViewById(R.id.tournament_spinner)).getSelectedItem();
		Log.i("Out:  ", name + " " + game_to + " " + selection);
	}
    
}
