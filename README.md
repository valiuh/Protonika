# Protonika
Protonika is a Kotlin Multiplatform app that recreates the legendary MK-61 programmable calculator on Android and iOS. It combines a shared virtual machine, modern Compose UI, script editing, and file import/export to write, run, and test calculator programs across platforms with one codebase and consistent behavior for hobbyists and retro-computing fans.

## Workflow status

### Run tests status
[![Codemagic android-tests status](https://api.codemagic.io/apps/6a5a32456e68dcef2c17842d/android-tests/status_badge.svg)](https://codemagic.io/app/6a5a32456e68dcef2c17842d/android-tests/latest_build)

### Run build and deploy status
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

### Required Codemagic variable groups

Create these variable groups in Codemagic App settings.

#### Group: `codemagic_api_credentials`

- `CM_API_TOKEN` - Codemagic API token used to trigger second workflow.
- `CM_APP_ID` - Codemagic application ID.

#### Group: `firebase_distribution_credentials`

- `FIREBASE_SERVICE_ACCOUNT` - Firebase service account JSON (secret value).
- `FIREBASE_SERVICE_ACCOUNT_B64` - Optional base64-encoded Firebase service account JSON (secret value). Recommended if multiline JSON pasting is problematic in CI UI.
- `FIREBASE_ANDROID_APP_ID` - Firebase Android App ID.
- `FIREBASE_ANDROID_TESTER_GROUP` - Firebase tester group alias.
- `FIREBASE_PROJECT_ID` - Firebase project ID.
- `FIREBASE_STORAGE_BUCKET` - Firebase Storage bucket name.

`FIREBASE_SERVICE_ACCOUNT` must be the full JSON content itself, for example starting with `{` and containing keys such as `client_email` and `private_key`. Do not use a file path and do not use placeholder text.

If you prefer base64 (safer for CI text fields):

```bash
base64 -i path/to/firebase-service-account.json | tr -d '\n'
```

Copy that output into `FIREBASE_SERVICE_ACCOUNT_B64`.

### Secret handling

- Real credentials must never be committed.
- Use Codemagic secret variables for CI.
- Use `.codemagic.secrets.env.example` as a local template only.
- Keep local secret file `.codemagic.secrets.env` untracked (already in `.gitignore`).

### Firebase setup checklist (first time)

1. Create/confirm Firebase project and Android app (`com.valiukh.protonika`).
2. Enable Firebase App Distribution and create tester group alias.
3. Create service account with permissions for:
	- Firebase App Distribution Admin
	- Storage Object Admin (or a narrower role sufficient for upload)
4. Add service account JSON as `FIREBASE_SERVICE_ACCOUNT` in Codemagic.
5. Add remaining Firebase variables listed above.

### Notes about JAR distribution

Firebase App Distribution only supports mobile app binaries (`APK`, `AAB`, `IPA`).
Therefore, `neutronika` JAR is distributed via Firebase Storage instead.

### Local verification before pushing

Run these commands locally:

```bash
./gradlew :androidApp:testDebugUnitTest :shared:testAndroidHostTest :shared:VirtualMachine:testAndroidHostTest :shared:VirtualMachine:jvmTest :shared:VirtualMachineCli:test
./gradlew :androidApp:assembleDebug
./gradlew :shared:VirtualMachineCli:fatJar
```

If all commands pass, the same steps should pass in Codemagic with correct secrets.
