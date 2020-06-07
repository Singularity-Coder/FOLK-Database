package com.singularitycoder.folkdatabase.profile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.singularitycoder.folkdatabase.R;

public class OccupationFragment extends Fragment {

    private static final String TAG = "OccupationFragment";

    private String emailKey;

    public OccupationFragment() {
    }

    public OccupationFragment(String emailKey) {
        this.emailKey = emailKey;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_occupation, container, false);

        return view;
    }

}
