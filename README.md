# PDF Viewer

Простий Android-додаток для перегляду PDF-файлів.

## Можливості

- 📄 Перегляд PDF з прокручуванням сторінок
- 🔍 Масштабування: щипок двома пальцями (до 10x) і подвійний тап
- 📤 Поділитися документом з іншими додатками
- 📌 Реєструється як переглядач PDF за замовчуванням
- 🌐 Інтерфейс: англійська та українська (за мовою телефона)
- ☕ Сподобався додаток? [Подякувати на Ko-fi](https://ko-fi.com/river44)

## Сумісність

- **Мінімальна версія:** Android 7.0 (API 24)
- **Цільова версія:** Android 15 (API 35), працює і на Android 16
- Перевірено для Redmi 9A (Android 11)

## Встановлення

Готовий APK — на сторінці [Releases](https://github.com/oonufrienko/android-pdf-viewer/releases/latest),
пряме посилання: [PDF-Viewer.apk](https://github.com/oonufrienko/android-pdf-viewer/releases/latest/download/PDF-Viewer.apk).
Скопіюйте на телефон і відкрийте (дозвольте встановлення з невідомих джерел).

Щоб зробити додаток переглядачем за замовчуванням: відкрийте будь-який PDF →
виберіть **PDF Viewer** → **«Завжди»**.

## Збірка з коду

Потрібні JDK 17 та Android SDK (platform 35, build-tools 34).

```
gradlew.bat assembleDebug
```

APK з'явиться в `app/build/outputs/apk/debug/app-debug.apk`.

## Технології

- Kotlin, AndroidX, Material Components
- [android-pdf-viewer](https://github.com/mhiew/AndroidPdfViewer) (форк barteksc, рендеринг через PDFium)
- Поширення файлів через FileProvider
