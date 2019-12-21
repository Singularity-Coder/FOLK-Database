package com.singularitycoder.folkdatabase.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.singularitycoder.folkdatabase.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HelperGeneral extends AppCompatActivity {

    private static final String TAG = "HelperGeneral";
    private final static int FADE_DURATION = 550;

    // Different Toasts n Snackbars
    // Image Viewing Libraries
    // Take picture or select from gallery
    // Different Dialog boiler plates

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void progressDialog(Context context) {
        ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
    }

    public static void dismissDialog(Context context) {
        ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        dialog.dismiss();
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }

    public static String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
//        String formatTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String formattedTime = sdf.format(currentTime);
        return formattedTime;
    }

    public static boolean isThisTimeGreater(String currentTime, String EndTime) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date time1 = sdf.parse(currentTime);
            Date time2 = sdf.parse(EndTime);
            if (time1 != null && time1.after(time2)) {
                System.out.println("time1 is after time2");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static boolean isThisDateGreater(String currentDate, String EndDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(currentDate);
            Date date2 = sdf.parse(EndDate);
            if (date1 != null && date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String convertDateFormat(String inputDate) {
        if (inputDate != null) {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return outputFormat.format(Objects.requireNonNull(date));
        } else {
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String currentDate() {
        String date = new SimpleDateFormat("MM-dd").format(new Date());
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public static String currentDateTime() {
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // split date and time for event created date
        String[] arrOfStr = dateTime.split(" ", 2);
        ArrayList<String> dateAndTime = new ArrayList<>(Arrays.asList(arrOfStr));

        // convert date to dd/mm/yyyy
        Date dateObj = null;
        try {
            dateObj = new SimpleDateFormat("yyyy-MM-dd").parse(dateAndTime.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDate = new SimpleDateFormat("dd MMM yyyy").format(dateObj);
        Log.d(TAG, "date: " + outputDate);

        // convert time to 12 hr format
        Date timeObj = null;
        try {
            timeObj = new SimpleDateFormat("H:mm:ss").parse(dateAndTime.get(1));
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        String outputTime = new SimpleDateFormat("hh:mm a").format(timeObj);
        Log.d(TAG, "time: " + outputTime);

        return outputDate + " at " + outputTime;
    }

    public static void showDatePicker(final TextView datefield, Context context) {
// Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd/MM/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        datefield.setText(sdf.format(c.getTime()));
                    }
                },
                mYear,
                mMonth,
                mDay).show();
    }

    public static void showTimePicker(final TextView timeField, Activity activity) {
// Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        new TimePickerDialog(
                activity,
                (view, hourOfDay, minute) -> {
//                        txtTime.setText(hourOfDay + ":" + minute);
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);

                    String myFormat = "hh:mm aa";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    timeField.setText(sdf.format(c.getTime()));
                },
                mHour,
                mMinute,
                false).show();
    }

    public void statusBarStuff(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  // clear FLAG_TRANSLUCENT_STATUS flag:
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.setStatusBarColor(ContextCompat.getColor(activity, color));   // change the color
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void dialogComingSoon(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Coming Soon")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })
                .show();
    }

    public static void toast(String msg, Context context, int length) {
        Toast.makeText(context, msg, length).show();
    }

    private void dialogSingleChoice(final String[] stringArray, final String dialogTitle) {
        final String[] single_choice_selected = {stringArray[0]};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setSingleChoiceItems(stringArray, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                single_choice_selected[0] = stringArray[i];
            }
        });

        builder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TAG", "selected language is-->>" + single_choice_selected[0]);
                switch (dialogTitle) {
                    case "con 1":
                        TextView s1 = null;
                        s1.setText(single_choice_selected[0]);
                        break;
                    case "con 2":
                        TextView s2 = null;
                        s2.setText(single_choice_selected[0]);
                        break;
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    public void generateCustomDialog(Activity activity, int layoutId, Dialog dialogName) {
        dialogName = new Dialog(activity);
        dialogName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogName.setCancelable(true);
        dialogName.setContentView(layoutId);

        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogName.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialogName.getWindow().getAttributes().height);

        dialogName.show();
    }

    // Get activity instance from any context.
    public static Activity giveMeActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper)
            return giveMeActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static boolean hasValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static boolean hasValidEmail(final String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static void glideLargeImage(Context context, String imgUrl, ImageView imageView, String empty1) {
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .placeholder(R.color.colorAccent)
                .error(R.drawable.profile_dummy_large)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(context).load(imgUrl)
                .transition(withCrossFade(300))     // giving issues
                .apply(requestOptions)
                .into(imageView);
    }

    public static void glideProfileImage(Context context, String imgUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.colorAccent)
                .error(R.drawable.profile_dummy_large)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(context).load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void glideSmallImageWithErrHandle(Context context, String imgUrl, ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .apply(
                        new RequestOptions()
                                .error(R.drawable.header)
                                .placeholder(R.color.colorAccent)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        Toast.makeText(context, "Bad Image! Loading default!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(imageView);
    }

    // Glide Big with error handling
    public static void glideImageWithErrHandle(Context context, String imgUrl, ImageView imageView, String empty1) {
        Glide.with(context)
                .load(imgUrl)
                .apply(
                        new RequestOptions()
                                .error(R.drawable.header)
                                .placeholder(R.color.colorAccent)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        Toast.makeText(context, "Bad Image! Loading default!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(imageView);
    }

    public static void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    public static void showSnack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void toast(Context context, String message, int length) {
        switch (length) {
            case 0:
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
