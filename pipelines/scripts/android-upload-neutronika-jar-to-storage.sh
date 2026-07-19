#!/usr/bin/env bash
set -euo pipefail

if [ -z "${FIREBASE_SERVICE_ACCOUNT:-}" ] && [ -z "${FIREBASE_SERVICE_ACCOUNT_B64:-}" ]; then
  echo "Either FIREBASE_SERVICE_ACCOUNT (raw JSON) or FIREBASE_SERVICE_ACCOUNT_B64 (base64 JSON) is required."
  exit 1
fi

if [ -z "${FIREBASE_PROJECT_ID:-}" ] || [ -z "${FIREBASE_STORAGE_BUCKET:-}" ]; then
  echo "FIREBASE_PROJECT_ID and FIREBASE_STORAGE_BUCKET are required."
  exit 1
fi

SERVICE_ACCOUNT_PATH="${CM_BUILD_DIR}/firebase-service-account.json"

# Prefer base64 variable if provided to avoid multiline formatting issues in CI UIs.
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

gcloud auth activate-service-account --key-file="${SERVICE_ACCOUNT_PATH}"
gcloud --quiet config set project "${FIREBASE_PROJECT_ID}"

JAR_PATH=$(find shared/VirtualMachineCli/build/libs -maxdepth 1 -type f -name "*-all.jar" | head -n 1)
if [ -z "${JAR_PATH}" ]; then
  echo "No neutronika fat JAR found."
  exit 1
fi

DESTINATION="gs://${FIREBASE_STORAGE_BUCKET}/neutronika/${CM_COMMIT}/$(basename "${JAR_PATH}")"
gsutil cp "${JAR_PATH}" "${DESTINATION}"
echo "Uploaded neutronika JAR to ${DESTINATION}"
