#!/usr/bin/env bash
set -euo pipefail

./gradlew \
  :androidApp:testDebugUnitTest \
  :shared:testAndroidHostTest \
  :shared:VirtualMachine:testAndroidHostTest \
  :shared:VirtualMachine:jvmTest \
  :shared:VirtualMachineCli:test \
  --stacktrace
