# Protonika
Protonika is a Kotlin Multiplatform app that recreates the legendary MK-61 programmable calculator on Android and iOS. It combines a shared virtual machine, modern Compose UI, script editing, and file import/export to write, run, and test calculator programs across platforms with one codebase and consistent behavior for hobbyists and retro-computing fans.

## Workflow status

### All tests status
[![Codemagic android-tests status](https://api.codemagic.io/apps/6a5a32456e68dcef2c17842d/android-tests/status_badge.svg)](https://codemagic.io/app/6a5a32456e68dcef2c17842d/android-tests/latest_build)

### Build and deploy status
[![Codemagic android-build-deploy status](https://api.codemagic.io/apps/6a5a32456e68dcef2c17842d/android-build-deploy/status_badge.svg)](https://codemagic.io/app/6a5a32456e68dcef2c17842d/android-build-deploy/latest_build)

## CI/CD

This project uses Codemagic with two separate Android workflows defined in `codemagic.yaml`:

1. `android-tests`
	- Runs on any branch (`push` and `pull_request`).
	- Runs all available unit and host tests.
	- Contains a placeholder stage for future Firebase Test Lab UI tests.
	- If branch is `main` and tests are successful, it triggers the deploy workflow via Codemagic REST API.

2. `android-build-deploy`
	- Intended to run only on `main`.
	- Builds Android debug APK.
	- Builds runnable `neutronika` fat JAR.
	- Distributes APK to Firebase App Distribution.
	- Uploads JAR to Firebase Storage as a separate artifact.

### Why two workflows

Splitting tests from deploy keeps feedback fast on all branches while ensuring deployments are done only from trusted mainline changes.

### Pipeline stage flow

#### Tests workflow (`android-tests`)

1. Environment diagnostics (`java -version`, Gradle version).
2. Unit and host tests across modules.
3. UI test placeholder for future Firebase Test Lab integration.
4. Deploy trigger step for `main` branch only.

#### Build and deploy workflow (`android-build-deploy`)

1. Enforce `main` branch.
2. Generate `release_notes.txt` from latest commit.
3. Build Android debug APK.
4. Deploy Android debug APK to Firebase App Distribution.
5. Build `neutronika` runnable fat JAR.
6. Upload JAR to Firebase Storage.