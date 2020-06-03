package com.singularitycoder.folkdatabase.profile;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.database.AllUsersItem;
import com.singularitycoder.folkdatabase.helper.HelperConstants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.singularitycoder.folkdatabase.helper.FolkDatabaseApp.hasInternet;
import static java.lang.String.valueOf;

public class BasicInfoFragment extends Fragment {

    private static final String TAG = "BasicInfoFragment";

    @Nullable
    @BindView(R.id.tv_basic_info_about_name)
    TextView tvName;
    @Nullable
    @BindView(R.id.tv_basic_info_about_email)
    TextView tvEmail;
    @Nullable
    @BindView(R.id.tv_basic_info_about_gmail)
    TextView tvGmail;
    @Nullable
    @BindView(R.id.tv_basic_info_about_mobile)
    TextView tvMobile;
    @Nullable
    @BindView(R.id.tv_basic_info_about_whatsapp)
    TextView tvWhatsApp;
    @Nullable
    @BindView(R.id.tv_basic_info_about_hkm_joining_date)
    TextView tvJoiningDate;
    @Nullable
    @BindView(R.id.tv_basic_info_about_account_created_on)
    TextView tvAccountCreationDate;

    @NonNull
    private String emailKey;

    @NonNull
    private Unbinder unbinder;

    @NonNull
    private ProgressDialog loadingBar;

    private AllUsersItem allUsersItem;


    public BasicInfoFragment() {
    }

    public BasicInfoFragment(@NotNull String emailKey) {
        this.emailKey = emailKey;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_basic_info, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        loadingBar = new ProgressDialog(getContext());
        if (hasInternet()) AsyncTask.SERIAL_EXECUTOR.execute(this::readBasicData);
        return view;
    }

    // READ
    private void readBasicData() {
        getActivity().runOnUiThread(() -> {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            if (null != loadingBar && !loadingBar.isShowing()) loadingBar.show();
        });

        FirebaseFirestore.getInstance()
                .collection(HelperConstants.COLL_AUTH_FOLK_MEMBERS)
                .whereEqualTo("email", emailKey)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "docList: " + docList);

                        for (DocumentSnapshot docSnap : docList) {
                            allUsersItem = docSnap.toObject(AllUsersItem.class);
                            if (allUsersItem != null) {
                                Log.d(TAG, "AllUsersItem: " + allUsersItem);

                                if (!("").equals(valueOf(docSnap.getString("email")))) {
                                    tvEmail.setText(valueOf(docSnap.getString("email")));
                                    Log.d(TAG, "readBasicData: email: " + valueOf(docSnap.getString("email")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("fullName")))) {
                                    tvName.setText(valueOf(docSnap.getString("fullName")));
                                    Log.d(TAG, "readBasicData: fullname: " + valueOf(docSnap.getString("fullName")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("gmail")))) {
                                    tvGmail.setText(valueOf(docSnap.getString("gmail")));
                                    Log.d(TAG, "readBasicData: gmail: " + valueOf(docSnap.getString("gmail")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    tvMobile.setText(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readBasicData: phone: " + valueOf(docSnap.getString("phone")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("phone")))) {
                                    tvWhatsApp.setText(valueOf(docSnap.getString("phone")));
                                    Log.d(TAG, "readBasicData: phone: " + valueOf(docSnap.getString("phone")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("creationTimeStamp")))) {
                                    tvAccountCreationDate.setText(valueOf(docSnap.getString("creationTimeStamp")));
                                    Log.d(TAG, "readBasicData: creationTimeStamp: " + valueOf(docSnap.getString("creationTimeStamp")));
                                }

                                if (!("").equals(valueOf(docSnap.getString("hkmJoiningDate")))) {
                                    tvJoiningDate.setText(valueOf(docSnap.getString("hkmJoiningDate")));
                                    Log.d(TAG, "readBasicData: hkmJoiningDate: " + valueOf(docSnap.getString("hkmJoiningDate")));
                                }
                            }
                            Log.d(TAG, "firedoc id: " + docSnap.getId());
                        }
                        Toast.makeText(getContext(), "Got Data", Toast.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        });
                    }
                })
                .addOnFailureListener(e -> getActivity().runOnUiThread(() -> {
                    if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
