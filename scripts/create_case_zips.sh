#!/usr/bin/env bash
set -euo pipefail

# Usage: ./scripts/create_case_zips.sh [output_dir]
# Default output_dir: Export_for_AI/per_case_zips

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUTPUT_DIR="${1:-$ROOT_DIR/Export_for_AI/per_case_zips}"

mkdir -p "$OUTPUT_DIR"

# Find top-level case directories (those starting with case- or similar)
# We'll treat any directory in root that contains a README.md or src/ as a case

echo "Root: $ROOT_DIR"
echo "Output: $OUTPUT_DIR"

shopt -s globstar nullglob

for dir in "$ROOT_DIR"/*/; do
  # skip hidden dirs
  base="$(basename "$dir")"
  [[ "$base" == .* ]] && continue

  # determine if directory looks like a case: has README or src
  if [[ -f "$dir/README.md" || -d "$dir/src" || -d "$dir/main" ]]; then
    echo "Packaging case: $base"
    zipname="$OUTPUT_DIR/${base}_source.zip"

    # build include patterns: README*, src/**, pom.xml, build.gradle*, *.kt, *.java, test dirs
    (cd "$ROOT_DIR" && \
      zip -r "$zipname" "$base" -i "$base/README*" "$base/src/**" "$base/**/src/**" "$base/**/src/test/**" "$base/**/pom.xml" "$base/**/build.gradle*" "$base/**/*.kt" "$base/**/*.java" "$base/**/*.kts" "$base/**/test/**" -x "*/.*/*" "*/.git/*" "*/build/*" "*/.gradle/*" "*/node_modules/*" "*/out/*" "*/dist/*" "*/.idea/*") >/dev/null

    echo " Created: $(ls -lh "$zipname")"
  fi
done

echo "All case zips created in $OUTPUT_DIR"
