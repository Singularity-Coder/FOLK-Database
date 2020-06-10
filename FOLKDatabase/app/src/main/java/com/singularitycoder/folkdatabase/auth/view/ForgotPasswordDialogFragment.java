package com.singularitycoder.folkdatabase.auth.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.viewmodel.AuthViewModel;
import com.singularitycoder.folkdatabase.helper.CustomEditText;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.helper.RequestStateMediator;
import com.singularitycoder.folkdatabase.helper.Status;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.singularitycoder.folkdatabase.helper.FolkDatabaseApp.hasInternet;
import static java.lang.String.valueOf;

public class ForgotPasswordDialogFragment extends DialogFragment {

    private static final String TAG = "ForgotPasswordDialogFra";

    @Nullable
    @BindView(R.id.con_lay_root_forgot_password)
    ConstraintLayout conLayRootForgotPasswordDialog;

    private final HelperGeneral helperObject = new HelperGeneral();

    private Unbinder unbinder;
    private ProgressDialog loadingBar;
    private DialogActionListener listener;
    private AuthViewModel authViewModel;

    public ForgotPasswordDialogFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
//        try {
//            listener = (DialogActionListener) context;     // Instantiate the NoticeDialogListener so we can send events to the host
//        } catch (ClassCastException e) {
//            throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListener");    // If activity doesn't implement the interface, throw exception
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments() && ("fullScreen").equals(getArguments().getString("DIALOG_TYPE"))) {
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // for rounded corners

        return dialog;
//        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_forgot_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        if ((null) != getActivity()) loadingBar = new ProgressDialog(getActivity());

        ImageView imgClose = view.findViewById(R.id.img_close);
        CustomEditText etResetEmail = view.findViewById(R.id.et_reset_email);
        Button btnReset = view.findViewById(R.id.btn_reset_password);

        imgClose.setOnClickListener(view1 -> dismiss());

        btnReset.setOnClickListener(view2 -> {
            Log.d(TAG, "dialogForgotPassword: email: " + valueOf(etResetEmail.getText()).trim());
            if (hasInternet()) {
                if (hasValidInput(etResetEmail)) {
                    authViewModel.resetPasswordFromRepository(valueOf(etResetEmail.getText()).trim()).observe(getViewLifecycleOwner(), liveDataObserver(valueOf(etResetEmail.getText()).trim()));
                }
            } else {
                Toast.makeText(helperObject, "No Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Observer liveDataObserver(String email) {
        Observer<RequestStateMediator> observer = null;
        if (hasInternet()) {
            observer = requestStateMediator -> {

                if (Status.LOADING == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            loadingBar.setMessage(valueOf(requestStateMediator.getMessage()));
                            loadingBar.setCanceledOnTouchOutside(false);
                            if (null != loadingBar && !loadingBar.isShowing()) loadingBar.show();
                        });
                    }
                }

                if (Status.SUCCESS == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                            Toast.makeText(getContext(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                            dismiss();
                        });
                    }
                }

                if (Status.EMPTY == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                            Toast.makeText(getContext(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                if (Status.ERROR == requestStateMediator.getStatus()) {
                    if (null != getActivity()) {
                        getActivity().runOnUiThread(() -> {
                            if (("RESET PASSWORD").equals(requestStateMediator.getKey())) {
//                                helperObject.showSnackBar(
//                                        conLayRootForgotPasswordDialog,
//                                        "Failed to send reset email!",
//                                        getResources().getColor(R.color.colorWhite),
//                                        "RELOAD",
//                                        view -> authViewModel.resetPasswordFromRepository(email).observe(getViewLifecycleOwner(), liveDataObserver(email)));
                            }

                            if (null != loadingBar && loadingBar.isShowing()) loadingBar.dismiss();
                            Toast.makeText(getActivity(), valueOf(requestStateMediator.getMessage()), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            };
        }
        return observer;
    }

    private boolean hasValidInput(CustomEditText etEmail) {
        String email = valueOf(etEmail.getText()).trim();

        if (email.equals("")) {
            etEmail.setError("Email is Required!");
            etEmail.requestFocus();
            return false;
        }

        if (!helperObject.hasValidEmail(email)) {
            etEmail.setError("Invalid Email!");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface DialogActionListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);

        public void onDialogNeutralClick(DialogFragment dialog);
    }
}
