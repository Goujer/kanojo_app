/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.google.zxing.client.android.PreferencesActivity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class CameraConfigurationManager {
    private static final int MAX_PREVIEW_PIXELS = 1024000;
    private static final int MIN_PREVIEW_PIXELS = 150400;
    private static final String TAG = "CameraConfiguration";
    private Point cameraResolution;
    private final Context context;
    private Point screenResolution;

	CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
    void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
	    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    Display display = manager.getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();
        if (width < height) {
            Log.i(TAG, "Display reports portrait orientation; assuming this is incorrect");
            int temp = width;
            width = height;
            height = temp;
        }
        this.screenResolution = new Point(width, height);
        Log.i(TAG, "Screen resolution: " + this.screenResolution);
        this.cameraResolution = findBestPreviewSizeValue(parameters, this.screenResolution);
        Log.i(TAG, "Camera resolution: " + this.cameraResolution);
    }

    void setDesiredCameraParameters(Camera camera, boolean safeMode) {
        String colorMode;
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }
        Log.i(TAG, "Initial camera parameters: " + parameters.flatten());
        if (safeMode) {
            Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        initializeTorch(parameters, prefs, safeMode);
        String focusMode = null;
        if (prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true)) {
            if (safeMode || prefs.getBoolean(PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS, true)) {
                focusMode = findSettableValue(parameters.getSupportedFocusModes(), "auto");
            } else {
                focusMode = findSettableValue(parameters.getSupportedFocusModes(), "continuous-picture", "continuous-video", "auto");
            }
        }
        if (!safeMode && focusMode == null) {
            focusMode = findSettableValue(parameters.getSupportedFocusModes(), "macro", "edof");
        }
        if (focusMode != null) {
            parameters.setFocusMode(focusMode);
        }
        if (prefs.getBoolean(PreferencesActivity.KEY_INVERT_SCAN, false) && (colorMode = findSettableValue(parameters.getSupportedColorEffects(), "negative")) != null) {
            parameters.setColorEffect(colorMode);
        }
        parameters.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
        camera.setParameters(parameters);
    }

    Point getCameraResolution() {
        return this.cameraResolution;
    }

    Point getScreenResolution() {
        return this.screenResolution;
    }

    boolean getTorchState(Camera camera) {
        String flashMode;
        if (camera == null || camera.getParameters() == null || (flashMode = camera.getParameters().getFlashMode()) == null) {
            return false;
        }
        if ("on".equals(flashMode) || "torch".equals(flashMode)) {
            return true;
        }
        return false;
    }

    void setTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        doSetTorch(parameters, newSetting, false);
        camera.setParameters(parameters);
    }

    private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode) {
        doSetTorch(parameters, FrontLightMode.readPref(prefs) == FrontLightMode.ON, safeMode);
    }

    private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
        String flashMode;
        if (newSetting) {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), "torch", "on");
        } else {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), "off");
        }
        if (flashMode != null) {
            parameters.setFlashMode(flashMode);
        }
    }

    private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution2) {
        int maybeFlippedWidth;
        int maybeFlippedHeight;
        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            Log.w(TAG, "Device returned no supported preview sizes; using default");
            Camera.Size defaultSize = parameters.getPreviewSize();
            return new Point(defaultSize.width, defaultSize.height);
        }
        ArrayList<Camera.Size> arrayList = new ArrayList<>(rawSupportedSizes);
        Collections.sort(arrayList, new Comparator<Camera.Size>() {
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });
        if (Log.isLoggable(TAG, Log.INFO)) {
            StringBuilder previewSizesString = new StringBuilder();
            for (Camera.Size supportedPreviewSize : arrayList) {
                previewSizesString.append(supportedPreviewSize.width).append('x').append(supportedPreviewSize.height).append(' ');
            }
            Log.i(TAG, "Supported preview sizes: " + previewSizesString);
        }
        Point bestSize = null;
        float screenAspectRatio = ((float) screenResolution2.x) / ((float) screenResolution2.y);
        float diff = Float.POSITIVE_INFINITY;
        for (Camera.Size supportedPreviewSize2 : arrayList) {
            int realWidth = supportedPreviewSize2.width;
            int realHeight = supportedPreviewSize2.height;
            int pixels = realWidth * realHeight;
            if (pixels >= MIN_PREVIEW_PIXELS && pixels <= MAX_PREVIEW_PIXELS) {
                boolean isCandidatePortrait = realWidth < realHeight;
                if (isCandidatePortrait) {
                    maybeFlippedWidth = realHeight;
                } else {
                    maybeFlippedWidth = realWidth;
                }
                if (isCandidatePortrait) {
                    maybeFlippedHeight = realWidth;
                } else {
                    maybeFlippedHeight = realHeight;
                }
                if (maybeFlippedWidth == screenResolution2.x && maybeFlippedHeight == screenResolution2.y) {
                    Point exactPoint = new Point(realWidth, realHeight);
                    Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
                    return exactPoint;
                }
                float newDiff = Math.abs((((float) maybeFlippedWidth) / ((float) maybeFlippedHeight)) - screenAspectRatio);
                if (newDiff < diff) {
                    bestSize = new Point(realWidth, realHeight);
                    diff = newDiff;
                }
            }
        }
        if (bestSize == null) {
            Camera.Size defaultSize2 = parameters.getPreviewSize();
            bestSize = new Point(defaultSize2.width, defaultSize2.height);
            Log.i(TAG, "No suitable preview sizes, using default: " + bestSize);
        }
        Log.i(TAG, "Found best approximate preview size: " + bestSize);
        return bestSize;
    }

    private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
        Log.i(TAG, "Supported values: " + supportedValues);
        String result = null;
        if (supportedValues != null) {
            int length = desiredValues.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String desiredValue = desiredValues[i];
                if (supportedValues.contains(desiredValue)) {
                    result = desiredValue;
                    break;
                }
                i++;
            }
        }
        Log.i(TAG, "Settable value: " + result);
        return result;
    }
}
