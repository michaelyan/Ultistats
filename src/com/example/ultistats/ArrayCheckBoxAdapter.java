package com.example.ultistats;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class ArrayCheckBoxAdapter extends ArrayAdapter<String> {

	private final Activity activity;
    private final ArrayList<String> objects = new ArrayList<String>();
	
	public ArrayCheckBoxAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.activity = (Activity) context;
		for (String s : objects)
			this.objects.add(s);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
        	LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.checkbox_element, null);
        }
        CheckBox tv = (CheckBox) v.findViewById(R.id.checkbox_element);
        tv.setText(objects.get(position));

        return v;
	}
	
}
