# Protonika
Protonika is a Kotlin Multiplatform app that recreates the legendary MK-61 programmable calculator on Android and iOS. It combines a shared virtual machine, modern Compose UI, script editing, and file import/export to write, run, and test calculator programs across platforms with one codebase and consistent behavior for hobbyists and retro-computing fans.

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
4. Build `neutronika` runnable fat JAR.
5. Upload JAR to Firebase Storage.
6. Publish APK to Firebase App Distribution.

### Required Codemagic variable groups

Create these variable groups in Codemagic App settings.

#### Group: `codemagic_api_credentials`

- `CM_API_TOKEN` - Codemagic API token used to trigger second workflow.
- `CM_APP_ID` - Codemagic application ID.

#### Group: `firebase_distribution_credentials`

- `FIREBASE_SERVICE_ACCOUNT` - Firebase service account JSON (secret value).
- `FIREBASE_ANDROID_APP_ID` - Firebase Android App ID.
- `FIREBASE_ANDROID_TESTER_GROUP` - Firebase tester group alias.
- `FIREBASE_PROJECT_ID` - Firebase project ID.
- `FIREBASE_STORAGE_BUCKET` - Firebase Storage bucket name.

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
./gradlew :androidApp:testDebugUnitTest :shared:allTests :shared:VirtualMachine:allTests :shared:VirtualMachineCli:test
./gradlew :androidApp:assembleDebug
./gradlew :shared:VirtualMachineCli:fatJar
```

If all commands pass, the same steps should pass in Codemagic with correct secrets.
