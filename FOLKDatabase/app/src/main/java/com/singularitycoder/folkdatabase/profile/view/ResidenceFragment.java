package com.singularitycoder.folkdatabase.profile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.singularitycoder.folkdatabase.R;

public class ResidenceFragment extends Fragment {

    private static final String TAG = "ResidenceFragment";

    private String emailKey;

    public ResidenceFragment() {
    }

    public ResidenceFragment(String emailKey) {
        this.emailKey = emailKey;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_residence, container, false);

        return view;
    }

}
