package com.example.ultistats;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: michaelyan
 * Date: 11/9/12
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void goToHomeActivity() {
        Intent parentActivityIntent = new Intent(getActivity(), HomeActivity.class);
        parentActivityIntent.addFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(parentActivityIntent);
        getActivity().finish();
    }
}