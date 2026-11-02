package org.stypox.dicio.claude.invio;

import android.content.Context;

/** The pending "press Invia" request, valid only for a few seconds. */
final class SendRequest {
    static final long VALID_MILLIS = 20_000L;
    private static final String PREFS = "invio";
    private static final String KEY = "armed_until";

    private SendRequest() {
    }

    static void arm(Context context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putLong(KEY, System.currentTimeMillis() + VALID_MILLIS).apply();
    }

    static boolean isArmed(Context context) {
        return System.currentTimeMillis() <
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getLong(KEY, 0L);
    }

    static void disarm(Context context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putLong(KEY, 0L).apply();
    }
}
