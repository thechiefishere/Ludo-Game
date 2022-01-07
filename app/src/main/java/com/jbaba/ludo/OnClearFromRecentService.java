package com.jbaba.ludo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jbaba.ludo.activities.WifiP2pActivity;

public class OnClearFromRecentService extends Service {
    public OnClearFromRecentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return  null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Log.d("ClearFromRecentService", "END");
        if(WifiP2pActivity.getSendReceive() != null)
        {
            try {
                WifiP2pActivity.getSendReceive().getSocket().close();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            WifiP2pActivity.disconnect();
        }
        if(WifiP2pActivity.wifiOnByActivity)
            WifiP2pActivity.wifiManager.setWifiEnabled(false);
        stopSelf();
    }
}
