package com.alexbros.opidlubnyi.allfootball.helpers;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.alexbros.opidlubnyi.allfootball.models.ModelData;
import com.alexbros.opidlubnyi.allfootball.util.ObfuscatedString;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

public class URLContentHelper {
    private static final int CONNECTION_TIMEOUT = 30000;

    public static String getAllGoalsClientHeader() {
        return new ObfuscatedString(new long[]{0x161277BED41F67AEL, 0xA1204B68013F367EL, 0xA261FE01B58811D8L}).toString();
    }

    public static String getAllGoalsClientHeaderValue() {
        return "a " + "5.4" + " " + "701";
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (manager != null) {
            ni = manager.getActiveNetworkInfo();
        }
        return (ni != null && ni.isConnected());
    }

    public static String getTeamProfileResponse(String id) throws Exception {
        String url = "https://server.thelivescoreapp.com/api/v1/android/team/getProfile?id=" + id + "&language=" + Locale.getDefault().getLanguage();
        return getAllAsOneLineUrlResponse(url);
    }

    private static String getAllAsOneLineUrlResponse(String url) throws Exception {
        ArrayList<String> lines = getUrlResponse(url, null, null);
        if (lines != null && !lines.isEmpty()) {
            StringBuilder all = new StringBuilder();
            for (String line : lines)
                all.append(line);

            return all.toString();
        } else {
            return "";
        }
    }

    private static ArrayList<String> getUrlResponse(String url, PostData postData, ModelData model) throws Exception {
        try {
//            if (BuildConfig.DEBUG) {
//                Log.d("URL", url);
//
//                if (postData != null)
//                    Log.d("POST data", "\"" + postData.data + "\"");
//            }

            final String proxyAddress = System.getProperty("http.proxyHost");
            final String proxyPort = System.getProperty("https.proxyPort");

            Proxy proxy;
            if (proxyAddress != null && !proxyAddress.isEmpty() && proxyPort != null && !proxyPort.equals("0") && !proxyPort.isEmpty()) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
            } else {
                proxy = Proxy.NO_PROXY;
            }

            if (url.startsWith("https")) {
                HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection(proxy);
                return getUrlResponse(connection, postData, model);
            } else {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(proxy);
                return getUrlResponse(connection, postData, model);
            }
        } catch (OutOfMemoryError e) {
            MemoryCacheCleaner.clean();

            throw new Exception();
        }
    }

    private static ArrayList<String> getUrlResponse(HttpURLConnection connection, PostData postData, ModelData model) throws Exception {
        connection.setReadTimeout(CONNECTION_TIMEOUT);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);

        connection.setUseCaches(false);

        connection.addRequestProperty(new ObfuscatedString(new long[]{0xAB0EFE02CE1C67DFL, 0x5D885E67463C9BFCL, 0xC50789EF439A8E5BL}).toString(), "true");
        connection.addRequestProperty(new ObfuscatedString(new long[]{0xD3261D75EDA0F1E3L, 0x76596614B15269D7L, 0x848AEEDA337375D5L}).toString(), "true");
        connection.addRequestProperty(getAllGoalsClientHeader(), getAllGoalsClientHeaderValue());
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//        connection.setRequestProperty("User-Agent", checkString(Build.MANUFACTURER) + "/" + checkString(Build.MODEL) + "/" + checkString(Build.VERSION.RELEASE) + "/" + Build.VERSION.SDK_INT + "/TLA " + model.versionName + ", gzip");
//        if (BuildConfig.ENABLE_DEV_SETTINGS) {
//            if (!model.dev_geoCountryCode.isEmpty()) {
//                connection.setRequestProperty("X-AppEngine-Country", model.dev_geoCountryCode);
//            }
//        }
        //connection.setRequestProperty("Connection", "close");
        if (postData != null) {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            if (postData.utf8) {
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postData.data.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
            } else {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(postData.data);
                outputStreamWriter.flush();
                outputStreamWriter.close();
            }

        }
        ArrayList<String> lines = new ArrayList<>();
        InputStream inputStream = connection.getInputStream();
        String contentEncoding = connection.getHeaderField("Content-Encoding");

        InputStreamReader in;

        if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
            in = new InputStreamReader(new GZIPInputStream(inputStream), "UTF-8");
        } else {
            in = new InputStreamReader(inputStream, "UTF-8");
        }

        BufferedReader br = new BufferedReader(in);
        String s;
        while ((s = br.readLine()) != null) {
            lines.add(s);
        }
        in.close();
        connection.disconnect();

//        if (BuildConfig.ENABLE_DEV_SETTINGS) {
//            if (model.dev_serverResponseDelay > 0) {
//                try {
//                    Log.d("DEV", "Adding server response delay " + model.dev_serverResponseDelay + " ms");
//                    Thread.sleep(model.dev_serverResponseDelay);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        return lines;
    }


//    private static String checkString(String s) {
//        if (s != null)
//            return s;
//        else
//            return "";
//    }

    public static String getOverviewResponse(int day) throws Exception {
        String url = "https://server.thelivescoreapp.com/api/v1/android/events/getOverview?day=" + day + "&offset=7200&language=uk&timestamp=0";

        return getAllAsOneLineUrlResponse(url);
    }

    public static String getConfigResponse(String configURL, ModelData model) throws Exception {
        return getAllAsOneLineUrlResponse(configURL, null, model);
    }

    private static String getAllAsOneLineUrlResponse(String url, PostData postData, ModelData model) throws Exception {
        ArrayList<String> lines = getUrlResponse(url, postData, model);
        if (lines != null && !lines.isEmpty()) {
            String all = "";
            for (String line : lines)
                all += line;

            return all;
        } else {
            return "";
        }
    }

    private static class PostData {
        String data = null;
        boolean utf8 = false;
    }

    public static ArrayList<String> getLiveOddsResponse(ModelData model, String eventId, String providerId) throws Exception {
        String url = "https://server.thelivescoreapp.com/soccer/eventliveodds/3?eventId=" + eventId + "&providerId=" + providerId;
        return getUrlResponse(url, null, model);
    }

    public static String getEventDetailResponse(ModelData model, String eventId) throws Exception {
        String url = "https://server.thelivescoreapp.com/api/v1/android/events/getDetails?eventId=" + eventId
                + "&userId="
                + "&trend=false"
                + "&calendar=false"
                + "&showevent=true";

        return getAllAsOneLineUrlResponse(url, null, model);
    }
}
