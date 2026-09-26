package io.virtualapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import io.virtualapp.R;

/**
 * Modern dialog utilities replacing deprecated ProgressDialog
 */
public class DialogUtil {

    public static void showDialog(Dialog dialog) {
        if (dialog == null) {
            return;
        }
        try {
            dialog.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog == null) {
            return;
        }
        try {
            dialog.dismiss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a modern Material progress dialog without using deprecated ProgressDialog
     */
    public static AlertDialog createProgressDialog(Context context, CharSequence message) {
        int padding = (int) (24 * context.getResources().getDisplayMetrics().density);
        int margin = (int) (16 * context.getResources().getDisplayMetrics().density);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(padding, padding, padding, padding);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        LinearLayout.LayoutParams pbParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        pbParams.rightMargin = margin;
        layout.addView(progressBar, pbParams);

        if (message != null && message.length() > 0) {
            TextView tv = new TextView(context);
            tv.setId(android.R.id.message);
            tv.setText(message);
            tv.setTextSize(16);
            layout.addView(tv);
        }

        AlertDialog dialog = new AlertDialog.Builder(context, R.style.VAAlertTheme)
                .setView(layout)
                .setCancelable(false)
                .create();

        return dialog;
    }

    public static void updateProgressMessage(Dialog dialog, CharSequence message) {
        if (dialog == null) {
            return;
        }
        TextView tv = dialog.findViewById(android.R.id.message);
        if (tv != null) {
            tv.setText(message);
        }
    }
}
