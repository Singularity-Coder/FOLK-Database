package com.singularitycoder.folkdatabase;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Helper extends AppCompatActivity {

    // Different Toasts n Snackbars
    // Image Viewing Libraries
    // Take picture or select from gallery
    // Different Dialog boiler plates

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showDatePicker(final TextView datefield, Context context) {
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

    public void showTimePicker(final TextView timeField) {
// Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        new TimePickerDialog(
                this,
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

    public void comingSoonDialog(Activity activity) {
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

    public void toast(String msg, Context context, int length) {
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

    /**
     * Get activity instance from desired context.
     */
    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper)
            return getActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }


    public static Activity giveMeActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper)
            return getActivity(((ContextWrapper) context).getBaseContext());
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

}
