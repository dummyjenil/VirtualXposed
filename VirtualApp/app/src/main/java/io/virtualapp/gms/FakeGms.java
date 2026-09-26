package io.virtualapp.gms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstallResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.virtualapp.R;

/**
 * Modern MicroG / Google Play Services Installer for VirtualXposed
 * Downloads and installs official microG components directly into the VirtualCore container.
 */
public class FakeGms {

    private static final String TAG = "FakeGms";

    public static final String GMS_PKG = "com.google.android.gms";
    public static final String GSF_PKG = "com.google.android.gsf";
    public static final String STORE_PKG = "com.android.vending";
    public static final String PLAY_STORE_PKG = "com.aurora.store";

    // Official Verified MicroG & Google Play Store Release Endpoints
    private static final String GSF_URL = "https://github.com/microg/GsfProxy/releases/download/v0.1.0/GsfProxy.apk";
    private static final String STORE_URL = "https://github.com/microg/GmsCore/releases/download/v0.3.16.252432/com.android.vending-84022632.apk";
    private static final String GMS_URL = "https://github.com/microg/GmsCore/releases/download/v0.3.16.252432/com.google.android.gms-252432032.apk";
    private static final String PLAY_STORE_URL = "https://f-droid.org/repo/com.aurora.store_76.apk";

    private static final ExecutorService sExecutor = Executors.newSingleThreadExecutor();
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    public interface OnGmsOperationListener {
        void onSuccess();
        void onProgress(String message, int progress);
        void onError(String errorMessage);
    }

    public static boolean isAlreadyInstalled(Context context) {
        return VirtualCore.get().isAppInstalled(GMS_PKG);
    }

    public static boolean isPlayStoreInstalled(Context context) {
        return VirtualCore.get().isAppInstalled(PLAY_STORE_PKG);
    }

