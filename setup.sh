curl -fsSL https://dl.google.com/android/cli/latest/linux_x86_64/install.sh | bash
# Naye PATH ko reload karein
source ~/.bashrc

# CLI environment setup karein
android init
# Required Android platforms aur build tools install karein
android sdk install "platforms/android-34" "platforms/android-36" "build-tools/36.0.0" "platform-tools"
