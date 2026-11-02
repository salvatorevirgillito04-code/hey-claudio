package org.stypox.dicio.claude.invio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Called by Dicio per Claude (same signing key) right before it opens the WhatsApp chat. */
public class SendRequestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SendRequest.arm(context);
    }
}
