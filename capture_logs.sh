#!/usr/bin/env bash
# ==============================================================================
# VirtualXposed Critical Log Capture Tool
# Captures ONLY Errors, Warnings, Crashes & Exceptions specifically for VirtualXposed
# Output Directory: latest_logs/
# ==============================================================================

set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_DIR="${SCRIPT_DIR}/latest_logs"
mkdir -p "${LOG_DIR}"

# ANSI Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# Defaults
TARGET_PKG="io.va.exposed64"
MODE="dump"
CLEAR_FIRST=false
LOG_LEVEL="critical" # "critical" (E, W, F, Crash), "error" (E, F, Crash), "all" (V, D, I, W, E, F)

while [[ $# -gt 0 ]]; do
    case "$1" in
        --live|-l)
            MODE="live"
            shift
            ;;
        --clear|-c)
            CLEAR_FIRST=true
            shift
            ;;
        --dump|-d)
            MODE="dump"
            shift
            ;;
        --errors-only|-e)
            LOG_LEVEL="error"
            shift
            ;;
        --all|-a)
            LOG_LEVEL="all"
            shift
            ;;
        --pkg|-p|--package)
            TARGET_PKG="$2"
            shift 2
            ;;
        --help|-h)
            echo -e "${CYAN}${BOLD}Usage:${NC} ./capture_logs.sh [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  -d, --dump          (Default) Snapshot critical logs (Errors & Warnings) to latest_logs/"
            echo "  -l, --live          Stream critical logs live in real-time"
            echo "  -c, --clear         Clear device logcat buffer first"
            echo "  -e, --errors-only   Capture only ERRORS & CRASHES (exclude warnings)"
            echo "  -a, --all           Capture ALL log levels (Debug, Info, Warnings, Errors)"
            echo "  -p, --pkg <name>    Filter specific package (default: io.va.exposed64)"
            echo "  -h, --help          Show this help message"
            echo ""
            echo "Examples:"
            echo "  ./capture_logs.sh                  # Snapshot critical logs of VirtualXposed"
            echo "  ./capture_logs.sh --live           # Live monitor errors & warnings"
            echo "  ./capture_logs.sh -c -l            # Clear buffer & start live stream"
            echo "  ./capture_logs.sh -e               # Snapshot only severe errors & crashes"
            exit 0
            ;;
        *)
            if [[ ! "$1" =~ ^- ]]; then
                TARGET_PKG="$1"
            fi
            shift
            ;;
    esac
done

echo -e "${CYAN}${BOLD}"
echo "===================================================="
echo "   🚨 Critical Log Capture: ${TARGET_PKG}          "
echo "===================================================="
echo -e "${NC}"

# Check for ADB
if ! command -v adb &> /dev/null; then
    echo -e "${RED}❌ Error: 'adb' command not found in PATH.${NC}"
    echo "Please install Android Platform Tools or add adb to your PATH."
    exit 1
fi

# Detect ADB Devices
TARGET_DEVICE=""
DEVICES=($(adb devices | grep -w "device" | awk '{print $1}'))

