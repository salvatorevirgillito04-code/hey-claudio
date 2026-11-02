# Progetto: Hey Claudio / Dicio per Claude

Salvo (lavora solo da telefono Oppo Reno 12 FS, Android, nessun computer) vuole attivare Claude come **assistente vocale** dicendo **"Hey Claudio"**, senza toccare il telefono. Rispondi sempre in italiano, con istruzioni pensate per il telefono.

## Cosa c'è nel repo
- `hey_claudio.tflite` — modello openWakeWord addestrato per "Hey Claudio" (input [1,16,96] float32 → output [1,1]); soglia già spostata così che lo 0,8 fisso di Dicio corrisponda a 0,7 del modello grezzo.
- `hey_claudio.onnx` — stesso modello, uscita grezza (soglia consigliata 0,7).
- `apk/dicio-per-claude-v7.apk` — versione modificata di Dicio (package `org.stypox.dicio.claude`, versionCode 25, firmata con una chiave nuova, diversa da quella della v3; la chiave è stata data a Salvo come file e NON è nel repo, che è pubblico). Quando sente "Hey Claudio", se Claude è l'assistente predefinito, chiama `SearchManager.launchAssist()` (API nascosta, chiamata con la libreria HiddenApiBypass di LSPosed; il servizio di sistema non chiede permessi) per aprire il pannello vocale di Claude come la pressione lunga del tasto di accensione, anche sopra la schermata di blocco (v6). Se fallisce, avvia `ClaudeTrampolineActivity` (invisibile, si mostra sopra la schermata di blocco e accende lo schermo), che chiede lo sblocco se il telefono è bloccato e poi apre Claude (`com.anthropic.claude`) provando ACTION_ASSIST → ACTION_VOICE_COMMAND → avvio normale.
- `dicio-per-claude/dicio-per-claude.patch` — tutte le modifiche a Dicio (base: commit in `dicio-per-claude/BASE_COMMIT` di https://github.com/Stypox/dicio-android); si applica con `git apply`; il modello va copiato in `app/src/main/assets/hey_claudio.tflite` (cartella da creare).
- `train_wakeword.ipynb` — notebook Colab di addestramento (su Colab è fragile: Python 3.13, datasets 4.x, scipy recente).

## Stato (2 nov 2026)
- La parola viene riconosciuta sul telefono, ma con la v3 Android/ColorOS bloccava l'apertura di Claude da background (solo notifica).
- Una versione con servizio di Accessibilità dava "App non installata" su ColorOS: tolta. Probabile vera causa: firma diversa da quella già installata (ColorOS dice solo "App non installata" quando c'è un conflitto di firma). La v4 (firma nuova) dava lo stesso messaggio: l'APK è valido (verificato con apksigner, zip integro), quindi va rimossa ogni copia di `org.stypox.dicio.claude` (anche in Clonazione app / Spazio privato / altri utenti) prima di installare.
- v4 (da provare sul telefono) usa insieme due strade, perché il sistema può bloccarne una in silenzio:
  1. avvio diretto grazie a "Mostra sopra altre app", con una finestrella sovrapposta invisibile 1x1 (da Android 15 il permesso vale solo se c'è una finestra sovrapposta visibile);
  2. notifica a schermo intero (USE_FULL_SCREEN_INTENT, da Android 14 va concessa a mano), che a schermo spento il sistema apre da solo.
  In più accende lo schermo (wake lock + turnScreenOn). Al primo avvio Dicio chiede, uno alla volta: sopra altre app → notifiche a schermo intero → nessuna ottimizzazione batteria.
- v5 = v4 senza il permesso BIND_NOTIFICATION_LISTENER_SERVICE e senza il servizio NotifyHandler (abilità "notifiche" di Dicio): Play Protect bloccava l'installazione ("App bloccata per proteggere il tuo dispositivo", protezione antifrode per app installate da browser con permessi sensibili). Non aggiungere permessi come lettura notifiche, SMS o Accessibilità.
- Salvo vuole il pannello assistente di Claude ("Parliamo, Salvo", quello del tasto di accensione), anche a schermo spento e SENZA sbloccare.
- Prove v6 sul telefono: schermo acceso e sbloccato → pannello di Claude, perfetto. Schermata di blocco (o schermo spento: si accende) → è Claude stesso a dire "sblocca per accedere a Claude", e dopo lo sblocco non si apriva niente. Il microfono di Dicio si fermava chiudendo l'app (ColorOS uccide l'app tolta dalle recenti).
- v7: se il telefono è bloccato, dopo launchAssist aspetta lo sblocco (ACTION_USER_PRESENT, fino a 60 s, microfono di Dicio in pausa nel frattempo) e richiama launchAssist; MainActivity ha excludeFromRecents, così non si può chiudere scorrendo dalle recenti. Da verificare.
- Verificato da Salvo: né il pannello vocale (ingranaggio), né le impostazioni dell'app Claude, né Impostazioni → App predefinite → Assistente digitale hanno un'opzione per la schermata di blocco.
- Aprire Claude SENZA sbloccare non dipende da noi: è l'app Claude che sulla schermata di blocco chiede lo sblocco. Alternative: sblocco col volto, Smart Lock/Extend Unlock, oppure blocco schermo "Nessuno"/"Scorri".
- Con blocco sicuro (PIN/impronta) serve comunque lo sblocco: Claude non può mostrarsi sopra la schermata di blocco. Lo sblocco col volto lo rende "senza mani".
- Dopo l'apertura di Claude il microfono di Dicio si ferma 20 s per lasciarlo a Claude.
- Claude va impostato come assistente digitale predefinito (al posto di Gemini) in Impostazioni → App → App predefinite → App assistente digitale.

## Come ricompilare l'APK
Android SDK 36, JDK 21 + JDK 17 (toolchain richiesta da un modulo: `-Porg.gradle.java.installations.paths=<jdk17>`), Gradle wrapper di Dicio, `./gradlew assembleRelease`, poi zipalign + apksigner. Se Maven Central risponde 429, usare in un init script di Gradle il mirror `https://maven-central.storage-download.googleapis.com/maven2/`. Alzare sempre `versionCode` a ogni nuova versione. Se la chiave originale non è disponibile, Salvo deve disinstallare l'app prima di installare una build firmata con una chiave nuova.
