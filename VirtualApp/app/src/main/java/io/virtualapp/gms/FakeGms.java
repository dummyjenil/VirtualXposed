package io.virtualapp.gms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstallResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.virtualapp.R;
import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.utils.DialogUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Modernized FakeGms installer avoiding deprecated ProgressDialog
 */
public class FakeGms {

    private static final String TAG = "FakeGms";

    private static final String GMS_CONFIG_URL = "http://vaexposed.weishu.me/gms.json";

    private static final String GMS_PKG = "com.google.android.gms";
    private static final String GSF_PKG = "com.google.android.gsf";
    private static final String STORE_PKG = "com.android.vending";
    private static final String FAKE_GAPPS_PKG = "com.thermatk.android.xf.fakegapps";

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void uninstallGms(Activity activity) {
        if (activity == null) {
            return;
        }

        AlertDialog failDialog = new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                .setTitle(R.string.uninstall_gms_title)
                .setMessage(R.string.uninstall_gms_content)
                .setPositiveButton(R.string.uninstall_gms_ok, ((dialog1, which1) -> {
                    AlertDialog progressDialog = DialogUtil.createProgressDialog(activity, activity.getString(R.string.preparing));
                    DialogUtil.showDialog(progressDialog);
                    VUiKit.defer().when(() -> {
                        VirtualCore.get().uninstallPackage(GMS_PKG);
                        VirtualCore.get().uninstallPackage(GSF_PKG);
                        VirtualCore.get().uninstallPackage(STORE_PKG);
                        VirtualCore.get().uninstallPackage(FAKE_GAPPS_PKG);
                    }).then((v) -> {
                        DialogUtil.dismissDialog(progressDialog);
                        AlertDialog hits = new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                .setTitle(R.string.uninstall_gms_title)
                                .setMessage(R.string.uninstall_gms_success)
                                .setPositiveButton(android.R.string.ok, null)
                                .create();
                        DialogUtil.showDialog(hits);

                    }).fail((v) -> {
                        DialogUtil.dismissDialog(progressDialog);
                    });

                }))
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        DialogUtil.showDialog(failDialog);
    }

    public static boolean isAlreadyInstalled(Context context) {
        if (context == null) {
            return false;
        }

        boolean alreadyInstalled = true;
        if (!VirtualCore.get().isAppInstalled(GMS_PKG)) {
            alreadyInstalled = false;
        }
        if (!VirtualCore.get().isAppInstalled(GSF_PKG)) {
            alreadyInstalled = false;
        }
        if (!VirtualCore.get().isAppInstalled(STORE_PKG)) {
            alreadyInstalled = false;
        }
        if (!VirtualCore.get().isAppInstalled(FAKE_GAPPS_PKG)) {
            alreadyInstalled = false;
        }
        return alreadyInstalled;
    }

    public static void installGms(Activity activity) {
        if (activity == null) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                .setTitle(R.string.install_gms_title)
                .setMessage(R.string.install_gms_content)
                .setPositiveButton(android.R.string.ok, ((dialog, which) -> {
                    AlertDialog progressDialog = DialogUtil.createProgressDialog(activity, "Fetching GMS config...");
                    DialogUtil.showDialog(progressDialog);

                    executorService.submit(() -> {
                        String failMsg = installGmsInternal(activity, progressDialog);
                        Log.i(TAG, "install gms result: " + failMsg);
                        DialogUtil.dismissDialog(progressDialog);

                        if (failMsg == null) {
                            activity.runOnUiThread(() -> {
                                AlertDialog failDialog = new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                        .setTitle(R.string.install_gms_title)
                                        .setMessage(R.string.install_gms_success)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .create();
                                DialogUtil.showDialog(failDialog);
                            });
                        } else {
                            activity.runOnUiThread(() -> {
                                AlertDialog failDialog = new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                        .setTitle(R.string.install_gms_fail_title)
                                        .setMessage(R.string.install_gms_fail_content)
                                        .setPositiveButton(R.string.install_gms_fail_ok, ((dialog1, which1) -> {
                                            try {
                                                Intent t = new Intent(Intent.ACTION_VIEW);
                                                t.setData(Uri.parse("https://github.com/android-hacker/VirtualXposed/wiki/Google-service-support"));
                                                activity.startActivity(t);
                                            } catch (Throwable ignored) {
                                                ignored.printStackTrace();
                                            }
                                        }))
                                        .setNegativeButton(android.R.string.cancel, null)
                                        .create();
                                DialogUtil.showDialog(failDialog);
                            });

                        }
                    });
                }))
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        DialogUtil.showDialog(alertDialog);
    }


