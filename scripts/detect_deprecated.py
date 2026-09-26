#!/usr/bin/env python3
"""
VirtualXposed Deprecation Detector & Modernization Scanner
Scans Java files, Gradle builds (-Xlint:deprecation), and Android resources
to identify all deprecated APIs, classes, methods, and patterns with modern replacements.
"""

import os
import sys
import re
import subprocess
from pathlib import Path
from collections import defaultdict

# ANSI Colors
GREEN = '\033[92m'
YELLOW = '\033[93m'
RED = '\033[91m'
BLUE = '\033[94m'
CYAN = '\033[96m'
BOLD = '\033[1m'
RESET = '\033[0m'

# Project directories
WORKSPACE_DIR = Path(__file__).resolve().parent.parent if 'scripts' in str(Path(__file__).resolve().parent) else Path(__file__).resolve().parent
VIRTUAL_APP_DIR = WORKSPACE_DIR / "VirtualApp"
REPORT_FILE = WORKSPACE_DIR / "DEPRECATED_REPORT.md"

# Known Deprecation Patterns & Modern Replacements
STATIC_PATTERNS = [
    {
        "id": "progress_dialog",
        "name": "ProgressDialog",
        "regex": r'\bProgressDialog\b',
        "severity": "HIGH",
        "reason": "ProgressDialog is deprecated since Android 8.0 (API 26) as it blocks user interaction.",
        "replacement": "Use ProgressBar inside layout or custom DialogFragment / MaterialAlertDialogBuilder."
    },
    {
        "id": "legacy_preference",
        "name": "android.preference.*",
        "regex": r'\bandroid\.preference\.(Preference|PreferenceFragment|PreferenceActivity|PreferenceScreen|SwitchPreference|CheckBoxPreference)\b',
        "severity": "HIGH",
        "reason": "Framework android.preference is deprecated since Android 10 (API 29).",
        "replacement": "Migrate to androidx.preference.PreferenceFragmentCompat and androidx.preference.*."
    },
    {
        "id": "get_drawable_legacy",
        "name": "getResources().getDrawable(id)",
        "regex": r'getResources\(\)\.getDrawable\(\s*R\.drawable\.[a-zA-Z0-9_]+\s*\)',
        "severity": "MEDIUM",
        "reason": "getDrawable(int) is deprecated since API 22 because it ignores theme styling.",
        "replacement": "Use ContextCompat.getDrawable(context, id) or ResourcesCompat.getDrawable(res, id, theme)."
    },
    {
        "id": "get_color_legacy",
        "name": "getResources().getColor(id)",
        "regex": r'getResources\(\)\.getColor\(\s*R\.color\.[a-zA-Z0-9_]+\s*\)',
        "severity": "MEDIUM",
        "reason": "getColor(int) is deprecated since API 23 because it ignores theme styling.",
        "replacement": "Use ContextCompat.getColor(context, id) or ResourcesCompat.getColor(res, id, theme)."
    },
    {
        "id": "handler_no_looper",
        "name": "new Handler() without Looper",
        "regex": r'new\s+Handler\(\s*\)',
        "severity": "MEDIUM",
        "reason": "Handler() constructor is deprecated since API 30 because implicit Looper can cause hidden thread bugs.",
        "replacement": "Use new Handler(Looper.getMainLooper()) or explicitly provide target Looper."
    },
    {
        "id": "storage_external_dir",
        "name": "Environment.getExternalStorageDirectory()",
        "regex": r'Environment\.getExternalStorageDirectory\(\)',
        "severity": "MEDIUM",
        "reason": "Deprecated in Android 10 (API 29) for Scoped Storage compliance.",
        "replacement": "Use context.getExternalFilesDir(null) or Storage Access Framework / MediaStore."
    },
    {
        "id": "fullscreen_flag",
        "name": "WindowManager.LayoutParams.FLAG_FULLSCREEN",
        "regex": r'FLAG_FULLSCREEN',
        "severity": "LOW",
        "reason": "FLAG_FULLSCREEN is deprecated since API 30 in favor of WindowInsetsController.",
        "replacement": "Use WindowCompat.getInsetsController(window, view).hide(WindowInsetsCompat.Type.statusBars())."
    },
    {
        "id": "system_overlay_window",
        "name": "WindowManager.LayoutParams.TYPE_PHONE / TYPE_SYSTEM_ALERT",
        "regex": r'TYPE_(PHONE|SYSTEM_ALERT|SYSTEM_OVERLAY)',
        "severity": "MEDIUM",
        "reason": "Deprecated since Android 8.0 (API 26).",
        "replacement": "Use WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY."
    },
    {
        "id": "network_info",
        "name": "NetworkInfo / getActiveNetworkInfo()",
        "regex": r'\b(getActiveNetworkInfo|NetworkInfo)\b',
        "severity": "MEDIUM",
        "reason": "NetworkInfo is deprecated since Android 10 (API 29).",
        "replacement": "Use ConnectivityManager.NetworkCallback or ConnectivityManager.getNetworkCapabilities()."
    }
]

