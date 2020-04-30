package com.google.analytics.tracking.android;

import android.content.Context;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

class ClientIdDefaultProvider implements DefaultProvider {
	//TODO: Fix this.
    private static ClientIdDefaultProvider sInstance;
    private static final Object sInstanceLock = new Object();
	private String mClientId;
    private boolean mClientIdLoaded = false;
    private final Object mClientIdLock = new Object();
    private final Context mContext;

    static void initializeProvider(Context c) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new ClientIdDefaultProvider(c);
            }
        }
    }

    @VisibleForTesting
    static void dropInstance() {
        synchronized (sInstanceLock) {
            sInstance = null;
        }
    }

    public static ClientIdDefaultProvider getProvider() {
        ClientIdDefaultProvider clientIdDefaultProvider;
        synchronized (sInstanceLock) {
            clientIdDefaultProvider = sInstance;
        }
        return clientIdDefaultProvider;
    }

    private ClientIdDefaultProvider(Context c) {
        this.mContext = c;
        asyncInitializeClientId();
    }

    public boolean providesField(String field) {
        return Fields.CLIENT_ID.equals(field);
    }

    public String getValue(String field) {
        if (Fields.CLIENT_ID.equals(field)) {
            return blockingGetClientId();
        }
        return null;
    }

    private String blockingGetClientId() {
        if (!this.mClientIdLoaded) {
            synchronized (this.mClientIdLock) {
                if (!this.mClientIdLoaded) {
                    Log.v("Waiting for clientId to load");
                    do {
                        try {
                            this.mClientIdLock.wait();
                        } catch (InterruptedException e) {
                            Log.e("Exception while waiting for clientId: " + e);
                        }
                    } while (!this.mClientIdLoaded);
                }
            }
        }
        Log.v("Loaded clientId");
        return this.mClientId;
    }

    private boolean storeClientId(String clientId) {
        try {
            Log.v("Storing clientId.");
            FileOutputStream fos = this.mContext.openFileOutput("gaClientId", 0);
            fos.write(clientId.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("Error creating clientId file.");
            return false;
        } catch (IOException e2) {
            Log.e("Error writing to clientId file.");
            return false;
        }
    }

    private String generateClientId() {
        String result = UUID.randomUUID().toString().toLowerCase();
        if (!storeClientId(result)) {
            return GreeDefs.BARCODE;
        }
        return result;
    }

    private void asyncInitializeClientId() {
        new Thread("client_id_fetcher") {
            public void run() {
                synchronized (ClientIdDefaultProvider.this.mClientIdLock) {
                    String unused = ClientIdDefaultProvider.this.mClientId = ClientIdDefaultProvider.this.initializeClientId();
                    boolean unused2 = ClientIdDefaultProvider.this.mClientIdLoaded = true;
                    ClientIdDefaultProvider.this.mClientIdLock.notifyAll();
                }
            }
        }.start();
    }

    @VisibleForTesting
	private String initializeClientId() {
        String rslt = null;
        try {
            FileInputStream input = this.mContext.openFileInput("gaClientId");
            byte[] bytes = new byte[128];
            int readLen = input.read(bytes, 0, 128);
            if (input.available() > 0) {
                Log.e("clientId file seems corrupted, deleting it.");
                input.close();
                this.mContext.deleteFile("gaClientId");
            } else if (readLen <= 0) {
                Log.e("clientId file seems empty, deleting it.");
                input.close();
                this.mContext.deleteFile("gaClientId");
            } else {
                String rslt2 = new String(bytes, 0, readLen);
                try {
                    input.close();
                    rslt = rslt2;
                } catch (FileNotFoundException e) {
                    rslt = rslt2;
                } catch (IOException e2) {
                    rslt = rslt2;
                    Log.e("Error reading clientId file, deleting it.");
                    this.mContext.deleteFile("gaClientId");
                }
            }
        } catch (FileNotFoundException e3) {
        } catch (IOException e4) {
            Log.e("Error reading clientId file, deleting it.");
            this.mContext.deleteFile("gaClientId");
        }
        if (rslt == null) {
            return generateClientId();
        }
        return rslt;
    }
}
