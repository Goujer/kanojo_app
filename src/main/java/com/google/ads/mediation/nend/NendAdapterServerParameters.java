package com.google.ads.mediation.nend;

import com.google.ads.mediation.MediationServerParameters;

public final class NendAdapterServerParameters extends MediationServerParameters {
    @MediationServerParameters.Parameter(name = "apiKey")
    public String apiKey;
    @MediationServerParameters.Parameter(name = "spotId")
    public String spotIdStr;
}
