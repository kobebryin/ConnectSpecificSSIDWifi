package com.example.develop.connectspecificssidwifi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    String networkSSID = "iii";   //Wifi's SSID
    String networkPass = "00000000";    //Wifi's password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check Wifi permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(true) Log.d(TAG, "User grant WIFI permission");
                    connectWIFI();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if(true)   Log.d(TAG, "User DO NOT grant WIFI permission");
                }

                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void connectWIFI(){
        Log.d(TAG, "connectWIFI start.");
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        //for WEP network you need to do this:
        //conf.wepKeys[0] = networkPass;
        //conf.wepTxKeyIndex = 0;
        //conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        //conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        //For WPA network you need to add passphrase like this:
        conf.preSharedKey = "\""+ networkPass +"\"";

        // For Open network you need to do this:
        //conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            Log.d(TAG, "WIFI ssID = " + i.SSID);
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                Log.d(TAG, "Found ssID = " + i.SSID);
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                Log.d(TAG, "Found networkId = " + i.networkId);
                wifiManager.reconnect();
                break;
            }
        }
    }
}
