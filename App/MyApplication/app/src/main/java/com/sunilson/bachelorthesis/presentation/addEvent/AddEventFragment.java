package com.sunilson.bachelorthesis.presentation.addEvent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunilson.bachelorthesis.R;

/**
 * @author Linus Weiss
 */

public class AddEventFragment extends Fragment {

    public static AddEventFragment newInstance() {
        Bundle args = new Bundle();
        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_event, container, false);
        return view;
    }

}
