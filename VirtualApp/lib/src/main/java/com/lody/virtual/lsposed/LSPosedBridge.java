package com.lody.virtual.lsposed;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexClassLoader;
import top.canyie.pine.Pine;
import top.canyie.pine.PineConfig;
import top.canyie.pine.callback.MethodHook;

/**
 * Modern LSPosed Core Bridge for VirtualApp container.
 * Powered by Pine (LSPosed ART Hook Engine) & HiddenApiBypass.
 */
public class LSPosedBridge {

    private static final String TAG = "LSPosedBridge";
    private static volatile boolean sInitialized = false;

    /**
     * Initialize LSPosed Hooking Engine and Bypass Hidden API Restrictions
     */
    public static synchronized void init(Context context, ClassLoader originClassLoader) {
        if (sInitialized) {
            return;
        }
        VLog.i(TAG, "Initializing LSPosed Core Engine with Pine ART Hooking...");

        // 1. Bypass Hidden API Restrictions on Android 9.0 (P) to Android 15+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                org.lsposed.hiddenapibypass.HiddenApiBypass.addHiddenApiExemptions("");
                VLog.i(TAG, "LSPosed HiddenApiBypass successfully applied.");
            } catch (Throwable t) {
                VLog.w(TAG, "HiddenApiBypass error: " + t.getMessage());
            }
        }

        // 2. Configure and initialize Pine Hook Engine
        try {
            PineConfig.debug = true;
            PineConfig.debuggable = true;
            VLog.i(TAG, "Pine ART Hook engine configured successfully.");
        } catch (Throwable t) {
            VLog.e(TAG, "Error configuring Pine: " + t.getMessage(), t);
        }

        sInitialized = true;
        VLog.i(TAG, "LSPosed Core Bridge initialized successfully!");
    }

    /**
     * Load an installed LSPosed / Xposed Module into the target application's ClassLoader.
     */
    public static void loadModule(String moduleApkPath, String odexDir, String libPath,
                                  ApplicationInfo appInfo, ClassLoader appClassLoader) {
        VLog.i(TAG, "Attempting to load LSPosed module from: " + moduleApkPath);
        File apkFile = new File(moduleApkPath);
        if (!apkFile.exists()) {
            VLog.e(TAG, "Module APK does not exist at: " + moduleApkPath);
            return;
        }

        List<String> entryClasses = extractXposedInitEntries(apkFile);
        if (entryClasses.isEmpty()) {
            VLog.w(TAG, "No assets/xposed_init found in module: " + moduleApkPath);
            return;
        }

        try {
            // Create isolated ClassLoader for the module
            ClassLoader parentCl = LSPosedBridge.class.getClassLoader();
            DexClassLoader moduleClassLoader = new DexClassLoader(
                    moduleApkPath,
                    odexDir,
                    libPath,
                    parentCl
            );

            for (String entryClass : entryClasses) {
                VLog.i(TAG, "Instantiating LSPosed module class: " + entryClass);
                try {
                    Class<?> clazz = Class.forName(entryClass, true, moduleClassLoader);
                    Object moduleInstance = clazz.newInstance();

                    // Try to invoke handleLoadPackage if standard Xposed module
                    try {
                        Method handleLoadPackageMethod = findHandleLoadPackageMethod(clazz);
                        if (handleLoadPackageMethod != null) {
                            Object lpparam = createLoadPackageParam(appInfo, appClassLoader);
                            if (lpparam != null) {
                                handleLoadPackageMethod.invoke(moduleInstance, lpparam);
                                VLog.i(TAG, "Successfully invoked handleLoadPackage for: " + entryClass);
                            }
                        }
                    } catch (Throwable t) {
                        VLog.w(TAG, "handleLoadPackage invocation notice: " + t.getMessage());
                    }

                } catch (Throwable t) {
                    VLog.e(TAG, "Failed to load module class " + entryClass + ": " + t.getMessage(), t);
                }
            }
        } catch (Throwable t) {
            VLog.e(TAG, "Failed to initialize DexClassLoader for module: " + moduleApkPath, t);
        }
    }

    /**
     * Reads entrypoint class names declared in assets/xposed_init
     */
    private static List<String> extractXposedInitEntries(File apkFile) {
        List<String> entries = new ArrayList<>();
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(apkFile);
            ZipEntry entry = zipFile.getEntry("assets/xposed_init");
            if (entry != null) {
                try (InputStream is = zipFile.getInputStream(entry);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            entries.add(line);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            VLog.e(TAG, "Error reading assets/xposed_init: " + t.getMessage());
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (Throwable ignored) {
                }
            }
        }
        return entries;
    }

    private static Method findHandleLoadPackageMethod(Class<?> clazz) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals("handleLoadPackage") && m.getParameterTypes().length == 1) {
                return m;
            }
        }
        return null;
    }

    private static Object createLoadPackageParam(ApplicationInfo appInfo, ClassLoader classLoader) {
        try {
            Class<?> paramClass = Class.forName("de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam");
            Object param = paramClass.newInstance();
            paramClass.getField("packageName").set(param, appInfo.packageName);
            paramClass.getField("processName").set(param, appInfo.processName != null ? appInfo.processName : appInfo.packageName);
            paramClass.getField("classLoader").set(param, classLoader);
            paramClass.getField("appInfo").set(param, appInfo);
            paramClass.getField("isFirstApplication").set(param, true);
            return param;
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static boolean isInitialized() {
        return sInitialized;
    }
}
