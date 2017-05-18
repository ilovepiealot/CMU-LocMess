/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grupo19.locmess19.Services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import grupo19.locmess19.Activities.InboxActivity;
import grupo19.locmess19.Activities.MessagesActivity;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 *
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 *
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification assocaited with that service is removed.
 */
public class LocationUpdatesService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationupdatesforegroundservice";

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    public static final String EXTRA_STRING = PACKAGE_NAME + "STRING";

    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";


    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * The entry point to Google Play Services.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    private Location mLocation;

    private ServerCommunication server;
    ArrayList<String[]> messageList;
    ArrayList<String[]> locationList;
    HashMap<String, String> userKeys;

    public String[] messageStringArray;

    public String username;
    private int sessionID = 0;


    // Current date, time
    Calendar dateCurrent = Calendar.getInstance();

    public WifiManager mWifiManager;

    public List<ScanResult> mScanResults;


    public LocationUpdatesService() {
    }

    @SuppressLint("WifiManagerLeak")
    @Override
    public void onCreate() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        createLocationRequest();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        server = new ServerCommunication("10.0.2.2", 11113);

        // mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        /* registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); */
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            /*
            // TODO(developer). If targeting O, use the following code.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        LocationUpdatesService.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }
             */
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
        mGoogleApiClient.disconnect();
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, LocationUpdatesService.this);
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    LocationUpdatesService.this);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdatesService.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        Intent newMessageIntent = new Intent(this, InboxActivity.class);
        newMessageIntent.putExtra("messageStringArray", messageStringArray);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                newMessageIntent, 0);

        return new NotificationCompat.Builder(this)
                .addAction(R.drawable.cast_album_art_placeholder, getString(R.string.launch_activity),
                        activityPendingIntent)
                .addAction(R.drawable.cast_album_art_placeholder, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText(text)
                .setContentTitle(Utils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis()).build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        try {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // In this example, we merely log the suspension.
        Log.e(TAG, "GoogleApiClient connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // In this example, we merely log the failure.
        Log.e(TAG, "GoogleApiClient connection failed.");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "New location: " + location);

        mLocation = location;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("loggedUser", "");


        if (location != null) {
            Long CurrentEpoch = dateCurrent.getTimeInMillis();
            messageList = server.getExistingMessages();
            locationList = server.getExistingLocations();
            userKeys = server.getUserKeys(sessionID);
            Boolean checkWhitelist = false;
            Boolean checkBlacklist = false;
            ArrayList<String[]> whitelistKeys = new ArrayList<>();
            ArrayList<String[]> blacklistKeys = new ArrayList<>();
            for (String[] messagesServer : messageList) {
                if (!messagesServer[5].equals(username)) {
                    Log.e(TAG, "username is different");
                    if (Long.parseLong(messagesServer[2]) <= CurrentEpoch && Long.parseLong(messagesServer[3]) >= CurrentEpoch) {
                        Log.e(TAG, "Message is active");
                        // KEYS VERIFICATION
                        String[] messageWhitelistKeyPairs = messagesServer[7].split("#KEY#");
                        String[] messageBlacklistKeyPairs = messagesServer[8].split("#KEY#");

                        for (String whitelistKey : messageWhitelistKeyPairs) {
                            Log.e(TAG, String.valueOf("Whitelist Key " + whitelistKey));
                            try {
                                whitelistKeys.add(whitelistKey.split(" -> "));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        for (String blacklistKey : messageBlacklistKeyPairs) {
                            Log.e(TAG, String.valueOf("Blacklist Key " + blacklistKey));

                            try {
                                blacklistKeys.add(blacklistKey.split(" -> "));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // WHITELIST
                        try {
                            for (String key : userKeys.keySet()) {
                                for (String[] whitelistKey : whitelistKeys) {
                                    Log.e(TAG, String.valueOf("Key " + key));
                                    Log.e(TAG, String.valueOf("Value " + userKeys.get(key)));
                                    Log.e(TAG, String.valueOf("Message key " + whitelistKey[0]));
                                    Log.e(TAG, String.valueOf("Message value " + whitelistKey[1]));
                                    if (whitelistKey[0].equals(key) && whitelistKey[1].equals(userKeys.get(key))) {
                                        checkWhitelist = true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // BLACKLIST
                        try {
                            for (String key : userKeys.keySet()) {
                                for (String[] blacklistKey : blacklistKeys) {
                                    if (blacklistKey[0].equals(key) && blacklistKey[1].equals(userKeys.get(key))) {
                                        checkBlacklist = true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, String.valueOf(checkWhitelist));
                        Log.e(TAG, String.valueOf(checkBlacklist));
                        if (checkWhitelist == true && checkBlacklist == false) {
                            Log.e(TAG, "Message is whitelisted");
                            for (String[] locServer : locationList) {
                                if (locServer[0].equals(messagesServer[4])) {
                                    Log.e(TAG, "location found in list");
                                    if (locServer.length == 4) {
                                        if (checkLocationInRadius(location, locServer[1], locServer[2], locServer[3])) {
                                            Log.e(TAG, "GPS location check");
                                            String finalMessage = "User: " + messagesServer[5] + ", Title: " + messagesServer[0];
                                            messageStringArray = messagesServer;
                                            Log.e(TAG, finalMessage);
                                            // Notify anyone listening for broadcasts about the new location.
                                            Intent intent = new Intent(ACTION_BROADCAST);
                                            // intent.putExtra(EXTRA_LOCATION, location);
                                            intent.putExtra(EXTRA_STRING, finalMessage);
                                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                                            // Update notification content if running as a foreground service.
                                            if (serviceIsRunningInForeground(this)) {
                                                mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            SharedPreferences sharedPeers = PreferenceManager.getDefaultSharedPreferences(this);
                            String peerListusername = sharedPeers.getString("peerList", "").split("\\s+")[0];
                            Log.e(TAG, "Peer List: " + peerListusername);
                            for (String[] locServer : locationList) {
                                if (locServer[0].equals(messagesServer[4])) {
                                    Log.e(TAG, "location found in list");
                                    if (locServer.length == 2) {
                                        if (locServer[1].equals(peerListusername)) {
                                            Log.e(TAG, "SSID location check");
                                            String finalMessage = "User: " + messagesServer[5] + ", Title: " + messagesServer[0];
                                            messageStringArray = messagesServer;
                                            Log.e(TAG, finalMessage);
                                            // Notify anyone listening for broadcasts about the new location.
                                            Intent intent = new Intent(ACTION_BROADCAST);
                                            // intent.putExtra(EXTRA_LOCATION, location);
                                            intent.putExtra(EXTRA_STRING, finalMessage);
                                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                                            // Update notification content if running as a foreground service.
                                            if (serviceIsRunningInForeground(this)) {
                                                mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                                            }
                                            break;
                                        }
                                    }
                                }
                            }

                                /* if (mScanResults != null) {
                                    for (ScanResult wifi : mScanResults) {
                                        if (wifi.SSID.equals(locServer[1])) {
                                            Log.e(TAG, "WIFI location check");
                                            String finalMessage = "User: " + messagesServer[5] + ", Title: " + messagesServer[0];
                                            messageStringArray = messagesServer;
                                            Log.e(TAG, finalMessage);
                                            // Notify anyone listening for broadcasts about the new location.
                                            Intent intent = new Intent(ACTION_BROADCAST);
                                            // intent.putExtra(EXTRA_LOCATION, location);
                                            intent.putExtra(EXTRA_STRING, finalMessage);
                                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                                            // Update notification content if running as a foreground service.
                                            if (serviceIsRunningInForeground(this)) {
                                                mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                                            }
                                            break;
                                        }
                                    }
                                } */
                        }
                    }
                }
            }
        }
    }

    public Boolean checkLocationInRadius(Location location, String Latitude, String Longitude, String Radius) {

        float[] distance = new float[2];
        Float radius = Float.parseFloat(Radius);
        double latitude = Double.parseDouble(Latitude);
        double longitude = Double.parseDouble(Longitude);

        Location.distanceBetween(latitude, longitude,
                location.getLatitude(), location.getLongitude(), distance);

        if( distance[0] > radius){

            Log.e(TAG, "Distance: " + String.valueOf(distance[0]));
            Log.e(TAG, "Radius: " + String.valueOf(radius));
            return false;
        } else {
            Log.e(TAG, "Distance: " + String.valueOf(distance[0]));
            Log.e(TAG, "Radius: " + String.valueOf(radius));
            return true;
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationUpdatesService getService() {
            return LocationUpdatesService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    };

}