package com.google.ads.mediation.admob;

import com.google.ads.mediation.MediationServerParameters;

public final class AdMobAdapterServerParameters extends MediationServerParameters {
    @MediationServerParameters.Parameter(name = "pubid")
    public String adUnitId;
    @MediationServerParameters.Parameter(name = "mad_hac", required = false)
    public String allowHouseAds = null;
}
