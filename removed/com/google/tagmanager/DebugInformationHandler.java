package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;

interface DebugInformationHandler {
    void receiveEventInfo(MutableDebug.EventInfo eventInfo);
}
