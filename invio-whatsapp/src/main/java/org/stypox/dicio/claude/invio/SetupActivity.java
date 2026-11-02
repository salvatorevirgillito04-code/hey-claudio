package org.stypox.dicio.claude.invio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Explains what to enable, with a button to the accessibility settings. */
public class SetupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        TextView text = new TextView(this);
        text.setTextSize(18);
        text.setText("Invio WhatsApp serve a Dicio per Claude per premere \"Invia\" quando dici, "
                + "per esempio: \"Hey Claudio, manda un messaggio a Mario su WhatsApp ciao come "
                + "stai e invia\".\n\nTocca il pulsante qui sotto, entra in \"App installate\" "
                + "(o \"Servizi scaricati\"), scegli \"Invio WhatsApp per Hey Claudio\" e "
                + "attivalo.\n\nSe l'interruttore è grigio: Impostazioni → App → Invio WhatsApp → "
                + "⋮ in alto a destra → \"Consenti impostazioni con restrizioni\", poi riprova.");
        layout.addView(text);

        Button button = new Button(this);
        button.setText("Apri le impostazioni di Accessibilità");
        button.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        layout.addView(button);

        setContentView(layout);
    }
}
