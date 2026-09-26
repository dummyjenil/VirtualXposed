curl -fsSL https://dl.google.com/android/cli/latest/linux_x86_64/install.sh | bash
source ~/.bashrc
android init
android sdk install "platforms/android-34" "platforms/android-36" "build-tools/36.0.0" "platform-tools"
