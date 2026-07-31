#!/usr/bin/env bash
set -euo pipefail

# Creates exactly one zip with default README/config patterns.
# Output: ./Export for AI/Export-for-AI-YYYY-MM-DD.zip

ROOT_DIR="$(pwd)"
OUT_DIR="$ROOT_DIR/Export for AI"
STAMP="$(date +%F)"
ZIP_NAME="Export-for-AI-${STAMP}.zip"
ZIP_PATH="$OUT_DIR/$ZIP_NAME"
TMP_LIST="$(mktemp)"

mkdir -p "$OUT_DIR"

find "$ROOT_DIR" -type f \
  \( -iname "readme*" \
  -o -iname "*.yaml" \
  -o -iname "*.yml" \
  -o -iname "*.conf" \
  -o -name ".env.example" \) \
  -not -path "*/.git/*" \
  -not -path "*/Export for AI/*" \
  -print > "$TMP_LIST"

if [ ! -s "$TMP_LIST" ]; then
  rm -f "$TMP_LIST"
  echo "No files matched default patterns in $ROOT_DIR"
  exit 1
fi

if [ -f "$ZIP_PATH" ]; then
  rm -f "$ZIP_PATH"
fi

(
  cd "$ROOT_DIR"
  sed "s|^$ROOT_DIR/||" "$TMP_LIST" | zip -q "$ZIP_PATH" -@
)

rm -f "$TMP_LIST"

echo "Created zip:"
echo "$ZIP_PATH"