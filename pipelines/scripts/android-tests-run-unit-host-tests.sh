#!/usr/bin/env bash
set -euo pipefail

./gradlew \
  :androidApp:testDebugUnitTest \
  :shared:testAndroidHostTest \
  --stacktrace
