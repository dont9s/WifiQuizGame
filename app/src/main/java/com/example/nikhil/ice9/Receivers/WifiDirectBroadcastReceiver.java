package com.example.nikhil.ice9.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.nikhil.ice9.FirstActivity;
import com.example.nikhil.ice9.Fragments.DeviceDetailFragment;
import com.example.nikhil.ice9.HomeActivity;
import com.example.nikhil.ice9.Interfaces.MyPeerListener;
import com.example.nikhil.ice9.R;

/**
 * Created by Nikhil on 29-07-2016.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "WifiDirectBroadcast";
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private FirstActivity activity;


    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       FirstActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.d(TAG, "onReceive:  + WifiP2PEnabled" );
                activity.setIsWifiP2pEnabled(true);
            } else {
                Log.d(TAG, "onReceive:  + WifiP2PDisabled" );
                activity.setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()

/*
            if (manager != null) {
                manager.requestPeers(channel, (WifiP2pManager.PeerListListener)
                        activity.getFragmentManager().findFragmentById(R.id.list_fragment));
            }
            Log.d(HomeActivity.TAG, "P2P peers changed");
*/
            Log.d(TAG, "onReceive: new WifiP2pService is discovered and its handling is not done in " +
                    "BroadcastReceiver but in FirstActivity in onDnsSdServiceAvailable()" );

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {


            // Connection state changed!  We should probably do something about
            // that.

            Log.d(TAG, "onReceive: " + "Connection Status changed");
            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP

                DeviceDetailFragment fragment = (DeviceDetailFragment) activity
                        .getFragmentManager().findFragmentById(R.id.frag_detail);
                manager.requestConnectionInfo(channel, (WifiP2pManager.ConnectionInfoListener) fragment);
            } else {
                // It's a disconnect
                //activity.resetData();
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
           /* DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
                    */

        }



    }
}
