# VirtualXposed Modernization & Deprecation Report

Generated automatically by `detect_deprecated.sh`.

## Summary Statistics

| Metric | Count |
|---|---|
| **Total Deprecated Items** | `126` |
| **High Severity (Classes/Architecture)** | `18` |
| **Medium Severity (Methods/APIs)** | `108` |
| **Low Severity (Flags/Properties)** | `0` |

## Detailed Itemized List

### 📄 [`VirtualApp/app/src/aosp/java/io/virtualapp/delegate/MyCrashHandler.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/aosp/java/io/virtualapp/delegate/MyCrashHandler.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [30](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/aosp/java/io/virtualapp/delegate/MyCrashHandler.java#L30) | 🟡 `MEDIUM` | `MODE_MULTI_PROCESS in Context` | MODE_MULTI_PROCESS in Context has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [51](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/aosp/java/io/virtualapp/delegate/MyCrashHandler.java#L51) | 🟡 `MEDIUM` | `versionCode in PackageInfo` | versionCode in PackageInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/app/src/main/java/io/virtualapp/delegate/MyTaskDescDelegate.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/delegate/MyTaskDescDelegate.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [26](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/delegate/MyTaskDescDelegate.java#L26) | 🟡 `MEDIUM` | `TaskDescription(String,Bitmap,int) in TaskDescription` | TaskDescription(String,Bitmap,int) in TaskDescription has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [26](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/delegate/MyTaskDescDelegate.java#L26) | 🟡 `MEDIUM` | `getIcon() in TaskDescription` | getIcon() in TaskDescription has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/app/src/main/java/io/virtualapp/home/ListAppFragment.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/home/ListAppFragment.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [202](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/home/ListAppFragment.java#L202) | 🟡 `MEDIUM` | `startActivityForResult(Intent,int) in Fragment` | startActivityForResult(Intent,int) in Fragment has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [231](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/home/ListAppFragment.java#L231) | 🟡 `MEDIUM` | `onActivityResult(int,int,Intent) in Fragment` | onActivityResult(int,int,Intent) in Fragment has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [232](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/home/ListAppFragment.java#L232) | 🟡 `MEDIUM` | `onActivityResult(int,int,Intent) in Fragment` | onActivityResult(int,int,Intent) in Fragment has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/app/src/main/java/io/virtualapp/home/adapters/AppPagerAdapter.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/home/adapters/AppPagerAdapter.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [18](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/home/adapters/AppPagerAdapter.java#L18) | 🟡 `MEDIUM` | `FragmentPagerAdapter in androidx.fragment.app` | FragmentPagerAdapter in androidx.fragment.app has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [23](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/home/adapters/AppPagerAdapter.java#L23) | 🟡 `MEDIUM` | `FragmentPagerAdapter(FragmentManager) in FragmentPagerAdapter` | FragmentPagerAdapter(FragmentManager) in FragmentPagerAdapter has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/app/src/main/java/io/virtualapp/settings/SettingsActivity.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/settings/SettingsActivity.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [204](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/settings/SettingsActivity.java#L204) | 🟡 `MEDIUM` | `no in string` | no in string has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/app/src/main/java/io/virtualapp/widgets/Indicator.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/widgets/Indicator.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [53](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/widgets/Indicator.java#L53) | 🟡 `MEDIUM` | `getOpacity() in Drawable` | getOpacity() in Drawable has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/app/src/main/java/io/virtualapp/widgets/LabelView.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/widgets/LabelView.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [304](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/widgets/LabelView.java#L304) | 🟡 `MEDIUM` | `scaledDensity in DisplayMetrics` | scaledDensity in DisplayMetrics has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/app/src/main/java/io/virtualapp/widgets/fittext/FitTextHelper.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/widgets/fittext/FitTextHelper.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [192](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/app/src/main/java/io/virtualapp/widgets/fittext/FitTextHelper.java#L192) | 🟡 `MEDIUM` | `StaticLayout(CharSequence,TextPaint,int,Alignment,float,float,boolean) in StaticLayout` | StaticLayout(CharSequence,TextPaint,int,Alignment,float,float,boolean) in StaticLayout has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [15](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L15) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [19](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L19) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [23](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L23) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [27](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L27) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [94](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L94) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [105](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L105) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [114](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L114) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [121](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L121) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [182](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L182) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [186](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L186) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [191](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L191) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [199](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L199) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [203](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L203) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [210](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L210) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [218](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L218) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [222](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L222) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [228](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L228) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [236](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L236) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [240](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L240) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [245](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L245) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [336](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L336) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [337](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L337) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [338](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L338) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [339](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/build/generated/aidl_source_output_dir/debug/out/android/net/IConnectivityManager.java#L339) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/client/VClientImpl.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/client/VClientImpl.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [512](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/client/VClientImpl.java#L512) | 🟡 `MEDIUM` | `Environment.getExternalStorageDirectory()` | Deprecated in Android 10 (API 29) for Scoped Storage compliance. | **Use context.getExternalFilesDir(null) or Storage Access Framework / MediaStore.** |
| [534](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/client/VClientImpl.java#L534) | 🟡 `MEDIUM` | `Environment.getExternalStorageDirectory()` | Deprecated in Android 10 (API 29) for Scoped Storage compliance. | **Use context.getExternalFilesDir(null) or Storage Access Framework / MediaStore.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/client/hook/proxies/location/GPSListenerThread.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/client/hook/proxies/location/GPSListenerThread.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [21](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/client/hook/proxies/location/GPSListenerThread.java#L21) | 🟡 `MEDIUM` | `new Handler() without Looper` | Handler() constructor is deprecated since API 30 because implicit Looper can cause hidden thread bugs. | **Use new Handler(Looper.getMainLooper()) or explicitly provide target Looper.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/os/VEnvironment.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/os/VEnvironment.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [163](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/os/VEnvironment.java#L163) | 🟡 `MEDIUM` | `Environment.getExternalStorageDirectory()` | Deprecated in Android 10 (API 29) for Scoped Storage compliance. | **Use context.getExternalFilesDir(null) or Storage Access Framework / MediaStore.** |
| [184](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/os/VEnvironment.java#L184) | 🟡 `MEDIUM` | `Environment.getExternalStorageDirectory()` | Deprecated in Android 10 (API 29) for Scoped Storage compliance. | **Use context.getExternalFilesDir(null) or Storage Access Framework / MediaStore.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/remote/AppTaskInfo.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/remote/AppTaskInfo.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [39](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/remote/AppTaskInfo.java#L39) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [40](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/remote/AppTaskInfo.java#L40) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [41](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/remote/AppTaskInfo.java#L41) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/remote/VParceledListSlice.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/remote/VParceledListSlice.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [78](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/remote/VParceledListSlice.java#L78) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [109](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/remote/VParceledListSlice.java#L109) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/RegisteredServicesParser.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/RegisteredServicesParser.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [37](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/RegisteredServicesParser.java#L37) | 🟡 `MEDIUM` | `Resources(AssetManager,DisplayMetrics,Configuration) in Resources` | Resources(AssetManager,DisplayMetrics,Configuration) in Resources has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VAccountManagerService.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VAccountManagerService.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [316](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VAccountManagerService.java#L316) | 🟡 `MEDIUM` | `<T>getParcelable(String) in Bundle` | <T>getParcelable(String) in Bundle has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [862](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VAccountManagerService.java#L862) | 🟡 `MEDIUM` | `get(String) in BaseBundle` | get(String) in BaseBundle has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [881](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VAccountManagerService.java#L881) | 🟡 `MEDIUM` | `LOGIN_ACCOUNTS_CHANGED_ACTION in AccountManager` | LOGIN_ACCOUNTS_CHANGED_ACTION in AccountManager has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [1248](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VAccountManagerService.java#L1248) | 🟡 `MEDIUM` | `<T>getParcelable(String) in Bundle` | <T>getParcelable(String) in Bundle has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VSyncRecord.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VSyncRecord.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [81](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VSyncRecord.java#L81) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [139](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/accounts/VSyncRecord.java#L139) | 🟡 `MEDIUM` | `get(String) in BaseBundle` | get(String) in BaseBundle has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/am/ActivityStack.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/ActivityStack.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [183](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/ActivityStack.java#L183) | 🟡 `MEDIUM` | `getRecentTasks(int,int) in ActivityManager` | getRecentTasks(int,int) in ActivityManager has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [192](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/ActivityStack.java#L192) | 🟡 `MEDIUM` | `id in RecentTaskInfo` | id in RecentTaskInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [413](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/ActivityStack.java#L413) | 🟡 `MEDIUM` | `FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET in Intent` | FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET in Intent has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/am/BroadcastSystem.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/BroadcastSystem.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [198](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/BroadcastSystem.java#L198) | 🟡 `MEDIUM` | `Handler() in Handler` | Handler() in Handler has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [214](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/BroadcastSystem.java#L214) | 🟡 `MEDIUM` | `Handler() in Handler` | Handler() in Handler has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/am/VActivityManagerService.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/VActivityManagerService.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [1060](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/VActivityManagerService.java#L1060) | 🟡 `MEDIUM` | `<T>getParcelableExtra(String) in Intent` | <T>getParcelableExtra(String) in Intent has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [1061](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/am/VActivityManagerService.java#L1061) | 🟡 `MEDIUM` | `<T>getParcelableExtra(String) in Intent` | <T>getParcelableExtra(String) in Intent has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/job/VJobSchedulerService.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/job/VJobSchedulerService.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [153](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/job/VJobSchedulerService.java#L153) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/location/VirtualLocationService.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/location/VirtualLocationService.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [66](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/location/VirtualLocationService.java#L66) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [69](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/location/VirtualLocationService.java#L69) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [111](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/location/VirtualLocationService.java#L111) | 🔴 `HIGH` | `readHashMap(ClassLoader) in Parcel` | readHashMap(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [32](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L32) | 🟡 `MEDIUM` | `icon in Notification` | icon in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [33](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L33) | 🟡 `MEDIUM` | `contentView in Notification` | contentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [35](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L35) | 🟡 `MEDIUM` | `bigContentView in Notification` | bigContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [37](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L37) | 🟡 `MEDIUM` | `icon in Notification` | icon in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [41](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L41) | 🟡 `MEDIUM` | `tickerView in Notification` | tickerView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [43](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L43) | 🟡 `MEDIUM` | `tickerView in Notification` | tickerView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [44](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L44) | 🟡 `MEDIUM` | `tickerView in Notification` | tickerView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [46](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L46) | 🟡 `MEDIUM` | `tickerView in Notification` | tickerView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [47](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L47) | 🟡 `MEDIUM` | `tickerView in Notification` | tickerView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [50](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L50) | 🟡 `MEDIUM` | `contentView in Notification` | contentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [51](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L51) | 🟡 `MEDIUM` | `contentView in Notification` | contentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [52](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L52) | 🟡 `MEDIUM` | `contentView in Notification` | contentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [53](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L53) | 🟡 `MEDIUM` | `contentView in Notification` | contentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [55](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L55) | 🟡 `MEDIUM` | `contentView in Notification` | contentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [56](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L56) | 🟡 `MEDIUM` | `contentView in Notification` | contentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [60](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L60) | 🟡 `MEDIUM` | `bigContentView in Notification` | bigContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [61](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L61) | 🟡 `MEDIUM` | `bigContentView in Notification` | bigContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [62](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L62) | 🟡 `MEDIUM` | `bigContentView in Notification` | bigContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [64](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L64) | 🟡 `MEDIUM` | `bigContentView in Notification` | bigContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [65](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L65) | 🟡 `MEDIUM` | `bigContentView in Notification` | bigContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [70](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L70) | 🟡 `MEDIUM` | `headsUpContentView in Notification` | headsUpContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [71](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/notification/NotificationCompatCompatV14.java#L71) | 🟡 `MEDIUM` | `headsUpContentView in Notification` | headsUpContentView in Notification has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/PackageSetting.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/PackageSetting.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [51](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/PackageSetting.java#L51) | 🔴 `HIGH` | `<T>readSparseArray(ClassLoader) in Parcel` | <T>readSparseArray(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/VPackageManagerService.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/VPackageManagerService.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [655](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/VPackageManagerService.java#L655) | 🟡 `MEDIUM` | `PermissionInfo(PermissionInfo) in PermissionInfo` | PermissionInfo(PermissionInfo) in PermissionInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [673](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/VPackageManagerService.java#L673) | 🟡 `MEDIUM` | `PermissionGroupInfo(PermissionGroupInfo) in PermissionGroupInfo` | PermissionGroupInfo(PermissionGroupInfo) in PermissionGroupInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [685](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/VPackageManagerService.java#L685) | 🟡 `MEDIUM` | `PermissionGroupInfo(PermissionGroupInfo) in PermissionGroupInfo` | PermissionGroupInfo(PermissionGroupInfo) in PermissionGroupInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionInfo.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionInfo.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [95](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionInfo.java#L95) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionParams.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionParams.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [148](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionParams.java#L148) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [151](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionParams.java#L151) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [152](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/installer/SessionParams.java#L152) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [127](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L127) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [351](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L351) | 🟡 `MEDIUM` | `versionCode in PackageInfo` | versionCode in PackageInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [437](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L437) | 🟡 `MEDIUM` | `GET_SIGNATURES in PackageManager` | GET_SIGNATURES in PackageManager has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [443](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L443) | 🟡 `MEDIUM` | `signatures in PackageInfo` | signatures in PackageInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [444](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L444) | 🟡 `MEDIUM` | `signatures in PackageInfo` | signatures in PackageInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [552](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L552) | 🟡 `MEDIUM` | `PermissionInfo(PermissionInfo) in PermissionInfo` | PermissionInfo(PermissionInfo) in PermissionInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [563](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L563) | 🟡 `MEDIUM` | `PermissionGroupInfo(PermissionGroupInfo) in PermissionGroupInfo` | PermissionGroupInfo(PermissionGroupInfo) in PermissionGroupInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [571](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/PackageParserEx.java#L571) | 🟡 `MEDIUM` | `GET_UNINSTALLED_PACKAGES in PackageManager` | GET_UNINSTALLED_PACKAGES in PackageManager has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/VPackage.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/VPackage.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [99](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/VPackage.java#L99) | 🔴 `HIGH` | `<T>readParcelable(ClassLoader) in Parcel` | <T>readParcelable(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [535](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/pm/parser/VPackage.java#L535) | 🟡 `MEDIUM` | `PROCESS_OUTGOING_CALLS in permission` | PROCESS_OUTGOING_CALLS in permission has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/com/lody/virtual/server/vs/VSPersistenceLayer.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/vs/VSPersistenceLayer.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [70](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/com/lody/virtual/server/vs/VSPersistenceLayer.java#L70) | 🔴 `HIGH` | `readHashMap(ClassLoader) in Parcel` | readHashMap(ClassLoader) in Parcel has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [14](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L14) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [15](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L15) | 🟡 `MEDIUM` | `NetworkInfo in android.net` | NetworkInfo in android.net has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [15](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L15) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [17](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L17) | 🟡 `MEDIUM` | `NetworkInfo in android.net` | NetworkInfo in android.net has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [17](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L17) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [19](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L19) | 🟡 `MEDIUM` | `NetworkInfo in android.net` | NetworkInfo in android.net has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [19](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L19) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [22](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L22) | 🟡 `MEDIUM` | `NetworkInfo in android.net` | NetworkInfo in android.net has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [22](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L22) | 🟡 `MEDIUM` | `State in NetworkInfo` | State in NetworkInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [22](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L22) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |
| [23](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L23) | 🟡 `MEDIUM` | `NetworkInfo in android.net` | NetworkInfo in android.net has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [23](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L23) | 🟡 `MEDIUM` | `DetailedState in NetworkInfo` | DetailedState in NetworkInfo has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [23](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/net/NetworkInfo.java#L23) | 🟡 `MEDIUM` | `NetworkInfo / getActiveNetworkInfo()` | NetworkInfo is deprecated since Android 10 (API 29). | **Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities().** |

### 📄 [`VirtualApp/lib/src/main/java/mirror/android/telephony/CellIdentityCdma.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/CellIdentityCdma.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [16](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/CellIdentityCdma.java#L16) | 🟡 `MEDIUM` | `CellIdentityCdma in android.telephony` | CellIdentityCdma in android.telephony has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [17](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/CellIdentityCdma.java#L17) | 🟡 `MEDIUM` | `CellIdentityCdma in android.telephony` | CellIdentityCdma in android.telephony has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/mirror/android/telephony/CellInfoCdma.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/CellInfoCdma.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [17](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/CellInfoCdma.java#L17) | 🟡 `MEDIUM` | `CellInfoCdma in android.telephony` | CellInfoCdma in android.telephony has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [18](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/CellInfoCdma.java#L18) | 🟡 `MEDIUM` | `CellInfoCdma in android.telephony` | CellInfoCdma in android.telephony has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |
| [19](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/CellInfoCdma.java#L19) | 🟡 `MEDIUM` | `CellIdentityCdma in android.telephony` | CellIdentityCdma in android.telephony has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/mirror/android/telephony/NeighboringCellInfo.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/NeighboringCellInfo.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [11](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/telephony/NeighboringCellInfo.java#L11) | 🟡 `MEDIUM` | `NeighboringCellInfo in android.telephony` | NeighboringCellInfo in android.telephony has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

### 📄 [`VirtualApp/lib/src/main/java/mirror/android/view/RenderScript.java`](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/view/RenderScript.java)

| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |
|---|---|---|---|---|
| [10](file:///home/jenil-sheth/Music/VirtualXposed/VirtualApp/lib/src/main/java/mirror/android/view/RenderScript.java#L10) | 🟡 `MEDIUM` | `RenderScript in android.renderscript` | RenderScript in android.renderscript has been deprecated | **Consult updated Android SDK / Java 17 API documentation.** |

## Modernization Strategy & Priority

1. **High Priority**: Migrate `android.preference` to `androidx.preference` and replace `ProgressDialog` with modern Material components.
2. **Medium Priority**: Replace un-themed `getResources().getDrawable()` / `getColor()` with `ContextCompat`.
3. **Low Priority**: Update legacy window flags to modern `WindowInsetsController` / `WindowCompat`.
