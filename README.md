# Hey Claudio

Parola di attivazione personalizzata **"Hey Claudio"** per l'assistente vocale [Dicio](https://github.com/Stypox/dicio-android) su Android, usata per aprire Claude a voce.

## File

- `hey_claudio.tflite` — **il file da importare in Dicio** (Impostazioni → Metodi di input e output → Importa parola di attivazione personalizzata). Sensibilità già tarata sulla soglia fissa di Dicio (0,8).
- `hey_claudio.onnx` — lo stesso modello in formato ONNX (uscita grezza, soglia consigliata 0,7).
- `train_wakeword.ipynb` — notebook Colab usato come base per l'addestramento.

## Risultati dei test (voci sintetiche)

- "Hey Claudio" riconosciuto nel 99% dei clip di prova.
- Frasi simili scambiate per la parola: 3%.
- Musica: 1 attivazione per errore su 100 brani da 30 secondi.

Con la voce vera i risultati possono essere un po' diversi: se si attiva poco o troppo, il modello si può riaddestrare cambiando la sensibilità.

Addestramento basato su [openWakeWord](https://github.com/dscripka/openWakeWord) e [openwakeword-colab-2026](https://github.com/alfiedennen/openwakeword-colab-2026) (licenza MIT).

## Dicio per Claude

Versione modificata di Dicio in cui "Hey Claudio" apre direttamente Claude come assistente vocale, senza dover dire "apri Claude". Le modifiche sono in `dicio-per-claude/dicio-per-claude.patch` (da applicare al commit di Dicio indicato in `BASE_COMMIT`, copiando anche `hey_claudio.tflite` in `app/src/main/assets/`).

APK pronto: `apk/dicio-per-claude-v8.apk`. Apre Claude sia a schermo acceso sia a schermo spento (se il telefono ha PIN/impronta chiede prima lo sblocco).