def scan_files_with_patterns():
    """Scans all source files for known deprecated patterns."""
    findings = []
    java_files = list(VIRTUAL_APP_DIR.rglob("*.java"))
    
    for file_path in java_files:
        try:
            with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                lines = f.readlines()
                
            for idx, line in enumerate(lines, 1):
                # Skip comments
                stripped = line.strip()
                if stripped.startswith("//") or stripped.startswith("/*") or stripped.startswith("*"):
                    continue
                    
                for pattern in STATIC_PATTERNS:
                    match = re.search(pattern["regex"], line)
                    if match:
                        findings.append({
                            "type": "Pattern Match",
                            "name": pattern["name"],
                            "file": str(file_path),
                            "rel_file": str(file_path.relative_to(WORKSPACE_DIR)),
                            "line": idx,
                            "code": stripped,
                            "severity": pattern["severity"],
                            "reason": pattern["reason"],
                            "replacement": pattern["replacement"]
                        })
        except Exception as e:
            print(f"{RED}Error reading {file_path}: {e}{RESET}")
            
    return findings

def run_gradle_lint_deprecation():
    """Runs Gradle compilation with -Xlint:deprecation to extract exact compiler deprecation warnings."""
    print(f"\n{YELLOW}[1/2] Running Gradle Javac with -Xlint:deprecation...{RESET}")
    gradlew = VIRTUAL_APP_DIR / "gradlew"
    
    if not gradlew.exists():
        print(f"{RED}gradlew not found at {gradlew}{RESET}")
        return []
        
    cmd = [str(gradlew), "compileAospDebugJavaWithJavac", "compileDebugJavaWithJavac", "--rerun-tasks", "--no-daemon"]
    try:
        proc = subprocess.run(cmd, cwd=str(VIRTUAL_APP_DIR), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True, errors="replace")
        output = proc.stdout
    except Exception as e:
        print(f"{RED}Failed to run Gradle: {e}{RESET}")
        return []
        
    compiler_findings = []
    # Match warnings like: /path/to/File.java:123: warning: [deprecation] methodName() in ClassName has been deprecated
    warning_regex = re.compile(r'^(.*?\.java):(\d+):\s+warning:\s+\[deprecation\]\s+(.*)$', re.MULTILINE)
    
    for match in warning_regex.finditer(output):
        file_path = match.group(1).strip()
        line_no = int(match.group(2).strip())
        message = match.group(3).strip()
        
        try:
            rel_file = str(Path(file_path).relative_to(WORKSPACE_DIR))
        except ValueError:
            rel_file = file_path
            
        compiler_findings.append({
            "type": "Javac Compiler Deprecation",
            "name": message.split(" has been deprecated")[0],
            "file": file_path,
            "rel_file": rel_file,
            "line": line_no,
            "code": "",
            "severity": "HIGH" if "class" in message.lower() or "interface" in message.lower() else "MEDIUM",
            "reason": message,
            "replacement": "Consult updated Android SDK / Java 17 API documentation."
        })
        
    return compiler_findings

