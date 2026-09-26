#!/bin/bash

# ==============================================================================
# VirtualXposed Local Build & Auto-Install Script
# ==============================================================================

set -e

# ANSI Color Codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${BLUE}====================================================${NC}"
echo -e "${BLUE}    VirtualXposed Local APK Builder & Installer     ${NC}"
echo -e "${BLUE}====================================================${NC}"

# Project Root & VirtualApp Directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VIRTUAL_APP_DIR="$SCRIPT_DIR/VirtualApp"
LOCAL_PROPERTIES="$VIRTUAL_APP_DIR/local.properties"
OUTPUT_DIR="$SCRIPT_DIR/buildOutput"

# 1. Detect / Configure Android SDK
echo -e "\n${YELLOW}[1/5] Checking Android SDK...${NC}"
SDK_PATH=""

if [ -n "$ANDROID_HOME" ] && [ -d "$ANDROID_HOME" ]; then
    SDK_PATH="$ANDROID_HOME"
elif [ -n "$ANDROID_SDK_ROOT" ] && [ -d "$ANDROID_SDK_ROOT" ]; then
    SDK_PATH="$ANDROID_SDK_ROOT"
elif [ -d "$HOME/Android/Sdk" ]; then
    SDK_PATH="$HOME/Android/Sdk"
elif [ -d "$HOME/Library/Android/sdk" ]; then
    SDK_PATH="$HOME/Library/Android/sdk"
elif [ -d "/usr/local/lib/android/sdk" ]; then
    SDK_PATH="/usr/local/lib/android/sdk"
fi

if [ -n "$SDK_PATH" ]; then
    echo -e "${GREEN}✓ Found Android SDK at: $SDK_PATH${NC}"
    echo "sdk.dir=$SDK_PATH" > "$LOCAL_PROPERTIES"
else
    if [ -f "$LOCAL_PROPERTIES" ]; then
        echo -e "${GREEN}✓ Using existing local.properties${NC}"
    else
        echo -e "${RED}✗ Error: Android SDK not found!${NC}"
        echo "Please set ANDROID_HOME or create VirtualApp/local.properties with:"
        echo "sdk.dir=/path/to/your/android/sdk"
        exit 1
    fi
fi

# Locate ADB binary
ADB_BIN="adb"
if ! command -v adb &> /dev/null; then
    if [ -n "$SDK_PATH" ] && [ -f "$SDK_PATH/platform-tools/adb" ]; then
        ADB_BIN="$SDK_PATH/platform-tools/adb"
    fi
fi

# 2. Check Java Version
echo -e "\n${YELLOW}[2/5] Checking Java environment...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Error: Java is not installed or not in PATH!${NC}"
    exit 1
fi
JAVA_VER=$(java -version 2>&1 | head -n 1)
echo -e "${GREEN}✓ $JAVA_VER${NC}"

# 3. Choose Build Type
BUILD_TARGET="${1:-debug}"

echo -e "\n${YELLOW}[3/5] Preparing Gradle build (Target: $BUILD_TARGET)...${NC}"
chmod +x "$VIRTUAL_APP_DIR/gradlew"
cd "$VIRTUAL_APP_DIR"

case "$BUILD_TARGET" in
    debug|install)
        echo -e "${BLUE}Building AOSP Debug APK...${NC}"
        ./gradlew assembleAospDebug --parallel
        ;;
    release)
        echo -e "${BLUE}Building AOSP Release APK...${NC}"
        ./gradlew assembleAospRelease --parallel
        ;;
    clean)
        echo -e "${BLUE}Cleaning build directories...${NC}"
        ./gradlew clean
        echo -e "${GREEN}Clean completed!${NC}"
        exit 0
        ;;
    all|*)
        echo -e "${BLUE}Building both Debug and Release APKs...${NC}"
        ./gradlew assembleAospDebug assembleAospRelease --parallel
        ;;
esac


# 4. Collect and Display Output APKs
echo -e "\n${YELLOW}[4/5] Collecting output APKs...${NC}"
mkdir -p "$OUTPUT_DIR"

FOUND_APKS=$(find "$VIRTUAL_APP_DIR/app/build/outputs/apk" -name "*.apk" 2>/dev/null || true)

INSTALLABLE_APK=""

if [ -n "$FOUND_APKS" ]; then
    echo -e "${GREEN}====================================================${NC}"
    echo -e "${GREEN}            Build Succeeded! 🎉                    ${NC}"
    echo -e "${GREEN}====================================================${NC}"
    echo -e "Generated APKs are saved in: ${BLUE}$OUTPUT_DIR${NC}\n"
    
    for apk in $FOUND_APKS; do
        APK_NAME=$(basename "$apk")
        cp "$apk" "$OUTPUT_DIR/$APK_NAME"
        FILE_SIZE=$(du -h "$OUTPUT_DIR/$APK_NAME" | cut -f1)
        echo -e "  📦 ${GREEN}$APK_NAME${NC} (${YELLOW}$FILE_SIZE${NC})"
        echo -e "     Path: $OUTPUT_DIR/$APK_NAME\n"
        if [[ "$APK_NAME" == *"debug"* ]]; then
            INSTALLABLE_APK="$OUTPUT_DIR/$APK_NAME"
        elif [ -z "$INSTALLABLE_APK" ]; then
            INSTALLABLE_APK="$OUTPUT_DIR/$APK_NAME"
        fi
    done
