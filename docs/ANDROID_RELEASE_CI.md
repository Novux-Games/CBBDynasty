# Android release AAB — GitHub Actions

**Workflow:** [`.github/workflows/android-bundle-release.yml`](../.github/workflows/android-bundle-release.yml)  
**Output:** Signed **`.aab`** for [Google Play Console](https://play.google.com/console) (not Expo/EAS).

## One-time: create an upload keystore

On any machine with Java (`keytool`):

```bash
keytool -genkeypair -v \
  -keystore upload-keystore.jks \
  -alias upload \
  -keyalg RSA -keysize 2048 -validity 10000
```

Store the keystore and passwords somewhere safe (password manager). **Play App Signing:** you upload with this **upload key** once; Google can manage the app signing key in the console.

## One-time: GitHub repository secrets

In **GitHub → Novux-Games/CBBDynasty → Settings → Secrets and variables → Actions**, add:

| Secret | Value |
|--------|--------|
| `ANDROID_KEYSTORE_BASE64` | Base64 of the keystore file (no newlines) |
| `ANDROID_KEYSTORE_PASSWORD` | Keystore password |
| `ANDROID_KEY_ALIAS` | Alias (e.g. `upload`) |
| `ANDROID_KEY_PASSWORD` | Key password if different from keystore password (optional) |

**Windows (PowerShell)** — encode the file:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("upload-keystore.jks")) | Set-Clipboard
```

**macOS / Linux:**

```bash
base64 -w0 upload-keystore.jks   # Linux
base64 -i upload-keystore.jks    # macOS (paste output as one line)
```

The workflow strips spaces and line breaks from the secret before decoding, but the value must still be valid base64 of the `.jks` / `.keystore` file.

## Run a build

1. **Actions** → **Android release bundle (AAB)** → **Run workflow** (branch `main`),  
   **or** push a tag: `git tag v0.1.0 && git push origin v0.1.0`
2. Open the completed run → **Artifacts**:
   - **app-release-aab** → upload to Play
   - **play-mapping-txt** → `mapping.txt` for **Deobfuscation file** (R8)
   - **play-native-debug-symbols** (if present) → **Native debug symbols**
3. **Play Console** → your app → testing track → **Create new release** → upload the AAB, then attach mapping/symbols on the same release if Play prompts

Release builds use **target/compile SDK 36**, **R8** (`mapping.txt`), and **NDK symbol table** metadata where AGP produces it.

## Local release build (optional)

Requires Android SDK (`sdk.dir` in `android/local.properties`). CI signs via env vars; locally, without those vars, Gradle falls back to the **debug** keystore so `:app:bundleRelease` still runs for smoke checks:

```bash
cd android && ./gradlew :app:bundleRelease
```

For a Play-equivalent local bundle, export the same env vars and point `ANDROID_KEYSTORE_PATH` at your `.jks` file (see `android/app/build.gradle.kts`).

## Related

- Debug APK workflow: [`.github/workflows/android-assemble-debug.yml`](../.github/workflows/android-assemble-debug.yml)
- Store copy: [`PLAY_STORE_LISTING.md`](PLAY_STORE_LISTING.md)
