package com.singularitycoder.folkdatabase.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.singularitycoder.folkdatabase.R;

public class TalentFragment extends Fragment {

    private TextView tvTalentText;


    public TalentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_talent, container, false);

        if (null != getActivity()) {
            SharedPreferences sp = getActivity().getSharedPreferences("talentItem", Context.MODE_PRIVATE);
            tvTalentText = view.findViewById(R.id.tv_talent_text);
            tvTalentText.setText(
                    "Can Cook: " + sp.getString("canCook", "") +
                            "\n\nCooking Self Rating: " + sp.getString("cookingSelfRating", "") +
                            "\n\nCan Cook South Indian: " + sp.getString("canCookSouthIndian", "") +
                            "\n\nSports College Level: " + sp.getString("sportsCollegeLevel", "") +
                            "\n\nSports District Level: " + sp.getString("sportsDistrictLevel", "") +
                            "\n\nDisclose: " + sp.getString("disclose", "")
            );
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
