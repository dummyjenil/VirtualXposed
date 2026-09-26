package com.lody.virtual.client.hook.delegate;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;

import com.lody.virtual.client.VClientImpl;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ActivityFixer;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.client.ipc.ActivityClientRecord;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IUiCallback;

import mirror.android.app.ActivityThread;

/**
 * @author Lody
 */
public final class AppInstrumentation extends InstrumentationDelegate implements IInjector {

    private static final String TAG = AppInstrumentation.class.getSimpleName();

    private static AppInstrumentation gDefault;

    private AppInstrumentation(Instrumentation base) {
        super(base);
    }

    public static AppInstrumentation getDefault() {
        if (gDefault == null) {
            synchronized (AppInstrumentation.class) {
                if (gDefault == null) {
                    gDefault = create();
                }
            }
        }
        return gDefault;
    }

    private static AppInstrumentation create() {
        Instrumentation instrumentation = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
        if (instrumentation instanceof AppInstrumentation) {
            return (AppInstrumentation) instrumentation;
        }
        return new AppInstrumentation(instrumentation);
    }


    @Override
    public void inject() throws Throwable {
        base = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
        ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this);
    }

    @Override
    public boolean isEnvBad() {
        return !(ActivityThread.mInstrumentation.get(VirtualCore.mainThread()) instanceof AppInstrumentation);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        VLog.i(TAG, "AppInstrumentation.callActivityOnCreate: activity=" + (activity != null ? activity.getClass().getName() : "null"));
        if (icicle != null) {
            BundleCompat.clearParcelledData(icicle);
        }
        VirtualCore.get().getComponentDelegate().beforeActivityCreate(activity);
        IBinder token = mirror.android.app.Activity.mToken.get(activity);
        ActivityClientRecord r = VActivityManager.get().getActivityRecord(token);
        if (r != null) {
            r.activity = activity;
        }
        ContextFixer.fixContext(activity);
        ActivityFixer.fixActivity(activity);
        ActivityInfo info = null;
        if (r != null) {
            info = r.info;
        }
        if (info != null) {
            if (info.theme != 0) {
                activity.setTheme(info.theme);
            }
            if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    && info.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                activity.setRequestedOrientation(info.screenOrientation);
            }
        }
        try {
            super.callActivityOnCreate(activity, icicle);
            VLog.i(TAG, "AppInstrumentation.callActivityOnCreate SUCCESS: activity=" + (activity != null ? activity.getClass().getName() : "null"));
        } catch (Throwable e) {
            VLog.e(TAG, "AppInstrumentation.callActivityOnCreate CRASH: activity crashed in onCreate: " + (activity != null ? activity.getClass().getName() : "null"), e);
            // 1. tell ui that we launched(failed)
            Intent intent = activity != null ? activity.getIntent() : null;
            callUiCallback(intent, false);
            // 2. finish ourself to tell AMS that do not try launch us again.
            if (activity != null) {
                activity.finish();
            }
            // 3. rethrow
            throw e;
        }
        VirtualCore.get().getComponentDelegate().afterActivityCreate(activity);
    }

    @Override
    public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
        VLog.i(TAG, "AppInstrumentation.newActivity: clazz=" + (clazz != null ? clazz.getName() : "null") + ", intent=" + intent);
        try {
            Activity act = super.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
            VLog.i(TAG, "AppInstrumentation.newActivity SUCCESS for clazz=" + (clazz != null ? clazz.getName() : "null") + " -> " + act);
            return act;
        } catch (Throwable e) {
            VLog.e(TAG, "AppInstrumentation.newActivity CRASH for clazz=" + (clazz != null ? clazz.getName() : "null"), e);
            // 1. tell ui that we launched(failed)
            callUiCallback(intent, false);
            // 3. rethrow
            throw e;
        }
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        VLog.i(TAG, "AppInstrumentation.newActivity: className=" + className + ", intent=" + intent);
        try {
            Activity act = super.newActivity(cl, className, intent);
            VLog.i(TAG, "AppInstrumentation.newActivity SUCCESS for className=" + className + " -> " + act);
            return act;
        } catch (Throwable e) {
            VLog.e(TAG, "AppInstrumentation.newActivity CRASH for className=" + className, e);
            // 1. tell ui that we launched(failed)
            callUiCallback(intent, false);
            // 3. rethrow
            throw e;
        }
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        if (icicle != null) {
            BundleCompat.clearParcelledData(icicle);
        }
        super.callActivityOnCreate(activity, icicle, persistentState);
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        VLog.i(TAG, "AppInstrumentation.callActivityOnResume: activity=" + (activity != null ? activity.getClass().getName() : "null"));
        VirtualCore.get().getComponentDelegate().beforeActivityResume(activity);
        VActivityManager.get().onActivityResumed(activity);
        super.callActivityOnResume(activity);
        VirtualCore.get().getComponentDelegate().afterActivityResume(activity);
        Intent intent = activity != null ? activity.getIntent() : null;

        callUiCallback(intent, true);
    }


    @Override
    public void callActivityOnDestroy(Activity activity) {
        VLog.i(TAG, "AppInstrumentation.callActivityOnDestroy: activity=" + (activity != null ? activity.getClass().getName() : "null"));
        VirtualCore.get().getComponentDelegate().beforeActivityDestroy(activity);
        super.callActivityOnDestroy(activity);
        VirtualCore.get().getComponentDelegate().afterActivityDestroy(activity);
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        VLog.i(TAG, "AppInstrumentation.callActivityOnPause: activity=" + (activity != null ? activity.getClass().getName() : "null"));
        VirtualCore.get().getComponentDelegate().beforeActivityPause(activity);
        super.callActivityOnPause(activity);
        VirtualCore.get().getComponentDelegate().afterActivityPause(activity);
    }


    @Override
    public void callApplicationOnCreate(Application app) {
        VLog.i(TAG, "AppInstrumentation.callApplicationOnCreate: app=" + (app != null ? app.getClass().getName() : "null"));
        super.callApplicationOnCreate(app);
    }

    @Override
    public boolean onException(Object obj, Throwable e) {
        VLog.e(TAG, "AppInstrumentation.onException: obj=" + obj, e);
        return super.onException(obj, e);
    }

    /**
     * tell the ui that the activity has launched.
     * @param intent
     */
    private void callUiCallback(Intent intent, boolean success) {
        IUiCallback callback = VirtualCore.getUiCallback(intent);
        VLog.i(TAG, "AppInstrumentation.callUiCallback: callback=" + callback + ", success=" + success + ", pkg=" + VClientImpl.get().getCurrentPackage() + ", userId=" + VUserHandle.myUserId());
        if (callback != null) {
            try {
                if (success) {
                    callback.onAppOpened(VClientImpl.get().getCurrentPackage(), VUserHandle.myUserId());
                } else {
                    callback.onOpenFailed(VClientImpl.get().getCurrentPackage(), VUserHandle.myUserId());
                }
            } catch (RemoteException e) {
                VLog.e(TAG, "AppInstrumentation.callUiCallback: RemoteException:", e);
            }
        }
    }
}