else
    echo -e "${RED}No APKs were found in output directory.${NC}"
    exit 1
fi

# 5. Automatic ADB Detection & Speedy Installation
echo -e "${YELLOW}[5/5] Checking for connected ADB devices...${NC}"

if command -v "$ADB_BIN" &> /dev/null; then
    DEVICES=$("$ADB_BIN" devices | awk '$2=="device" {print $1}')
    
    if [ -n "$DEVICES" ] && [ -n "$INSTALLABLE_APK" ]; then
        echo -e "${GREEN}✓ Connected ADB device(s) found!${NC}"
        
        for dev in $DEVICES; do
            DEV_MODEL=$("$ADB_BIN" -s "$dev" shell getprop ro.product.model 2>/dev/null | tr -d '\r\n' || echo "Android Device")
            DEV_SDK=$("$ADB_BIN" -s "$dev" shell getprop ro.build.version.sdk 2>/dev/null | tr -d '\r\n' || echo "Unknown")
            echo -e "\n🚀 Installing to ${CYAN}$DEV_MODEL${NC} [Serial: ${BLUE}$dev${NC}, API: ${YELLOW}$DEV_SDK${NC}]..."
            
            # Install with reinstall (-r), allow downgrade (-d), and test flag (-t)
            INSTALL_OUTPUT=$("$ADB_BIN" -s "$dev" install -r -d -t "$INSTALLABLE_APK" 2>&1 || true)
            echo "$INSTALL_OUTPUT"

            if [[ "$INSTALL_OUTPUT" == *"Success"* ]]; then
                echo -e "${GREEN}✓ Successfully installed on $DEV_MODEL!${NC}"
                echo -e "⚡ Launching VirtualXposed..."
                "$ADB_BIN" -s "$dev" shell am start -n io.va.exposed64/io.virtualapp.splash.SplashActivity > /dev/null 2>&1 || \
                "$ADB_BIN" -s "$dev" shell am start -n io.virtualapp/io.virtualapp.splash.SplashActivity > /dev/null 2>&1 || true
                echo -e "${GREEN}✓ App launched on $DEV_MODEL! 🎉${NC}"
            elif [[ "$INSTALL_OUTPUT" == *"INSTALL_FAILED_UPDATE_INCOMPATIBLE"* ]] || [[ "$INSTALL_OUTPUT" == *"signatures do not match"* ]]; then
                echo -e "${YELLOW}⚠ Signature mismatch detected with existing app on device.${NC}"
                echo -e "${CYAN}Uninstalling previous version and performing clean install...${NC}"
                "$ADB_BIN" -s "$dev" uninstall io.va.exposed64 > /dev/null 2>&1 || true
                "$ADB_BIN" -s "$dev" uninstall io.va.exposed > /dev/null 2>&1 || true
                "$ADB_BIN" -s "$dev" uninstall io.virtualapp > /dev/null 2>&1 || true
                
                RETRY_OUTPUT=$("$ADB_BIN" -s "$dev" install -r -d -t "$INSTALLABLE_APK" 2>&1 || true)
                echo "$RETRY_OUTPUT"
                if [[ "$RETRY_OUTPUT" == *"Success"* ]]; then
                    echo -e "${GREEN}✓ Clean install succeeded on $DEV_MODEL!${NC}"
                    echo -e "⚡ Launching VirtualXposed..."
                    "$ADB_BIN" -s "$dev" shell am start -n io.va.exposed64/io.virtualapp.splash.SplashActivity > /dev/null 2>&1 || \
                    "$ADB_BIN" -s "$dev" shell am start -n io.virtualapp/io.virtualapp.splash.SplashActivity > /dev/null 2>&1 || true
                    echo -e "${GREEN}✓ App launched on $DEV_MODEL! 🎉${NC}"
                else
                    echo -e "${RED}✗ Failed to install on $DEV_MODEL.${NC}"
                fi
            else
                echo -e "${RED}✗ Failed to install on $DEV_MODEL.${NC}"
            fi
        done
    else
        echo -e "${YELLOW}ℹ No online ADB devices detected. To install on your device, connect via USB/Wi-Fi and run:${NC}"
        echo -e "  ${CYAN}adb install -r -d -t $INSTALLABLE_APK${NC}"
    fi
else
    echo -e "${YELLOW}ℹ ADB not found. Skipping auto-install.${NC}"
fi

echo -e "\n${GREEN}All done!${NC}\n"
