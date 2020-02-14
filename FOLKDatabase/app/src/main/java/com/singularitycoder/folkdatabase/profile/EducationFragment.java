package com.singularitycoder.folkdatabase.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.singularitycoder.folkdatabase.R;

public class EducationFragment extends Fragment {

    private static final String TAG = "EducationFragment";

    private String emailKey;

    public EducationFragment() {
    }

    public EducationFragment(String emailKey) {
        this.emailKey = emailKey;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_education, container, false);

        return view;
    }

}
