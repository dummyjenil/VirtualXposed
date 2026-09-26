package io.virtualapp.settings;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.ipc.VActivityManager;

import java.io.File;
import java.io.IOException;

import io.virtualapp.R;
import io.virtualapp.VCommends;
import io.virtualapp.gms.FakeGms;
import io.virtualapp.home.ListAppActivity;
import io.virtualapp.utils.Misc;

/**
 * Modernized Settings Activity using AndroidX PreferenceFragmentCompat and AppCompatActivity
 */
public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    public static final String SHARED_PREFERENCES_KEY = "com.android.launcher3.prefs";
    private static final String ADVANCE_SETTINGS_KEY = "settings_advance";
    private static final String ADD_APP_KEY = "settings_add_app";
    private static final String MODULE_MANAGE_KEY = "settings_module_manage";
    private static final String APP_MANAGE_KEY = "settings_app_manage";
    private static final String TASK_MANAGE_KEY = "settings_task_manage";
    private static final String DESKTOP_SETTINGS_KEY = "settings_desktop";
    private static final String FAQ_SETTINGS_KEY = "settings_faq";
    private static final String ABOUT_KEY = "settings_about";
    private static final String REBOOT_KEY = "settings_reboot";
    private static final String HIDE_SETTINGS_KEY = "advance_settings_hide_settings";
    private static final String DISABLE_INSTALLER_KEY = "advance_settings_disable_installer";
    public static final String ENABLE_LAUNCHER = "advance_settings_enable_launcher";
    private static final String INSTALL_GMS_KEY = "advance_settings_install_gms";
    public static final String DIRECTLY_BACK_KEY = "advance_settings_directly_back";
    private static final String RECOMMEND_PLUGIN = "settings_plugin_recommend";
    private static final String DISABLE_RESIDENT_NOTIFICATION = "advance_settings_disable_resident_notification";
    private static final String ALLOW_FAKE_SIGNATURE = "advance_settings_allow_fake_signature";
    private static final String DISABLE_XPOSED = "advance_settings_disable_xposed";
    private static final String FILE_MANAGE = "settings_file_manage";
    private static final String PERMISSION_MANAGE = "settings_permission_manage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.settings_title);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.getKey());
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, pref.getKey())
                .addToBackStack(pref.getKey())
                .commit();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Modern AndroidX PreferenceFragmentCompat
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            getPreferenceManager().setSharedPreferencesName(SHARED_PREFERENCES_KEY);
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);

            Preference addApp = findPreference(ADD_APP_KEY);
            Preference moduleManage = findPreference(MODULE_MANAGE_KEY);
            Preference recommend = findPreference(RECOMMEND_PLUGIN);
            Preference appManage = findPreference(APP_MANAGE_KEY);
            Preference taskManage = findPreference(TASK_MANAGE_KEY);
            Preference desktop = findPreference(DESKTOP_SETTINGS_KEY);
            Preference faq = findPreference(FAQ_SETTINGS_KEY);
            Preference about = findPreference(ABOUT_KEY);
            Preference reboot = findPreference(REBOOT_KEY);
            Preference fileMange = findPreference(FILE_MANAGE);
            Preference permissionManage = findPreference(PERMISSION_MANAGE);

            SwitchPreference disableInstaller = findPreference(DISABLE_INSTALLER_KEY);
            SwitchPreference enableLauncher = findPreference(ENABLE_LAUNCHER);
            SwitchPreference disableResidentNotification = findPreference(DISABLE_RESIDENT_NOTIFICATION);
            SwitchPreference allowFakeSignature = findPreference(ALLOW_FAKE_SIGNATURE);
            SwitchPreference disableXposed = findPreference(DISABLE_XPOSED);

            if (addApp != null) {
                addApp.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        ListAppActivity.gotoListApp(getActivity());
                    }
                    return false;
                });
            }

            if (moduleManage != null) {
                moduleManage.setOnPreferenceClickListener(preference -> {
                    try {
                        Intent t = new Intent();
                        t.setComponent(new ComponentName("de.robv.android.xposed.installer", "de.robv.android.xposed.installer.WelcomeActivity"));
                        t.putExtra("fragment", 1);
                        int ret = VActivityManager.get().startActivity(t, 0);
                        if (ret < 0 && getContext() != null) {
                            Toast.makeText(getContext(), R.string.xposed_installer_not_found, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Throwable ignored) {
                        ignored.printStackTrace();
                    }
                    return false;
                });
            }

            if (recommend != null) {
                recommend.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(getActivity(), RecommendPluginActivity.class));
                    return false;
                });
            }

            boolean xposedEnabled = VirtualCore.get().isXposedEnabled();
            if (!xposedEnabled) {
                if (moduleManage != null) {
                    getPreferenceScreen().removePreference(moduleManage);
                }
                if (recommend != null) {
                    getPreferenceScreen().removePreference(recommend);
                }
            }

            if (appManage != null) {
                appManage.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(getActivity(), AppManageActivity.class));
                    return false;
                });
            }

            if (taskManage != null) {
                taskManage.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(getActivity(), TaskManageActivity.class));
                    return false;
                });
            }

            if (faq != null) {
                faq.setOnPreferenceClickListener(preference -> {
                    Uri uri = Uri.parse("https://github.com/android-hacker/VAExposed/wiki/FAQ");
                    Intent t = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(t);
                    return false;
                });
            }

            if (desktop != null) {
                desktop.setOnPreferenceClickListener(preference -> {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), R.string.settings_desktop_text, Toast.LENGTH_SHORT).show();
                    }
                    return false;
                });
            }

            if (about != null) {
                about.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    return false;
                });
            }

            if (reboot != null) {
                reboot.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.settings_reboot_title)
                                .setMessage(getResources().getString(R.string.settings_reboot_content))
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    VirtualCore.get().killAllApps();
                                    if (getContext() != null) {
                                        Toast.makeText(getContext(), R.string.reboot_tips_1, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .show();
                    }
                    return false;
                });
            }

            if (disableInstaller != null) {
                disableInstaller.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (!(newValue instanceof Boolean) || getActivity() == null) {
                        return false;
                    }
                    try {
                        boolean disable = (boolean) newValue;
                        PackageManager packageManager = getActivity().getPackageManager();
                        packageManager.setComponentEnabledSetting(new ComponentName(getActivity().getPackageName(), "vxp.installer"),
                                !disable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                        return true;
                    } catch (Throwable ignored) {
                        return false;
                    }
                });
            }

            if (enableLauncher != null) {
                enableLauncher.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (!(newValue instanceof Boolean) || getActivity() == null) {
                        return false;
                    }
                    try {
                        boolean enable = (boolean) newValue;
                        PackageManager packageManager = getActivity().getPackageManager();
                        packageManager.setComponentEnabledSetting(new ComponentName(getActivity().getPackageName(), "vxp.launcher"),
                                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                        return true;
                    } catch (Throwable ignored) {
                        return false;
                    }
                });
            }

            Preference installGms = findPreference(INSTALL_GMS_KEY);
            if (installGms != null) {
                installGms.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        boolean alreadyInstalled = FakeGms.isAlreadyInstalled(getActivity());
                        if (alreadyInstalled) {
                            FakeGms.uninstallGms(getActivity());
                        } else {
                            FakeGms.installGms(getActivity());
                        }
                    }
                    return true;
                });
            }

            if (fileMange != null) {
                fileMange.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        OnlinePlugin.openOrDownload(getActivity(), OnlinePlugin.FILE_MANAGE_PACKAGE,
                                OnlinePlugin.FILE_MANAGE_URL, getString(R.string.install_file_manager_tips));
                    }
                    return false;
                });
            }

            if (permissionManage != null) {
                permissionManage.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        OnlinePlugin.openOrDownload(getActivity(), OnlinePlugin.PERMISSION_MANAGE_PACKAGE,
                                OnlinePlugin.PERMISSION_MANAGE_URL, getString(R.string.install_permission_manager_tips));
                    }
                    return false;
                });
            }

            if (disableXposed != null) {
                disableXposed.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (!(newValue instanceof Boolean) || getActivity() == null) {
                        return false;
                    }

                    boolean on = (boolean) newValue;
                    File disableXposedFile = getActivity().getFileStreamPath(".disable_xposed");
                    if (on) {
                        try {
                            return disableXposedFile.createNewFile();
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        return !disableXposedFile.exists() || disableXposedFile.delete();
                    }
                });
            }

            if (disableResidentNotification != null) {
                disableResidentNotification.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (!(newValue instanceof Boolean) || getActivity() == null) {
                        return false;
                    }

                    boolean on = (boolean) newValue;
                    File flag = getActivity().getFileStreamPath(Constants.NO_NOTIFICATION_FLAG);
                    if (on) {
                        try {
                            return flag.createNewFile();
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        return !flag.exists() || flag.delete();
                    }
                });
            }

            if (Build.VERSION.SDK_INT < 25 && disableResidentNotification != null) {
                PreferenceScreen advance = findPreference(ADVANCE_SETTINGS_KEY);
                if (advance != null) {
                    advance.removePreference(disableResidentNotification);
                }
            }

            if (allowFakeSignature != null) {
                allowFakeSignature.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (!(newValue instanceof Boolean) || getActivity() == null) {
                        return false;
                    }

                    boolean on = (boolean) newValue;
                    File flag = getActivity().getFileStreamPath(Constants.FAKE_SIGNATURE_FLAG);
                    if (on) {
                        try {
                            return flag.createNewFile();
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        return !flag.exists() || flag.delete();
                    }
                });
            }
        }

        @Override
        public void startActivity(@NonNull Intent intent) {
            try {
                super.startActivity(intent);
            } catch (Throwable ignored) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "startActivity failed.", Toast.LENGTH_SHORT).show();
                }
                ignored.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VCommends.REQUEST_SELECT_APP) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