    public static void uninstallPlayStore(Activity activity, Runnable onFinished) {
        if (activity == null) return;
        new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                .setTitle("Uninstall Google Play Store")
                .setMessage("Are you sure you want to remove Google Play Store from VirtualXposed?")
                .setPositiveButton("Uninstall", (dialog, which) -> {
                    ProgressDialog pd = new ProgressDialog(activity);
                    pd.setMessage("Removing Play Store...");
                    pd.setCancelable(false);
                    pd.show();

                    sExecutor.submit(() -> {
                        try {
                            VirtualCore.get().uninstallPackage(PLAY_STORE_PKG);
                        } catch (Throwable t) {
                            Log.e(TAG, "Error uninstalling PlayStore: " + t.getMessage());
                        }
                        sHandler.post(() -> {
                            if (pd.isShowing()) pd.dismiss();
                            if (onFinished != null) onFinished.run();
                            new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                    .setTitle("Play Store")
                                    .setMessage("Google Play Store uninstalled.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        });
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void installPlayStore(Activity activity, Runnable onFinished) {
        if (activity == null) return;
        new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                .setTitle("Install Google Play Store")
                .setMessage("Download and install the official Play Store client (Aurora Store) into VirtualXposed?\n\nThis lets you browse, search, and install any Google Play Store apps directly inside your virtual space.")
                .setPositiveButton("Download & Install", (dialog, which) -> {
                    ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Installing Google Play Store");
                    progressDialog.setMessage("Downloading Play Store (9.3 MB)...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(100);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    sExecutor.submit(() -> {
                        File cacheDir = activity.getCacheDir();
                        File playStoreFile = new File(cacheDir, "playstore.apk");
                        boolean ok = downloadWithRedirects(PLAY_STORE_URL, playStoreFile, percent -> {
                            sHandler.post(() -> {
                                if (progressDialog.isShowing()) {
                                    progressDialog.setProgress(percent);
                                    progressDialog.setMessage("Downloading Play Store: " + percent + "%");
                                }
                            });
                        });

                        if (!ok) {
                            sHandler.post(() -> {
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                        .setTitle("Download Failed")
                                        .setMessage("Unable to download Google Play Store. Please check your internet connection.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            });
                            return;
                        }

                        sHandler.post(() -> {
                            if (progressDialog.isShowing()) progressDialog.setMessage("Installing Play Store into container...");
                        });

                        InstallResult res = VirtualCore.get().installPackage(playStoreFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
                        playStoreFile.delete();

                        sHandler.post(() -> {
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                            if (res.isSuccess) {
                                if (onFinished != null) onFinished.run();
                                new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                        .setTitle("Success")
                                        .setMessage("Google Play Store installed successfully!\n\nYou can open Play Store from the home screen to download and manage apps.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            } else {
                                new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                        .setTitle("Installation Failed")
                                        .setMessage("Failed to install Play Store: " + res.error)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            }
                        });
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void uninstallGms(Activity activity, Runnable onFinished) {
        if (activity == null) {
            return;
        }

        new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                .setTitle("Uninstall Google Services")
                .setMessage("Are you sure you want to remove MicroG / Google Play Services from the container?")
                .setPositiveButton("Uninstall", (dialog, which) -> {
                    ProgressDialog pd = new ProgressDialog(activity);
                    pd.setMessage("Removing Google Services...");
                    pd.setCancelable(false);
                    pd.show();

                    sExecutor.submit(() -> {
                        try {
                            VirtualCore.get().uninstallPackage(GMS_PKG);
                            VirtualCore.get().uninstallPackage(GSF_PKG);
                            VirtualCore.get().uninstallPackage(STORE_PKG);
                        } catch (Throwable t) {
                            Log.e(TAG, "Error uninstalling GMS: " + t.getMessage());
                        }

                        sHandler.post(() -> {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            if (onFinished != null) {
                                onFinished.run();
                            }
                            new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                    .setTitle("Google Services")
                                    .setMessage("Google Services uninstalled successfully.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        });
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void installGms(Activity activity, Runnable onFinished) {
        if (activity == null) {
            return;
        }

        new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                .setTitle("Install Google Services (MicroG)")
                .setMessage("Download and install official MicroG Services, GSF Proxy, and Play Store companion into VirtualXposed?")
                .setPositiveButton("Download & Install", (dialog, which) -> {
                    ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Installing MicroG Services");
                    progressDialog.setMessage("Initializing download...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(100);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    sExecutor.submit(() -> {
                        installInternal(activity, new OnGmsOperationListener() {
                            @Override
                            public void onSuccess() {
                                sHandler.post(() -> {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    if (onFinished != null) {
                                        onFinished.run();
                                    }
                                    new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                            .setTitle("Success")
                                            .setMessage("MicroG / Google Play Services installed successfully!\n\nYou can now run Google Play Services dependent apps in VirtualXposed.")
                                            .setPositiveButton(android.R.string.ok, null)
                                            .show();
                                });
                            }

                            @Override
                            public void onProgress(String message, int progress) {
                                sHandler.post(() -> {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.setMessage(message);
                                        if (progress >= 0) {
                                            progressDialog.setProgress(progress);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(String errorMessage) {
                                sHandler.post(() -> {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    new AlertDialog.Builder(activity, R.style.VAAlertTheme)
                                            .setTitle("Installation Failed")
                                            .setMessage(errorMessage)
                                            .setPositiveButton(android.R.string.ok, null)
                                            .show();
                                });
                            }
                        });
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private static void installInternal(Context context, OnGmsOperationListener listener) {
        File cacheDir = context.getCacheDir();

        // 1. Download GsfProxy (~21 KB)
        listener.onProgress("Downloading GSF Proxy (1/3)...", 0);
        File gsfFile = new File(cacheDir, "gsf.apk");
        if (!downloadWithRedirects(GSF_URL, gsfFile, (p) -> listener.onProgress("Downloading GSF Proxy (1/3)... " + p + "%", p / 3))) {
            listener.onError("Failed to download GSF Proxy. Please check your internet connection.");
            return;
        }

        // 2. Download Play Store / Companion (~4.2 MB)
        listener.onProgress("Downloading Play Store Companion (2/3)...", 33);
        File storeFile = new File(cacheDir, "vending.apk");
        if (!downloadWithRedirects(STORE_URL, storeFile, (p) -> listener.onProgress("Downloading Play Store Companion (2/3)... " + p + "%", 33 + (p / 3)))) {
            listener.onError("Failed to download Play Store Companion. Please check your internet connection.");
            return;
        }

        // 3. Download GmsCore (~108 MB)
        listener.onProgress("Downloading MicroG GmsCore (3/3)...", 66);
        File gmsFile = new File(cacheDir, "gms.apk");
        if (!downloadWithRedirects(GMS_URL, gmsFile, (p) -> listener.onProgress("Downloading MicroG GmsCore (3/3)... " + p + "%", 66 + (p / 3)))) {
            listener.onError("Failed to download MicroG GmsCore. Please check your internet connection.");
            return;
        }

        // 4. Install into VirtualCore
        listener.onProgress("Installing GSF Proxy into container...", 100);
        InstallResult gsfResult = VirtualCore.get().installPackage(gsfFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
        gsfFile.delete();
        if (!gsfResult.isSuccess) {
            listener.onError("Failed to install GSF Proxy: " + gsfResult.error);
            return;
        }

        listener.onProgress("Installing Store Companion into container...", 100);
        InstallResult storeResult = VirtualCore.get().installPackage(storeFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
        storeFile.delete();
        if (!storeResult.isSuccess) {
            listener.onError("Failed to install Store Companion: " + storeResult.error);
            return;
        }

        listener.onProgress("Installing MicroG GmsCore into container...", 100);
        InstallResult gmsResult = VirtualCore.get().installPackage(gmsFile.getAbsolutePath(), InstallStrategy.UPDATE_IF_EXIST);
        gmsFile.delete();
        if (!gmsResult.isSuccess) {
            listener.onError("Failed to install MicroG GmsCore: " + gmsResult.error);
            return;
        }

        listener.onSuccess();
    }

    private interface DownloadProgressListener {
        void onProgress(int percent);
    }

    /**
     * Download a file handling standard HTTP 301/302/307 redirects (e.g. GitHub Releases -> AWS S3)
     */
    private static boolean downloadWithRedirects(String initialUrl, File targetFile, DownloadProgressListener progressListener) {
        String currentUrl = initialUrl;
        int redirects = 0;
        final int MAX_REDIRECTS = 8;

        while (redirects < MAX_REDIRECTS) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(currentUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setInstanceFollowRedirects(true);
                conn.setConnectTimeout(25000);
                conn.setReadTimeout(30000);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; VirtualXposed)");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                        responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                        responseCode == HttpURLConnection.HTTP_SEE_OTHER ||
                        responseCode == 307 || responseCode == 308) {
                    String newUrl = conn.getHeaderField("Location");
                    if (newUrl == null) {
                        return false;
                    }
                    currentUrl = newUrl;
                    redirects++;
                    conn.disconnect();
                    continue;
                }

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    long contentLength = conn.getContentLength();
                    try (InputStream is = conn.getInputStream();
                         FileOutputStream fos = new FileOutputStream(targetFile)) {
                        byte[] buffer = new byte[8192];
                        long totalRead = 0;
                        int bytesRead;
                        int lastReported = -1;

                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                            totalRead += bytesRead;
                            if (contentLength > 0 && progressListener != null) {
                                int percent = (int) ((totalRead * 100) / contentLength);
                                if (percent != lastReported) {
                                    lastReported = percent;
                                    progressListener.onProgress(percent);
                                }
                            }
                        }
                        fos.flush();
                        return true;
                    }
                } else {
                    Log.e(TAG, "Download failed with response code: " + responseCode + " for URL: " + currentUrl);
                    return false;
                }
            } catch (Throwable t) {
                Log.e(TAG, "Download exception for URL: " + currentUrl + " : " + t.getMessage(), t);
                return false;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return false;
    }
}

