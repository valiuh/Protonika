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

# Accept FIREBASE_STORAGE_BUCKET as one of:
# 1) plain bucket name, 2) gs://bucket-name, 3) bucket with optional prefix path.
BUCKET_INPUT="${FIREBASE_STORAGE_BUCKET}"
while [[ "${BUCKET_INPUT}" == gs://* ]]; do
  BUCKET_INPUT="${BUCKET_INPUT#gs://}"
done
while [[ "${BUCKET_INPUT}" == https://storage.googleapis.com/* ]]; do
  BUCKET_INPUT="${BUCKET_INPUT#https://storage.googleapis.com/}"
done
BUCKET_INPUT="${BUCKET_INPUT%/}"

BUCKET_NAME="${BUCKET_INPUT%%/*}"
BUCKET_PREFIX=""
if [[ "${BUCKET_INPUT}" == */* ]]; then
  BUCKET_PREFIX="${BUCKET_INPUT#*/}"
fi

if [ -z "${BUCKET_NAME}" ] || [[ "${BUCKET_NAME}" == *:* ]]; then
  echo "Invalid FIREBASE_STORAGE_BUCKET: '${FIREBASE_STORAGE_BUCKET}'."
  echo "Use a bucket name (example: 'protonika-f5f53.firebasestorage.app') or 'gs://<bucket>'."
  exit 1
fi

DESTINATION_BASE="gs://${BUCKET_NAME}"
if [ -n "${BUCKET_PREFIX}" ]; then
  DESTINATION_BASE="${DESTINATION_BASE}/${BUCKET_PREFIX}"
fi

COMMIT_REF="${CM_COMMIT:-local}"
DESTINATION="${DESTINATION_BASE}/neutronika/${COMMIT_REF}/$(basename "${JAR_PATH}")"
gsutil cp "${JAR_PATH}" "${DESTINATION}"
echo "Uploaded neutronika JAR to ${DESTINATION}"
