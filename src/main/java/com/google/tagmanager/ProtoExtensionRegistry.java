package com.google.tagmanager;

import com.google.analytics.containertag.proto.Serving;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;

class ProtoExtensionRegistry {
    private static ExtensionRegistryLite registry;

    ProtoExtensionRegistry() {
    }

    public static synchronized ExtensionRegistryLite getRegistry() {
        ExtensionRegistryLite extensionRegistryLite;
        synchronized (ProtoExtensionRegistry.class) {
            if (registry == null) {
                registry = ExtensionRegistryLite.newInstance();
                Serving.registerAllExtensions(registry);
            }
            extensionRegistryLite = registry;
        }
        return extensionRegistryLite;
    }
}
