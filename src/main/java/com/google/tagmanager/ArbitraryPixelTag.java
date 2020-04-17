package com.google.tagmanager;

import android.content.Context;
import android.net.Uri;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ArbitraryPixelTag extends TrackingTag {
    private static final String ADDITIONAL_PARAMS = Key.ADDITIONAL_PARAMS.toString();
    static final String ARBITRARY_PIXEL_UNREPEATABLE = ("gtm_" + ID + "_unrepeatable");
    private static final String ID = FunctionType.ARBITRARY_PIXEL.toString();
    private static final String UNREPEATABLE = Key.UNREPEATABLE.toString();
    private static final String URL = Key.URL.toString();
    private static final Set<String> unrepeatableIds = new HashSet();
    private final Context mContext;
    private final HitSenderProvider mHitSenderProvider;

    public interface HitSenderProvider {
        HitSender get();
    }

    public static String getFunctionId() {
        return ID;
    }

    public ArbitraryPixelTag(final Context context) {
        this(context, new HitSenderProvider() {
            public HitSender get() {
                return DelayedHitSender.getInstance(context);
            }
        });
    }

    @VisibleForTesting
    ArbitraryPixelTag(Context context, HitSenderProvider hitSenderProvider) {
        super(ID, URL);
        this.mHitSenderProvider = hitSenderProvider;
        this.mContext = context;
    }

    public void evaluateTrackingTag(Map<String, TypeSystem.Value> tag) {
        String unrepeatableId;
        if (tag.get(UNREPEATABLE) != null) {
            unrepeatableId = Types.valueToString(tag.get(UNREPEATABLE));
        } else {
            unrepeatableId = null;
        }
        if (unrepeatableId == null || !idProcessed(unrepeatableId)) {
            Uri.Builder uriBuilder = Uri.parse(Types.valueToString(tag.get(URL))).buildUpon();
            TypeSystem.Value additionalParamsList = tag.get(ADDITIONAL_PARAMS);
            if (additionalParamsList != null) {
                Object l = Types.valueToObject(additionalParamsList);
                if (!(l instanceof List)) {
                    Log.e("ArbitraryPixel: additional params not a list: not sending partial hit: " + uriBuilder.build().toString());
                    return;
                }
                for (Object m : (List) l) {
                    if (!(m instanceof Map)) {
                        Log.e("ArbitraryPixel: additional params contains non-map: not sending partial hit: " + uriBuilder.build().toString());
                        return;
                    }
                    for (Map.Entry<Object, Object> entry : ((Map) m).entrySet()) {
                        uriBuilder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    }
                }
            }
            String uri = uriBuilder.build().toString();
            this.mHitSenderProvider.get().sendHit(uri);
            Log.v("ArbitraryPixel: url = " + uri);
            if (unrepeatableId != null) {
                synchronized (ArbitraryPixelTag.class) {
                    unrepeatableIds.add(unrepeatableId);
                    SharedPreferencesUtil.saveAsync(this.mContext, ARBITRARY_PIXEL_UNREPEATABLE, unrepeatableId, "true");
                }
            }
        }
    }

    private synchronized boolean idProcessed(String unrepeatableId) {
        boolean z = true;
        synchronized (this) {
            if (!idInCache(unrepeatableId)) {
                if (idInSharedPreferences(unrepeatableId)) {
                    unrepeatableIds.add(unrepeatableId);
                } else {
                    z = false;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean idInSharedPreferences(String unrepeatableId) {
        return this.mContext.getSharedPreferences(ARBITRARY_PIXEL_UNREPEATABLE, 0).contains(unrepeatableId);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void clearCache() {
        unrepeatableIds.clear();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean idInCache(String unrepeatableId) {
        return unrepeatableIds.contains(unrepeatableId);
    }
}
