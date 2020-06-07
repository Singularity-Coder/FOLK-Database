package com.singularitycoder.folkdatabase.profile.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.helper.AllCallbacks;
import com.singularitycoder.folkdatabase.helper.Status;
import com.singularitycoder.folkdatabase.profile.viewmodel.ProfileViewModel;

import org.jetbrains.annotations.NotNull;

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
        getBasicInfo();
        return view;
    }

    private void getBasicInfo() {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        if (hasInternet()) {
            final Observer<AllCallbacks> observer = allCallbacks -> {

                if (Status.LOADING == allCallbacks.getStatus()) {
                    getActivity().runOnUiThread(() -> {
                        loadingBar.setMessage(valueOf(allCallbacks.getMessage()));
                        loadingBar.setCanceledOnTouchOutside(false);
                        if (null != loadingBar && !loadingBar.isShowing()) loadingBar.show();
                    });
                }

                if (Status.SUCCESS == allCallbacks.getStatus()) {
                    getActivity().runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        Toast.makeText(getContext(), valueOf(allCallbacks.getMessage()), Toast.LENGTH_SHORT).show();

                        tvName.setText(((AuthUserItem) allCallbacks.getData()).getFullName());
                        tvEmail.setText(((AuthUserItem) allCallbacks.getData()).getEmail());
                        tvGmail.setText(((AuthUserItem) allCallbacks.getData()).getGmail());
                        tvMobile.setText(((AuthUserItem) allCallbacks.getData()).getPhone());
                        tvWhatsApp.setText(((AuthUserItem) allCallbacks.getData()).getPhone());
                        tvJoiningDate.setText(((AuthUserItem) allCallbacks.getData()).getHkmJoiningDate());
                        tvAccountCreationDate.setText(((AuthUserItem) allCallbacks.getData()).getCreationTimeStamp());
                    });
                }

                if (Status.EMPTY == allCallbacks.getStatus()) {
                    getActivity().runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        Toast.makeText(getContext(), valueOf(allCallbacks.getMessage()), Toast.LENGTH_SHORT).show();
                    });
                }

                if (Status.ERROR == allCallbacks.getStatus()) {
                    getActivity().runOnUiThread(() -> {
                        if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                        Toast.makeText(getContext(), valueOf(allCallbacks.getMessage()), Toast.LENGTH_SHORT).show();
                    });
                }
            };
            profileViewModel.getBasicInfoFromRepository(emailKey).observe(getViewLifecycleOwner(), observer);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
