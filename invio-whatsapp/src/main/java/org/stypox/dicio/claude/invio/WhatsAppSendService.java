package org.stypox.dicio.claude.invio;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Presses the "Invia" button of WhatsApp, but only while a request from Dicio per Claude is
 * pending (a few seconds after the voice command). It never reads or stores messages.
 */
public class WhatsAppSendService extends AccessibilityService {
    private static final String TAG = "InvioWhatsApp";
    private static final String[] SEND_IDS = {
            "com.whatsapp:id/send", "com.whatsapp.w4b:id/send",
    };
    private static final String[] SEND_DESCRIPTIONS = {"Invia", "Send"};

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!SendRequest.isArmed(this)) return;
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;
        CharSequence pkg = root.getPackageName();
        if (pkg == null || !pkg.toString().startsWith("com.whatsapp")) return;

        AccessibilityNodeInfo button = findSendButton(root);
        if (button == null) return;
        if (clickNodeOrParent(button)) {
            Log.d(TAG, "Pressed Invia");
            SendRequest.disarm(this);
        }
    }

    private static AccessibilityNodeInfo findSendButton(AccessibilityNodeInfo root) {
        for (String id : SEND_IDS) {
            List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByViewId(id);
            if (nodes != null) {
                for (AccessibilityNodeInfo node : nodes) {
                    if (node.isVisibleToUser() && node.isEnabled()) return node;
                }
            }
        }
        for (String description : SEND_DESCRIPTIONS) {
            List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByText(description);
            if (nodes != null) {
                for (AccessibilityNodeInfo node : nodes) {
                    CharSequence desc = node.getContentDescription();
                    if (desc != null && desc.toString().equalsIgnoreCase(description)
                            && node.isVisibleToUser() && node.isEnabled()) {
                        return node;
                    }
                }
            }
        }
        return null;
    }

    private static boolean clickNodeOrParent(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo current = node;
        for (int i = 0; i < 3 && current != null; i++) {
            if (current.isClickable()) {
                return current.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            current = current.getParent();
        }
        return false;
    }

    @Override
    public void onInterrupt() {
    }
}
