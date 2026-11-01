# Progetto: Hey Claudio / Dicio per Claude

Salvo (lavora solo da telefono Oppo Reno 12 FS, Android, nessun computer) vuole attivare Claude come **assistente vocale** dicendo **"Hey Claudio"**, senza toccare il telefono. Rispondi sempre in italiano, con istruzioni pensate per il telefono.

## Cosa c'è nel repo
- `hey_claudio.tflite` — modello openWakeWord addestrato per "Hey Claudio" (input [1,16,96] float32 → output [1,1]); soglia già spostata così che lo 0,8 fisso di Dicio corrisponda a 0,7 del modello grezzo.
- `hey_claudio.onnx` — stesso modello, uscita grezza (soglia consigliata 0,7).
- `apk/dicio-per-claude-v3.apk` — versione modificata di Dicio (package `org.stypox.dicio.claude`, versionCode 21, firmata con una chiave personale non presente nel repo). Quando sente "Hey Claudio" apre Claude (`com.anthropic.claude`) provando ACTION_ASSIST → ACTION_VOICE_COMMAND → avvio normale.
- `dicio-per-claude/dicio-per-claude.patch` — tutte le modifiche a Dicio (base: commit in `dicio-per-claude/BASE_COMMIT` di https://github.com/Stypox/dicio-android); il modello va copiato in `app/src/main/assets/hey_claudio.tflite`.
- `train_wakeword.ipynb` — notebook Colab di addestramento (su Colab è fragile: Python 3.13, datasets 4.x, scipy recente).

## Stato (2 nov 2026)
- La parola viene riconosciuta sul telefono, ma Android/ColorOS blocca l'apertura di Claude da background: compare solo una notifica.
- Una versione con servizio di Accessibilità dava "App non installata" su ColorOS: tolta.
- v3 usa il permesso "Mostra sopra altre app" (SYSTEM_ALERT_WINDOW); da verificare sul telefono se apre Claude e se lo apre già in modalità vocale (Claude impostato come assistente digitale predefinito).
- Dopo l'apertura di Claude il microfono di Dicio si ferma 20 s per lasciarlo a Claude.

## Come ricompilare l'APK
Android SDK 36, JDK 21 + JDK 17 (toolchain richiesta da un modulo), Gradle wrapper di Dicio, `./gradlew assembleRelease`, poi zipalign + apksigner. Alzare sempre `versionCode` a ogni nuova versione. Se la chiave originale non è disponibile, Salvo deve disinstallare l'app prima di installare una build firmata con una chiave nuova.
