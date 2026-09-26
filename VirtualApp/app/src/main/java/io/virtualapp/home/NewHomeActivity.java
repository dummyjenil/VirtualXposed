package io.virtualapp.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.lody.virtual.client.core.VirtualCore;

import io.virtualapp.R;
import io.virtualapp.settings.AppManageActivity;
import io.virtualapp.settings.SettingsActivity;

/**
 * Modernized, Clean & Lightweight Home Activity
 */
public class NewHomeActivity extends ListAppActivity {

    private static final String TAG = "NewHomeActivity";
    public static final String SHARED_PREFERENCES_KEY = SettingsActivity.SHARED_PREFERENCES_KEY;
    private boolean mDirectlyBack = false;

    public static void goHome(Context context) {
        Intent intent = new Intent(context, NewHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        mDirectlyBack = sharedPreferences.getBoolean(SettingsActivity.DIRECTLY_BACK_KEY, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_app_manage) {
            startActivity(new Intent(this, AppManageActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Activity getActivity() {
        return this;
    }

    public Context getContext() {
        return this;
    }

    public void startVirtualActivity(Intent intent, Bundle options, int userId) {
        String packageName = intent.getPackage();
        if (TextUtils.isEmpty(packageName)) {
            ComponentName component = intent.getComponent();
            if (component != null) {
                packageName = component.getPackageName();
            }
        }
        if (packageName == null) {
            try {
                startActivity(intent);
                return;
            } catch (Throwable ignored) {
            }
        }
        boolean result = LoadingActivity.launch(this, packageName, userId);
        if (!result) {
            throw new ActivityNotFoundException("Cannot launch virtual activity for: " + intent);
        }
        if (mDirectlyBack) {
            finish();
        }
    }
}