def generate_reports(static_findings, compiler_findings):
    """Combines findings, dedupes, prints to terminal, and writes markdown report."""
    print(f"\n{YELLOW}[2/2] Analyzing and consolidating findings...{RESET}\n")
    
    all_findings = []
    seen = set()
    
    for item in compiler_findings + static_findings:
        key = (item["file"], item["line"], item["name"])
        if key not in seen:
            seen.add(key)
            all_findings.append(item)
            
    # Group by file
    by_file = defaultdict(list)
    for item in all_findings:
        by_file[item["rel_file"]].append(item)
        
    # Terminal Display
    total_count = len(all_findings)
    high_count = sum(1 for f in all_findings if f["severity"] == "HIGH")
    med_count = sum(1 for f in all_findings if f["severity"] == "MEDIUM")
    low_count = sum(1 for f in all_findings if f["severity"] == "LOW")
    
    print(f"{BLUE}===================================================={RESET}")
    print(f"{BOLD}{BLUE}       VirtualXposed Deprecation Scan Report       {RESET}")
    print(f"{BLUE}===================================================={RESET}")
    print(f"Total Deprecations Found: {BOLD}{total_count}{RESET} ("
          f"{RED}{high_count} HIGH{RESET}, "
          f"{YELLOW}{med_count} MEDIUM{RESET}, "
          f"{GREEN}{low_count} LOW{RESET})\n")
          
    for file_name, file_items in sorted(by_file.items()):
        print(f"📁 {BOLD}{CYAN}{file_name}{RESET} ({len(file_items)} items):")
        for item in sorted(file_items, key=lambda x: x["line"]):
            sev_color = RED if item["severity"] == "HIGH" else (YELLOW if item["severity"] == "MEDIUM" else GREEN)
            print(f"  • Line {BOLD}{item['line']}{RESET} [{sev_color}{item['severity']}{RESET}] {BOLD}{item['name']}{RESET}")
            if item["reason"]:
                print(f"    Reason: {item['reason']}")
            if item["replacement"]:
                print(f"    💡 Suggestion: {GREEN}{item['replacement']}{RESET}")
        print()

    # Markdown Report Generation
    with open(REPORT_FILE, "w", encoding="utf-8") as md:
        md.write("# VirtualXposed Modernization & Deprecation Report\n\n")
        md.write(f"Generated automatically by `detect_deprecated.sh`.\n\n")
        md.write("## Summary Statistics\n\n")
        md.write(f"| Metric | Count |\n|---|---|\n")
        md.write(f"| **Total Deprecated Items** | `{total_count}` |\n")
        md.write(f"| **High Severity (Classes/Architecture)** | `{high_count}` |\n")
        md.write(f"| **Medium Severity (Methods/APIs)** | `{med_count}` |\n")
        md.write(f"| **Low Severity (Flags/Properties)** | `{low_count}` |\n\n")
        
        md.write("## Detailed Itemized List\n\n")
        
        for file_name, file_items in sorted(by_file.items()):
            md.write(f"### 📄 [`{file_name}`](file://{WORKSPACE_DIR}/{file_name})\n\n")
            md.write("| Line | Severity | Deprecated Item | Problem / Reason | Recommended Modern Replacement |\n")
            md.write("|---|---|---|---|---|\n")
            for item in sorted(file_items, key=lambda x: x["line"]):
                badge = f"🔴 `{item['severity']}`" if item['severity'] == "HIGH" else (f"🟡 `{item['severity']}`" if item['severity'] == "MEDIUM" else f"🟢 `{item['severity']}`")
                name_clean = item['name'].replace('|', '\\|')
                reason_clean = item['reason'].replace('|', '\\|')
                rep_clean = item['replacement'].replace('|', '\\|')
                md.write(f"| [{item['line']}](file://{item['file']}#L{item['line']}) | {badge} | `{name_clean}` | {reason_clean} | **{rep_clean}** |\n")
            md.write("\n")

        md.write("## Modernization Strategy & Priority\n\n")
        md.write("1. **High Priority**: Migrate `android.preference` to `androidx.preference` and replace `ProgressDialog` with modern Material components.\n")
        md.write("2. **Medium Priority**: Replace un-themed `getResources().getDrawable()` / `getColor()` with `ContextCompat`.\n")
        md.write("3. **Low Priority**: Update legacy window flags to modern `WindowInsetsController` / `WindowCompat`.\n")

    print(f"{GREEN}✓ Full report saved to: {BOLD}{REPORT_FILE}{RESET}\n")

if __name__ == "__main__":
    static_results = scan_files_with_patterns()
    compiler_results = run_gradle_lint_deprecation()
    generate_reports(static_results, compiler_results)
