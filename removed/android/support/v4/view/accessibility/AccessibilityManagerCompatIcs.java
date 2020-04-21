package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityManager;
import java.util.List;

class AccessibilityManagerCompatIcs {

    interface AccessibilityStateChangeListenerBridge {
        void onAccessibilityStateChanged(boolean z);
    }

    AccessibilityManagerCompatIcs() {
    }

    public static Object newAccessibilityStateChangeListener(final AccessibilityStateChangeListenerBridge bridge) {
        return new AccessibilityManager.AccessibilityStateChangeListener() {
            public void onAccessibilityStateChanged(boolean enabled) {
                bridge.onAccessibilityStateChanged(enabled);
            }
        };
    }

    public static boolean addAccessibilityStateChangeListener(AccessibilityManager manager, Object listener) {
        return manager.addAccessibilityStateChangeListener((AccessibilityManager.AccessibilityStateChangeListener) listener);
    }

    public static boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, Object listener) {
        return manager.removeAccessibilityStateChangeListener((AccessibilityManager.AccessibilityStateChangeListener) listener);
    }

    public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
        return manager.getEnabledAccessibilityServiceList(feedbackTypeFlags);
    }

    public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
        return manager.getInstalledAccessibilityServiceList();
    }

    public static boolean isTouchExplorationEnabled(AccessibilityManager manager) {
        return manager.isTouchExplorationEnabled();
    }
}