    private static String installGmsInternal(Activity activity, AlertDialog dialog) {
        File cacheDir = activity.getCacheDir();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(GMS_CONFIG_URL)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return "Download gms config failed, please check your network, error: 0";
        }

        if (!response.isSuccessful()) {
            return "Download gms config failed, please check your network, error: 1";
        }

        ResponseBody body = response.body();
        if (body == null) {
            return "Download gms config failed, please check your network, error: 2";
        }

        String gmsUrl = null;
        String gsfUrl = null;
        String storeUrl = null;
        String fakeGappsUrl = null;

        try {
            String configContent = body.string();
            JSONObject jsonObject = new JSONObject(configContent);
            gmsUrl = jsonObject.optString(GMS_PKG);
            gsfUrl = jsonObject.optString(GSF_PKG);
            storeUrl = jsonObject.optString(STORE_PKG);
            fakeGappsUrl = jsonObject.optString(FAKE_GAPPS_PKG);
        } catch (IOException | JSONException e) {
            return "parse gms config failed, please check your network, error: 3";
        }

        if (TextUtils.isEmpty(gmsUrl) || TextUtils.isEmpty(gsfUrl) || TextUtils.isEmpty(storeUrl) || TextUtils.isEmpty(fakeGappsUrl)) {
            return "invalid gms config, please check your network, error: 4";
        }

        // 1. download GSF
        File gsfFile = new File(cacheDir, "gsf.apk");
        boolean downloadGsf = downloadFile(gsfUrl, gsfFile, null);
        if (!downloadGsf) {
            return "Download Google Services Framework failed, error: 5";
        }

        // 2. download GMS
        File gmsFile = new File(cacheDir, "gms.apk");
        boolean downloadGms = downloadFile(gmsUrl, gmsFile, null);
        if (!downloadGms) {
            return "Download Google Play Services failed, error: 6";
        }

        // 3. download Vending
        File storeFile = new File(cacheDir, "vending.apk");
        boolean downloadVending = downloadFile(storeUrl, storeFile, null);
        if (!downloadVending) {
            return "Download Google Play Store failed, error: 7";
        }

        // 4. download FakeGapps
        File fakeGapps = new File(cacheDir, "fakeGapps.apk");
        boolean downloadFakeGapps = downloadFile(fakeGappsUrl, fakeGapps, null);
        if (!downloadFakeGapps) {
            return "Download FakeGapps failed, error: 8";
        }

        // 5. Install all of them!
        InstallResult gsfResult = VirtualCore.get().installPackage(gsfFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
        if (!gsfResult.isSuccess) {
            return "Install Google Service Framework failed: " + gsfResult.error;
        }
        gsfFile.delete();

        InstallResult gmsResult = VirtualCore.get().installPackage(gmsFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
        if (!gmsResult.isSuccess) {
            return "Install Google Play Service failed: " + gmsResult.error;
        }
        gmsFile.delete();

        InstallResult vendingResult = VirtualCore.get().installPackage(storeFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
        if (!vendingResult.isSuccess) {
            return "Install Google Play Store failed: " + vendingResult.error;
        }
        storeFile.delete();

        InstallResult fakeGappsResult = VirtualCore.get().installPackage(fakeGapps.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
        if (!fakeGappsResult.isSuccess) {
            return "Install FakeGapps failed: " + fakeGappsResult.error;
        }
        fakeGapps.delete();

        // 6. Enable FakeGapps
        File dataDir = VEnvironment.getDataUserPackageDirectory(0, "de.robv.android.xposed.installer");
        File modulePath = VEnvironment.getPackageResourcePath(FAKE_GAPPS_PKG);
        File configDir = new File(dataDir, "exposed_conf" + File.separator + "modules.list");
        FileWriter writer = null;
        try {
            writer = new FileWriter(configDir, true);
            writer.append(modulePath.getAbsolutePath());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public interface DownloadListener {
        void onProgress(int progress);
    }

    public static boolean downloadFile(String url, File outFile, DownloadListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        FileOutputStream fos = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                return false;
            }
            ResponseBody body = response.body();
            if (body == null) {
                return false;
            }
            long toal = body.contentLength();
            long sum = 0;

            InputStream inputStream = body.byteStream();
            fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buffer)) >= 0) {
                fos.write(buffer, 0, count);
                sum += count;
                if (toal > 0) {
                    int progress = (int) ((sum * 1.0) / toal * 100);
                    if (listener != null) {
                        listener.onProgress(progress);
                    }
                }
            }
            fos.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
