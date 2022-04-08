package com.google.zxing.client.android.camera.exposure;

import com.google.zxing.client.android.common.PlatformSupportManager;

public final class ExposureManager extends PlatformSupportManager<ExposureInterface> {
    public ExposureManager() {
        super(ExposureInterface.class, new DefaultExposureInterface());
        addImplementationClass(8, "com.google.zxing.client.android.camera.exposure.FroyoExposureInterface");
    }
}
