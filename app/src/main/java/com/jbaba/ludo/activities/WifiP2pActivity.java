package com.jbaba.ludo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jbaba.ludo.R;
import com.jbaba.ludo.concreteclasses.SendReceive;
import com.jbaba.ludo.concreteclasses.WifiDirectBroadcastReceiver;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WifiP2pActivity extends AppCompatActivity {

    private TextView wifiState;
    private TextView searchingTextView;
    private ListView availableWifi;

    public static WifiManager wifiManager;
    private static WifiP2pManager wifiP2pManager;
    private static WifiP2pManager.Channel channel;

    private WifiDirectBroadcastReceiver wifiDirectBroadCaster;
    private IntentFilter intentFilter;

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private String[] deviceName;
    private WifiP2pDevice[] deviceList;

    public static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 10;
    private static SendReceive sendReceive;

    public static boolean wifiOnByActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_wifi_p2p);


        initComponent();
        displayLocationSettingsRequest(this, 1);
        findPeers();
        listViewclicked();
    }

    private void initComponent() {
        wifiState = (TextView) findViewById(R.id.wifi_state);
        searchingTextView = (TextView) findViewById(R.id.searching_textview);
        availableWifi = (ListView) findViewById(R.id.wifi_list);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        switchWifiOn();
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);

        wifiDirectBroadCaster = new WifiDirectBroadcastReceiver(wifiP2pManager, channel, this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceName = new String[peerList.getDeviceList().size()];
                deviceList = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()) {
                    deviceName[index] = device.deviceName;
                    deviceList[index] = device;
                    index++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceName);
                availableWifi.setAdapter(adapter);
            }
            if (peers.size() == 0)
                Toast.makeText(getApplicationContext(), "No device found", Toast.LENGTH_SHORT).show();
        }
    };

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerInfo = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                Log.d("Debugging", "I enterred host");
                Toast.makeText(getApplicationContext(), "You are Host", Toast.LENGTH_SHORT).show();
                Thread thread = new Thread(new Server());
                thread.start();
                Intent intent = new Intent(WifiP2pActivity.this, HouseChoiceMultiplayerActivity.class);
                intent.putExtra(HouseChoiceMultiplayerActivity.HOSTORCLIENT, "Host");
                startActivity(intent);
            } else if (wifiP2pInfo.groupFormed) {
                Log.d("Debugging", "I enterred client");
                Toast.makeText(getApplicationContext(), "You are Client", Toast.LENGTH_SHORT).show();
                Thread thread = new Thread(new Client(groupOwnerInfo));
                thread.start();
                Intent intent = new Intent(WifiP2pActivity.this, HouseChoiceMultiplayerActivity.class);
                intent.putExtra(HouseChoiceMultiplayerActivity.HOSTORCLIENT, "Client");
                startActivity(intent);
            }
        }
    };

    public void displayLocationSettingsRequest(final Context context, final int REQUEST_CHECK_SETTINGS) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                LocationSettingsResponse response = null;
                try {
                    response = task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    exception.printStackTrace();

                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(WifiP2pActivity.this, REQUEST_CHECK_SETTINGS);
                                break;
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }

            }
        });
    }

    public void switchWifiOn() {
        if (wifiManager.isWifiEnabled())
            wifiState.setText("Wifi is ON");
        else {
            wifiManager.setWifiEnabled(true);
            setWifiOnByActivity(true);
            wifiState.setText("Wifi is ON");
        }
    }

    public void listViewclicked() {
        availableWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device = deviceList[i];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void findPeers() {
        sleepThread();
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                searchingTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(getApplicationContext(), "Problem trying to find wifi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(wifiDirectBroadCaster, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(wifiDirectBroadCaster);
    }


    public class Server implements Runnable {
        private Socket socket;

        @Override
        public void run() {
            try {
                Log.d("Debugging", "Am in server run");
                ServerSocket serverSocket = new ServerSocket(9772);
                socket = serverSocket.accept();
                setSendReceive(new SendReceive(socket));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class Client implements Runnable {
        private Socket socket;
        private String hostAddress;

        public Client(InetAddress hostAddress) {
            this.hostAddress = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAddress, 9772), 500);
                setSendReceive(new SendReceive(socket));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    public static void disconnect() {
        if (wifiP2pManager != null && channel != null) {
            wifiP2pManager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && wifiP2pManager != null && channel != null) {
                        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(int i) {
                            }
                        });
                    }
                }
            });
        }
    }

    public void sleepThread()
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        disconnect();
        if(isWifiOnByActivity())
            wifiManager.setWifiEnabled(false);

        Intent intent = new Intent(this, GameTypeActivity.class);
        startActivity(intent);
    }

    public static SendReceive getSendReceive() {
        return sendReceive;
    }

    public static void setSendReceive(SendReceive sendReceiv) {
        sendReceive = sendReceiv;
    }

    public boolean isWifiOnByActivity() {
        return wifiOnByActivity;
    }

    public void setWifiOnByActivity(boolean wifiOnByActivity) {
        this.wifiOnByActivity = wifiOnByActivity;
    }
}