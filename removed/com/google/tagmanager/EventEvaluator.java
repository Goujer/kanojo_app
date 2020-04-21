package com.google.tagmanager;

import com.google.tagmanager.ResourceUtil;

class EventEvaluator {
    private final ResourceUtil.ExpandedResource mResource;
    private final Runtime mRuntime;

    public EventEvaluator(Runtime runtime, ResourceUtil.ExpandedResource resource) {
        if (runtime == null) {
            throw new NullPointerException("runtime cannot be null");
        }
        this.mRuntime = runtime;
        if (resource != runtime.getResource()) {
            throw new IllegalArgumentException("resource must be the same as the resource in runtime");
        }
        this.mResource = runtime.getResource();
    }

    /* access modifiers changed from: package-private */
    public void evaluateEvent(String eventName) {
        throw new UnsupportedOperationException("this code not yet written");
    }
}
