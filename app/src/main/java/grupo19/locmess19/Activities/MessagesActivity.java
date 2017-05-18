package grupo19.locmess19.Activities;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.Fragments.Locations;
import grupo19.locmess19.Fragments.MessagesMenu;
import grupo19.locmess19.Fragments.ProfilesFragment;
import grupo19.locmess19.R;
import grupo19.locmess19.Services.LocationUpdatesService;
import grupo19.locmess19.Services.Utils;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;


public class MessagesActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        SimWifiP2pManager.PeerListListener
        {


    private static final String TAG = MessagesActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // UI elements.
    private Button mRequestLocationUpdatesButton;

    private boolean check = false;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;

// Monitors the state of the connection to the service.
private final ServiceConnection mServiceConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            }

    @Override
    public void onServiceDisconnected(ComponentName name) {
            mService = null;
            }
    };

            private ServiceConnection mConnection = new ServiceConnection() {
                // callbacks for service binding, passed to bindService()

                @Override
                public void onServiceConnected(ComponentName className, IBinder service) {
                    mManager = new SimWifiP2pManager(new Messenger(service));
                    mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
                    mBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    mManager = null;
                    mChannel = null;
                    mBound = false;
                }
            };


/**
 * The {@link android.support.v4.view.PagerAdapter} that will provide
 * fragments for each of the sections. We use a
 * {@link FragmentPagerAdapter} derivative, which will keep every
 * loaded fragment in memory. If this becomes too memory intensive, it
 * may be best to switch to a
 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
 */
private SectionsPagerAdapter mSectionsPagerAdapter;

/**
 * The {@link ViewPager} that will host the section contents.
 */
private ViewPager mViewPager;
private String username;
private int sessionID;


            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.activity_messages);
        guiSetButtonListeners();
        guiUpdateInitState();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("loggedUser", "");
        sessionID = sharedPreferences.getInt("sessionID", 0);

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new SimWifiP2pBroadcastReceiver(this);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
        .registerOnSharedPreferenceChangeListener(this);

        mRequestLocationUpdatesButton = (Button) findViewById(R.id.request_location_updates_button);

        mRequestLocationUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!check) {
                        Intent intent = new Intent(view.getContext(), SimWifiP2pService.class);
                        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                        mBound = true;
                        Toast.makeText(mService, "CENAS", Toast.LENGTH_SHORT).show();
                        check = true;
                        mService.requestLocationUpdates();
                    } else {
                        if (mBound) {
                            unbindService(mConnection);
                            mBound = false;
                        }
                        check = false;
                        mService.removeLocationUpdates();
                        Toast.makeText(view.getContext(), "Service not bound",
                        Toast.LENGTH_SHORT).show();
                    }
            }
        });

        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(Utils.requestingLocationUpdates(this));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
        Context.BIND_AUTO_CREATE);

    }



@Override
protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
        new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
        }

@Override
protected void onPause() {
    super.onPause();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    // unregisterReceiver(mReceiver);

}

@Override
protected void onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
        .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
        }

/**
 * Receiver for broadcasts sent by {@link LocationUpdatesService}.
 */
private class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
        String finalMessage = intent.getStringExtra(LocationUpdatesService.EXTRA_STRING);
        Log.e(TAG, finalMessage);
        if (finalMessage != null) {
                /* Toast.makeText(MessagesActivity.this, Utils.getLocationText(location),
                        Toast.LENGTH_SHORT).show(); */
            Toast.makeText(MessagesActivity.this,
                    finalMessage, Toast.LENGTH_SHORT).show();

        }
    }
}




    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            // mRequestLocationUpdatesButton.setEnabled(false);
        } else {
            mRequestLocationUpdatesButton.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_signout) {
            startActivity(new Intent(MessagesActivity.this, MainActivity.class)); //TODO destroy class after user sign out
        }

        return super.onOptionsItemSelected(item);
    }

/**
 * A placeholder fragment containing a simple view.
 */
public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                ProfilesFragment profilesFragment = new ProfilesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putInt("sessionID", sessionID);
                profilesFragment.setArguments(bundle);
                return profilesFragment;
            case 1:
                // Games fragment activity
                MessagesMenu whatthefuck = new MessagesMenu();
                return whatthefuck;
            case 2:
                Locations locations = new Locations();
                return locations;
        }
        return PlaceholderFragment.newInstance(position + 1);
        // return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "Messages";
            case 2:
                return "Locations";
        }
        return null;
    }
}

	/*
	 * Listeners associated to buttons
	 */

            private View.OnClickListener listenerInRangeButton = new View.OnClickListener() {
                public void onClick(View v){
                    if (mBound) {
                        mManager.requestPeers(mChannel, MessagesActivity.this);
                    } else {
                        Toast.makeText(v.getContext(), "Service not bound",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            };

            	/*
	 * Termite listeners
	 */

            @Override
            public void onPeersAvailable(SimWifiP2pDeviceList peers) {
                StringBuilder peersStr = new StringBuilder();

                // compile list of devices in range
                for (SimWifiP2pDevice device : peers.getDeviceList()) {
                    String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
                    peersStr.append(devstr);
                }

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("peerList", String.valueOf(peersStr));
                editor.commit();

                // display list of devices in range
                new AlertDialog.Builder(this)
                        .setTitle("Devices in WiFi Range")
                        .setMessage(peersStr.toString())
                        .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
            private void guiSetButtonListeners() {
                findViewById(R.id.idInRangeButton).setOnClickListener(listenerInRangeButton);
            }

            private void guiUpdateInitState() {
                findViewById(R.id.idInRangeButton).setEnabled(true);
            }
}
