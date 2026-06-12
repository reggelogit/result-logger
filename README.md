# Result Logger – Android alkalmazás

## Mit csinál?
- **[10] gomb** → `10`-et ír a fájlba UTC időbélyeggel
- **[1] gomb** → `1`-et ír a fájlba UTC időbélyeggel
- Fájl helye: `Documents/results.txt` (belső tárhely)
- Formátum: `2025-06-12 14:30:00 | 10`

---

## APK build lépései (Android Studio nélkül)

### Előfeltételek
- Java JDK 17+ telepítve
- Android SDK (Command Line Tools) telepítve
- `ANDROID_HOME` vagy `ANDROID_SDK_ROOT` környezeti változó beállítva

### Gyors build
```bash
cd ResultLogger

# Linux/macOS
chmod +x gradlew
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

Az APK helye:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Telepítés eszközre (USB debugging engedélyezve)
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## Android Studio-val (ajánlott)

1. Nyisd meg Android Studio-t
2. **File → Open** → válaszd ki a `ResultLogger` mappát
3. Várd meg a Gradle sync-et
4. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. Az APK a `app/build/outputs/apk/debug/` mappában lesz

---

## Gradle wrapper letöltés (ha szükséges)

Ha a `gradle/wrapper/gradle-wrapper.jar` hiányzik:
```bash
gradle wrapper --gradle-version 8.4
```
Vagy töltsd le manuálisan:
https://services.gradle.org/distributions/gradle-8.4-bin.zip

---

## Engedélyek (Permissions)

- **Android 9 (API 28) és alatta**: `WRITE_EXTERNAL_STORAGE` kell – az app automatikusan kéri
- **Android 10+ (API 29+)**: Nem kell külön engedély a Documents mappához
- Az app automatikusan kezeli mindkét esetet

---

## Fájl helye az eszközön

```
/sdcard/Documents/results.txt
```
vagy
```
/storage/emulated/0/Documents/results.txt
```

Elérhető bármilyen fájlkezelő alkalmazással (pl. Files by Google).

---

## Minimális követelmények
- Android 7.0 (API 24) vagy újabb
- ~2 MB szabad hely
