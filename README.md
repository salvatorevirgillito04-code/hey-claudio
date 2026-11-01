# Hey Claudio

Parola di attivazione personalizzata "Hey Claudio" per l'assistente vocale Dicio su Android, usata per aprire Claude.

## Come si usa

1. Apri il notebook su Colab:
   https://colab.research.google.com/github/salvatorevirgillito04-code/hey-claudio/blob/main/train_wakeword.ipynb
2. Runtime → Cambia tipo di runtime → T4 GPU.
3. Runtime → Esegui tutte (circa 2,5 ore su Colab gratuito, tenere lo schermo acceso).
4. Alla fine viene scaricato `hey_claudio.onnx`: va convertito in `.tflite` e importato in Dicio
   (Impostazioni → Metodi di input e output → Importa parola di attivazione personalizzata).

Notebook basato su [openwakeword-colab-2026](https://github.com/alfiedennen/openwakeword-colab-2026) (licenza MIT).
