# VirtualXposed

<p align="center">
  <img src="https://raw.githubusercontent.com/tiann/arts/master/vxp_install.gif" alt="VirtualXposed Demo" width="300" />
</p>

<p align="center">
  <strong>Run Xposed Modules without Root, Bootloader Unlock, or Custom ROMs</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform" />
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue.svg" alt="Target SDK" />
  <img src="https://img.shields.io/badge/Java-17-orange.svg" alt="Java 17" />
  <img src="https://img.shields.io/badge/Gradle-8.12-brightgreen.svg" alt="Gradle" />
  <img src="https://img.shields.io/badge/License-GPL--3.0-lightgrey.svg" alt="License" />
</p>

---

## 📌 Overview

**VirtualXposed** is a virtualization environment based on **VirtualApp** and runtime dynamic hooking frameworks (**Epic / SandHook / Exposed**) that allows running Xposed modules without requiring:
- ❌ Device Root access
- ❌ Unlocked Bootloader
- ❌ Flashing custom system recovery / images (e.g., Magisk / TWRP)

By creating an isolated virtual runtime process inside user-space, applications and Xposed modules are loaded together inside the sandboxed container, hooking APIs directly in runtime memory.

---

## ✨ Features

- **No Root Required**: Safely run modules on standard, stock Android devices without voiding warranty.
- **LSPosed & Modern HiddenApiBypass**: Integrated `org.lsposed.hiddenapibypass` for seamless hidden API unsealing across modern Android 9 to Android 14+.
- **Sandboxed Isolation**: Run separate isolated instances of apps alongside your host device apps.
- **Package Visibility & Android 14 Support**: Full `QUERY_ALL_PACKAGES` and `<queries>` integration for LSPosed Manager, EdXposed, and Xposed installers.
- **Hooking Engine**: Integrates ART runtime method hooking engines (`epic`, `free_reflection`, and `exposed`).
- **Built-in Xposed Management**: Activate, deactivate, and manage your favorite modules effortlessly.
- **64-bit & Multi-ABI Architecture**: Supports `arm64-v8a`, `armeabi-v7a`, and `x86_64`.
- **Modern Android Toolchain**: Upgraded to target modern Android SDKs (SDK 36), AGP 8.12, Java 17, and AndroidX libraries.


---

## ⚠️ Important Limitations

1. **System-Level Modifications**: Because VirtualXposed operates entirely in application user-space without root, modules that modify core Android OS components (e.g., SystemUI status bar tweaks, kernel patches) **cannot** work.
2. **Resource Hooks**: Standard Java/ART method hooking is supported; direct resource hooking is limited.
3. **Non-Commercial Use**: Commercial distribution and closed-source monetization are strictly restricted under the project license.

---

## 🚀 Getting Started

### 1. Installation

Download the latest APK from the [Releases](https://github.com/android-hacker/VirtualXposed/releases) page and install it on your device.

### 2. Adding Apps & Xposed Modules

Open VirtualXposed and add target applications and Xposed modules into the virtual container:

1. **Clone installed apps**: Select apps already installed on your device to import into the sandbox.
2. **Install from APK**: Select `.apk` files located on internal storage / SD card.
3. **System File Chooser**: Pick external APK files directly using the built-in file chooser.

> [!NOTE]
> **Crucial Rule:** Both the target app (e.g., YouTube) and its corresponding module (e.g., YouTube AdAway) **must be installed inside VirtualXposed**. Installing one on the host OS and one inside VirtualXposed will not work.

### 3. Activating Modules

1. Open **Xposed Installer** inside VirtualXposed.
2. Navigate to the **Modules** section.
3. Enable the checkbox for your installed module.

### 4. Rebooting the Virtual Environment

- You **do not** need to restart your physical phone.
- Go to **Settings** within VirtualXposed and click **Reboot**. The virtual environment will restart instantly with modules applied.

---

## 🛠️ Building from Source

### Prerequisites

- **JDK**: Java Development Kit (JDK 17 or higher recommended)
- **Android SDK**: Build-Tools `36.0.0`, SDK Platforms `36`
- **Android NDK**: NDK installed for compiling native C/C++ libraries
- **Gradle**: Gradle 8.12+ (managed automatically by `./gradlew`)

### Build Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/dummyjenil/VirtualXposed.git
   cd VirtualXposed/VirtualApp
   ```

2. **Configure SDK Path:**
   Create a `local.properties` file in `VirtualApp/` pointing to your Android SDK:
   ```properties
   sdk.dir=/path/to/your/android-sdk
   ```

3. **Build the APK:**
   ```bash
   # Build debug APK
   ./gradlew assembleAospDebug

   # Build release APK
   ./gradlew assembleAospRelease
   ```

---

## 📂 Project Architecture

```
VirtualXposed/
├── VirtualApp/
│   ├── app/                # Main VirtualXposed UI, installer, home launcher & settings
│   │   └── src/main/java/io/virtualapp/
│   ├── lib/                # Core VirtualApp engine, hooking bridges, process virtualization
│   │   ├── src/main/java/com/lody/virtual/
│   │   └── src/main/jni/   # Native ART & runtime hooking implementation
│   ├── gradle/wrapper/     # Gradle wrapper configuration
│   ├── build.gradle        # Top-level build configuration
│   └── settings.gradle     # Module declarations
└── README.md               # Project documentation
```

---

## 🔒 Security & Antivirus False Positives

Due to the nature of process injection, virtualization, dynamic hooking, and stub loading required for Xposed emulation without root, some automated antivirus scanners (such as VirusTotal) may flag VirtualXposed as suspicious or riskware.

VirtualXposed is 100% open-source, contains no malicious payload, and source code is fully inspectable.

---

## 🤝 Credits & Acknowledgements

- **[VirtualApp](https://github.com/asLody/VirtualApp)** – Core Android sandbox & virtualization engine
- **[Epic](https://github.com/tiann/epic)** – ART runtime dynamic method hooking
- **[XposedBridge](https://github.com/rovo89/Xposed)** – Original Xposed framework API
- **[And64InlineHook](https://github.com/Rprop/And64InlineHook)** – ARM64 inline hooking

---

## 📄 License

This project is licensed under the GPL-3.0 License. See the [LICENSE](LICENSE.txt) file for details.

