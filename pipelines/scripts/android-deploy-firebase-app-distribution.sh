#!/usr/bin/env bash
set -euo pipefail

if [ -z "${FIREBASE_SERVICE_ACCOUNT:-}" ] && [ -z "${FIREBASE_SERVICE_ACCOUNT_B64:-}" ]; then
  echo "Either FIREBASE_SERVICE_ACCOUNT (raw JSON) or FIREBASE_SERVICE_ACCOUNT_B64 (base64 JSON) is required."
  exit 1
fi

if [ -z "${FIREBASE_ANDROID_APP_ID:-}" ] || [ -z "${FIREBASE_ANDROID_TESTER_GROUP:-}" ]; then
  echo "FIREBASE_ANDROID_APP_ID and FIREBASE_ANDROID_TESTER_GROUP are required."
  exit 1
fi

if ! command -v firebase >/dev/null 2>&1; then
  echo "Firebase CLI is not available on the build machine."
  exit 1
fi

SERVICE_ACCOUNT_PATH="${CM_BUILD_DIR}/firebase-service-account.json"

if [ -n "${FIREBASE_SERVICE_ACCOUNT_B64:-}" ]; then
  printf '%s' "${FIREBASE_SERVICE_ACCOUNT_B64}" | base64 --decode > "${SERVICE_ACCOUNT_PATH}"
else
  printf '%s' "${FIREBASE_SERVICE_ACCOUNT}" > "${SERVICE_ACCOUNT_PATH}"
fi

if ! jq -e 'type == "object" and has("client_email") and has("private_key")' "${SERVICE_ACCOUNT_PATH}" > /dev/null 2>&1; then
  echo "Firebase credentials are not valid JSON service-account content."
  echo "Checklist:"
  echo "1) FIREBASE_SERVICE_ACCOUNT must contain the full JSON object (not a path, not placeholder text)."
  echo "2) Or set FIREBASE_SERVICE_ACCOUNT_B64 with base64 of the JSON file."
  echo "3) Value must include keys like client_email and private_key."
  exit 1
fi

export GOOGLE_APPLICATION_CREDENTIALS="${SERVICE_ACCOUNT_PATH}"

APK_PATH=$(find androidApp/build/outputs/apk/debug -type f -name "*.apk" | head -n 1)
if [ -z "${APK_PATH}" ]; then
  echo "No debug APK found for Firebase App Distribution."
  exit 1
fi

firebase appdistribution:distribute "${APK_PATH}" \
  --app "${FIREBASE_ANDROID_APP_ID}" \
  --groups "${FIREBASE_ANDROID_TESTER_GROUP}" \
  --release-notes-file release_notes.txt
