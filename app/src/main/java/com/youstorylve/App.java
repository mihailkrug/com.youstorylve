package com.youstorylve;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.onesignal.OneSignal;

import java.util.Map;


public class App extends Application {
    public static final String AF_DEV_KEY = "WnB9CFSdjJdYkCJzjB5dbG";
    public static PreferencesManagerImpl preferencesManager;
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        preferencesManager = new PreferencesManagerImpl(getApplicationContext());

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {


            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib init = AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, getApplicationContext());
        Log.d("APPS_FLYER ID", "AppsFlyerUID" + init.getAppsFlyerUID(getApplicationContext()));

        preferencesManager.setAppsFlyerUID(init.getAppsFlyerUID(getApplicationContext()));

        AppsFlyerLib.getInstance().startTracking(this);

    }
}