if [ ${#DEVICES[@]} -eq 0 ]; then
    echo -e "${RED}❌ No connected ADB devices found!${NC}"
    echo "Please connect device via USB or: adb connect <ip>:<port>"
    exit 1
elif [ ${#DEVICES[@]} -eq 1 ]; then
    TARGET_DEVICE="${DEVICES[0]}"
    echo -e "${GREEN}✓ Device detected: ${BOLD}${TARGET_DEVICE}${NC}"
else
    TARGET_DEVICE="${DEVICES[0]}"
    echo -e "${GREEN}✓ Multiple devices found. Using: ${BOLD}${TARGET_DEVICE}${NC}"
fi

# Fetch active PIDs for the target package
get_target_pids() {
    (adb -s "${TARGET_DEVICE}" shell "ps -A 2>/dev/null || ps" 2>/dev/null | grep -F "${TARGET_PKG}" | awk '{print $2}' | tr '\n' ' ') || true
}

PIDS=$(get_target_pids)
if [ -n "$PIDS" ]; then
    echo -e "${BLUE}ℹ️ Active PIDs for ${TARGET_PKG}: ${BOLD}${PIDS}${NC}"
else
    echo -e "${YELLOW}ℹ️ No active running PID found for ${TARGET_PKG} yet (will capture all matching logs & crashes).${NC}"
fi

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
FULL_LOG_FILE="${LOG_DIR}/log_full_${TIMESTAMP}.log"
CRITICAL_LOG_FILE="${LOG_DIR}/log_critical_${TIMESTAMP}.log"
LATEST_FULL="${LOG_DIR}/latest_full.log"
LATEST_LOG="${LOG_DIR}/latest.log"

# Build regex pattern for package, its PIDs, and core VirtualXposed subsystems
build_package_regex() {
    local current_pids
    current_pids=$(get_target_pids)
    local pid_pattern=""
    if [ -n "$current_pids" ]; then
        local pid_list
        pid_list=$(echo "$current_pids" | tr ' ' '\n' | grep -v '^$' | tr '\n' '|' | sed 's/|$//')
        if [ -n "$pid_list" ]; then
            pid_pattern="|(\\([ ]*(${pid_list})\\))|(\\s+(${pid_list})\\s+)"
        fi
    fi
    echo "(${TARGET_PKG}|io\\.virtualapp|io\\.va|LoadingActivity|ActivityStack|StubCP|TransactionHandlerProxy|AppInstrumentation|HCallbackStub|VLog|VirtualCore|VAppManager|VPackageManager|VActivityManager|Installd|DaemonService|XApp|VClientImpl|ExposedBridge|AndroidRuntime${pid_pattern})"
}

# Critical filters (Errors, Warnings, Crashes, Stack traces)
CRITICAL_REGEX="[[:space:]][EWF]/|[[:space:]]E[[:space:]]+|[[:space:]]W[[:space:]]+|[[:space:]]F[[:space:]]+|FATAL|Exception|Error|crash|SIGSEGV|F/DEBUG|Caused by|at [a-zA-Z0-9_\.]+\([a-zA-Z0-9_\.]+\.java:[0-9]+\)|DeadObject|SecurityException|Unmarshalling|StrictMode"
ERROR_ONLY_REGEX="[[:space:]][EF]/|[[:space:]]E[[:space:]]+|[[:space:]]F[[:space:]]+|FATAL|Exception|Error|crash|SIGSEGV|F/DEBUG|Caused by|DeadObject|SecurityException|Unmarshalling"

if [ "$CLEAR_FIRST" = true ]; then
    echo -e "${YELLOW}🧹 Clearing logcat buffer on ${TARGET_DEVICE}...${NC}"
    adb -s "${TARGET_DEVICE}" logcat -c
    echo -e "${GREEN}✓ Logcat cleared.${NC}"
fi

# Python filtering helper for precise multiline error and package matching
filter_stream() {
    local pkg_reg="$1"
    local lvl_mode="$2"
    python3 -u - "$pkg_reg" "$lvl_mode" << 'EOF'
import sys, re

pkg_reg = sys.argv[1]
level_mode = sys.argv[2]

pkg_pattern = re.compile(pkg_reg, re.IGNORECASE)
crit_pattern = re.compile(r'(\s[EWF]/|\sE\s+|\sW\s+|\sF\s+|FATAL|Exception|Error|crash|SIGSEGV|F/DEBUG|Caused by|\bat\s+[a-zA-Z0-9_.]+\([a-zA-Z0-9_.]+\.java:\d+\)|DeadObject|SecurityException|Unmarshalling|StrictMode)', re.IGNORECASE)
err_pattern = re.compile(r'(\s[EF]/|\sE\s+|\sF\s+|FATAL|Exception|Error|crash|SIGSEGV|F/DEBUG|Caused by|DeadObject|SecurityException|Unmarshalling)', re.IGNORECASE)

in_package_trace = False

for line in iter(sys.stdin.readline, ''):
    is_pkg_match = bool(pkg_pattern.search(line))
    is_trace_continuation = bool(re.search(r'^\s*(at\s+|Caused by:|---|Process:|PID:|Flags:|Package:|#\d+\s+pc|\+\+\+)', line))
    
    if is_pkg_match:
        in_package_trace = True
    elif not is_trace_continuation:
        in_package_trace = False

    if is_pkg_match or (in_package_trace and is_trace_continuation):
        if level_mode == 'all':
            sys.stdout.write(line)
            sys.stdout.flush()
        elif level_mode == 'error':
            if err_pattern.search(line) or is_trace_continuation:
                sys.stdout.write(line)
                sys.stdout.flush()
        else: # critical: errors + warnings + crashes
            if crit_pattern.search(line) or is_trace_continuation:
                sys.stdout.write(line)
                sys.stdout.flush()
EOF
}

if [ "$MODE" = "live" ]; then
    echo -e "${BLUE}🔴 Streaming CRITICAL logs for: ${BOLD}${TARGET_PKG}${NC}..."
    echo -e "${CYAN}📄 Writing live output to: ${BOLD}${LATEST_LOG}${NC}"
    echo -e "${YELLOW}(Press Ctrl+C to stop streaming)${NC}\n"

    PKG_REGEX=$(build_package_regex)

    adb -s "${TARGET_DEVICE}" logcat -v time | filter_stream "${PKG_REGEX}" "${LOG_LEVEL}" | tee "${LATEST_LOG}"

else
    echo -e "${BLUE}📥 Capturing snapshot of CRITICAL logs (Errors & Warnings) for ${BOLD}${TARGET_PKG}...${NC}"

    # Dump full logcat
    adb -s "${TARGET_DEVICE}" logcat -d -v time > "${FULL_LOG_FILE}"
    cp "${FULL_LOG_FILE}" "${LATEST_FULL}"

    PKG_REGEX=$(build_package_regex)

    # Filter critical package logs
    filter_stream "${PKG_REGEX}" "${LOG_LEVEL}" < "${FULL_LOG_FILE}" > "${CRITICAL_LOG_FILE}" || true
    cp "${CRITICAL_LOG_FILE}" "${LATEST_LOG}"

    FULL_LINES=$(wc -l < "${FULL_LOG_FILE}")
    CRITICAL_LINES=$(wc -l < "${CRITICAL_LOG_FILE}")

    echo -e "\n${GREEN}====================================================${NC}"
    echo -e "${GREEN}      🚨 Critical Logs Captured for ${TARGET_PKG}! 🎉  ${NC}"
    echo -e "${GREEN}====================================================${NC}"
    echo -e "📦 Target Package : ${BOLD}${TARGET_PKG}${NC}"
    echo -e "📁 Directory      : ${BOLD}${LOG_DIR}${NC}"
    echo -e "📄 Critical Log   : ${BOLD}${LATEST_LOG}${NC} (${CRITICAL_LINES} critical lines)"
    echo -e "📄 Raw Full Log   : ${BOLD}${LATEST_FULL}${NC} (${FULL_LINES} total lines)"
    echo -e "🕒 Timestamp File : ${BOLD}log_critical_${TIMESTAMP}.log${NC}"
    echo ""

    if [ "$CRITICAL_LINES" -gt 0 ]; then
        echo -e "${YELLOW}${BOLD}🔍 Recent Critical Lines Preview:${NC}"
        tail -n 12 "${CRITICAL_LOG_FILE}" | while read -r line; do
            if echo "$line" | grep -qE "FATAL|E/|Exception|DeadObject|Error|crash|SIGSEGV|F/DEBUG|Caused by"; then
                echo -e "  ${RED}${line}${NC}"
            elif echo "$line" | grep -qE "W/|Warning"; then
                echo -e "  ${YELLOW}${line}${NC}"
            else
                echo -e "  ${line}"
            fi
        done
        echo ""
        echo -e "${CYAN}👉 Complete critical logs saved at: ${BOLD}${LATEST_LOG}${NC}"
    else
        echo -e "${GREEN}✓ Clean! No critical errors or warnings recorded for ${TARGET_PKG}.${NC}"
    fi

    echo ""
    echo -e "💡 Useful Commands:"
    echo -e "  - Live monitor critical logs : ${CYAN}./capture_logs.sh --live${NC}"
    echo -e "  - Capture only ERRORS/CRASHES: ${CYAN}./capture_logs.sh -e${NC}"
    echo -e "  - Clear device log buffer    : ${CYAN}./capture_logs.sh -c -d${NC}"
fi
