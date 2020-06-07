package com.singularitycoder.folkdatabase.profile.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.profile.adapter.ProfileActivitiesAdapter;
import com.singularitycoder.folkdatabase.profile.model.ProfileContactItem;

import java.util.ArrayList;

public class UserActivityFragment extends Fragment {

    private static final String TAG = "ActivityFragment";

    private String emailKey;

    private RecyclerView recyclerView;
    private ProfileActivitiesAdapter mProfileActivitiesAdapter;
    private ArrayList<ProfileContactItem> activityList;


    public UserActivityFragment() {
    }

    public UserActivityFragment(String emailKey) {
        this.emailKey = emailKey;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_activity, container, false);
        init(view);
        setUpRecyclerView();
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recycler_profile_activity);
    }

    private void setUpRecyclerView() {
        if (null != getActivity()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            activityList = new ArrayList<>();
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));
            activityList.add(new ProfileContactItem(R.drawable.profile_dummy_large, "Catherine Bennet", "12 July, 4819 @ 6:00 AM", "Called", ""));

            mProfileActivitiesAdapter = new ProfileActivitiesAdapter(activityList, getActivity());
            mProfileActivitiesAdapter.setHasStableIds(true);
            recyclerView.setAdapter(mProfileActivitiesAdapter);
        }
    }

//        @Override
//        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.menu_contacts, menu);
//            super.onCreateOptionsMenu(menu, inflater);
//        }

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
