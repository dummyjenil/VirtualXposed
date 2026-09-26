#!/usr/bin/env bash

# ==============================================================================
# VirtualXposed Deprecation Detector
# Detects all deprecated Java/Android APIs across the codebase using
# Gradle -Xlint:deprecation and static AST pattern analysis.
# ==============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PYTHON_SCRIPT="$SCRIPT_DIR/scripts/detect_deprecated.py"

# Ensure Python 3 is available
if ! command -v python3 &> /dev/null; then
    echo "Error: python3 is required to run the deprecation detector."
    exit 1
fi

chmod +x "$PYTHON_SCRIPT"
python3 "$PYTHON_SCRIPT" "$@"
