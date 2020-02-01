package com.singularitycoder.folkdatabase.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.HelperConstants;
import com.singularitycoder.folkdatabase.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.singularitycoder.folkdatabase.helper.HelperGeneral.hasInternet;

public class FolkGuidesFragment extends Fragment {

    private static final String TAG = FolkGuidesFragment.class.getSimpleName();
    private ArrayList<FolkGuideItem> folkGuidesList;
    private RecyclerView recyclerView;
    private FolkGuidesAdapter folkGuidesAdapter;
    private FolkGuideItem folkGuideItem;
    private ProgressDialog loadingBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noInternetText;
    private TextView tvListCount;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LottieAnimationView noFeedImage;
    private TextView noFeedText;

    public FolkGuidesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        if (null != getActivity()) {
            init(view);
            setRefreshLayout();
            getData(getActivity());
            setUpRecyclerView();
        }
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recycler_person);
        noInternetText = view.findViewById(R.id.tv_no_internet);

        noFeedText = view.findViewById(R.id.tv_no_feed_text);
        noFeedImage = view.findViewById(R.id.img_no_feed_lottie_image);
        noFeedImage.setAnimation(R.raw.empty_box);
        noFeedImage.playAnimation();

        tvListCount = view.findViewById(R.id.tv_list_count);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        if (null != getActivity()) {
            loadingBar = new ProgressDialog(getActivity());
        }
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
    }

    private void setRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            if (null != getActivity()) {
                getData(getActivity());
            }
        });
    }

    private void setUpRecyclerView() {
        if (null != getActivity()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            folkGuidesList = new ArrayList<>();

            folkGuidesAdapter = new FolkGuidesAdapter(getContext(), folkGuidesList);
            folkGuidesAdapter.setHasStableIds(true);
            folkGuidesAdapter.setOnItemClickListener(new FolkGuidesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    // Start activity
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    FolkGuideItem folkGuideItem = folkGuidesList.get(position);
                    intent.putExtra("profileKey", "FOLKGUIDE");
                    intent.putExtra("folkguideItem", folkGuideItem);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(folkGuidesAdapter);
        }
    }

    private void getData(final Context context) {
        if (hasInternet(context)) {
            Log.d(TAG, "hit 1");
            setUpRecyclerView();
            AsyncTask.execute(this::readFolkGuidesData);
            noInternetText.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            folkGuidesAdapter.notifyDataSetChanged();
        } else {
            noInternetText.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    // READ
    private void readFolkGuidesData() {
        if (null != getActivity()) {
            SharedPreferences sp = getActivity().getSharedPreferences("authItem", Context.MODE_PRIVATE);
            String zone = sp.getString("zone", "");

            FirebaseFirestore.getInstance()
                    .collection(HelperConstants.COLL_AUTH_FOLK_GUIDES)
                    .whereEqualTo("zone", zone)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                            Log.d(TAG, "docList: " + docList);

                            for (DocumentSnapshot docSnap : docList) {
                                folkGuideItem = docSnap.toObject(FolkGuideItem.class);
                                if (folkGuideItem != null) {
                                    Log.d(TAG, "personItem: " + folkGuideItem);
                                    folkGuideItem.setId(docSnap.getId());
                                    folkGuideItem.setStrFirstName(docSnap.getString(HelperConstants.KEY_FG_NAME));
                                    folkGuideItem.setStrFolkGuideAbbr(docSnap.getString(HelperConstants.KEY_FG_ABBR));
                                    folkGuideItem.setStrZone(docSnap.getString(HelperConstants.KEY_ZONE));
                                    folkGuideItem.setStrPhone(docSnap.getString(HelperConstants.KEY_MOBILE_NUMBER));
                                    folkGuideItem.setStrWhatsApp(docSnap.getString(HelperConstants.KEY_MOBILE_NUMBER));
                                    folkGuideItem.setStrEmail(docSnap.getString(HelperConstants.KEY_EMAIL));
                                }
                                Log.d(TAG, "firedoc id: " + docSnap.getId());
                                folkGuidesList.add(folkGuideItem);
                                tvListCount.setText(folkGuidesList.size() + " FOLK Guides");
                            }
                            folkGuidesAdapter.notifyDataSetChanged();
                            if (null != getActivity()) {
                                Toast.makeText(getActivity(), "Got Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (folkGuidesList.size() == 0) {
                            noFeedImage.setVisibility(View.VISIBLE);
                            noFeedText.setVisibility(View.VISIBLE);
                        } else {
                            noFeedImage.setVisibility(View.GONE);
                            noFeedText.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (null != getActivity()) {
                            Toast.makeText(getActivity(), "Couldn't get data!", Toast.LENGTH_SHORT).show();
                        }
                        noFeedImage.setVisibility(View.VISIBLE);
                        noFeedText.setVisibility(View.VISIBLE);
                    });
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_folk_guides, menu);

        MenuItem searchItem = menu.findItem(R.id.action_folk_guide_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search Folk Guides");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFolkGuides(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_folk_guide_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void searchFolkGuides(String text) {
        ArrayList<FolkGuideItem> filterdUsers = new ArrayList<>();
        for (FolkGuideItem folkGuide : folkGuidesList) {
            if (folkGuide.getStrFirstName().toLowerCase().trim().contains(text.toLowerCase())) {
                filterdUsers.add(folkGuide);
            }
        }
        folkGuidesAdapter.flterList(filterdUsers);
    }
}