package io.virtualapp.delegate;

import android.app.Application;

import com.lody.virtual.client.core.VirtualCore;

/**
 * Standard VirtualApp Initializer for pure Android build.
 */
public class MyVirtualInitializer extends BaseVirtualInitializer {

    public MyVirtualInitializer(Application application, VirtualCore core) {
        super(application, core);
    }
}
