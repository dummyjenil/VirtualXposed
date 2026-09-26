package io.virtualapp.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.lsposed.LSPosedSelfTest;

import java.io.File;
import java.io.IOException;

import io.virtualapp.R;
import io.virtualapp.gms.FakeGms;

/**
 * Modern, Clean Settings Activity using AndroidX PreferenceFragmentCompat
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_KEY = "com.android.launcher3.prefs";
    public static final String DIRECTLY_BACK_KEY = "advance_settings_directly_back";
    private static final String APP_MANAGE_KEY = "settings_app_manage";
    private static final String TASK_MANAGE_KEY = "settings_task_manage";
    private static final String REBOOT_KEY = "settings_reboot";
    private static final String INSTALL_GMS_KEY = "advance_settings_install_gms";
    private static final String INSTALL_PLAYSTORE_KEY = "advance_settings_install_playstore";
    private static final String ALLOW_FAKE_SIGNATURE = "advance_settings_allow_fake_signature";
    private static final String DISABLE_XPOSED = "advance_settings_disable_xposed";
    private static final String LSPOSED_TEST_KEY = "settings_lsposed_test";

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
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Modern AndroidX PreferenceFragment
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            getPreferenceManager().setSharedPreferencesName(SHARED_PREFERENCES_KEY);
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);

            Preference appManage = findPreference(APP_MANAGE_KEY);
            Preference taskManage = findPreference(TASK_MANAGE_KEY);
            Preference lsposedTest = findPreference(LSPOSED_TEST_KEY);
            Preference reboot = findPreference(REBOOT_KEY);
            Preference installGms = findPreference(INSTALL_GMS_KEY);
            Preference installPlayStore = findPreference(INSTALL_PLAYSTORE_KEY);
            SwitchPreference allowFakeSignature = findPreference(ALLOW_FAKE_SIGNATURE);
            SwitchPreference disableXposed = findPreference(DISABLE_XPOSED);

            if (appManage != null) {
                appManage.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(getActivity(), AppManageActivity.class));
                    return true;
                });
            }

            if (taskManage != null) {
                taskManage.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(getActivity(), TaskManageActivity.class));
                    return true;
                });
            }

            if (lsposedTest != null) {
                lsposedTest.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        LSPosedSelfTest.TestReport report = LSPosedSelfTest.runSelfTest();
                        new AlertDialog.Builder(getActivity())
                                .setTitle(report.isSuccess ? "LSPosed Engine Active" : "LSPosed Test Failed")
                                .setMessage(report.toString())
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                    return true;
                });
            }

            if (reboot != null) {
                reboot.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.settings_reboot_title)
                                .setMessage(R.string.settings_reboot_content)
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    VirtualCore.get().killAllApps();
                                    if (getContext() != null) {
                                        Toast.makeText(getContext(), R.string.reboot_tips_1, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .show();
                    }
                    return true;
                });
            }

            if (installGms != null) {
                updateGmsPreferenceState(installGms);
                installGms.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        boolean alreadyInstalled = FakeGms.isAlreadyInstalled(getActivity());
                        if (alreadyInstalled) {
                            FakeGms.uninstallGms(getActivity(), () -> updateGmsPreferenceState(installGms));
                        } else {
                            FakeGms.installGms(getActivity(), () -> updateGmsPreferenceState(installGms));
                        }
                    }
                    return true;
                });
            }

            if (installPlayStore != null) {
                updatePlayStorePreferenceState(installPlayStore);
                installPlayStore.setOnPreferenceClickListener(preference -> {
                    if (getActivity() != null) {
                        boolean alreadyInstalled = FakeGms.isPlayStoreInstalled(getActivity());
                        if (alreadyInstalled) {
                            FakeGms.uninstallPlayStore(getActivity(), () -> updatePlayStorePreferenceState(installPlayStore));
                        } else {
                            FakeGms.installPlayStore(getActivity(), () -> updatePlayStorePreferenceState(installPlayStore));
                        }
                    }
                    return true;
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

        private void updateGmsPreferenceState(Preference preference) {
            if (preference == null || getActivity() == null) return;
            boolean installed = FakeGms.isAlreadyInstalled(getActivity());
            if (installed) {
                preference.setTitle("Google Services (MicroG) [Installed]");
                preference.setSummary("Tap to uninstall Google Services from virtual environment");
            } else {
                preference.setTitle("Install Google Services (MicroG)");
                preference.setSummary("Download and install official MicroG Core, GSF Proxy & Store Companion");
            }
        }

        private void updatePlayStorePreferenceState(Preference preference) {
            if (preference == null || getActivity() == null) return;
            boolean installed = FakeGms.isPlayStoreInstalled(getActivity());
            if (installed) {
                preference.setTitle("Google Play Store [Installed]");
                preference.setSummary("Tap to uninstall Google Play Store from virtual environment");
            } else {
                preference.setTitle("Install Google Play Store");
                preference.setSummary("Download and install official Play Store client (Aurora Store)");
            }
        }
    }
}
