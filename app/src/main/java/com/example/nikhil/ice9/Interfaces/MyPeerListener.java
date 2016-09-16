package com.example.nikhil.ice9.Interfaces;

import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.nikhil.ice9.Fragments.DeviceListFragment;
import com.example.nikhil.ice9.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 30-07-2016.
 */
public class MyPeerListener implements WifiP2pManager.PeerListListener {
    public static List peerList = new ArrayList();

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        // Out with the old, in with the new.
        peerList.clear();
        peerList.addAll(peers.getDeviceList());
        //DeviceListFragment.peerListAdapter.notifyDataSetChanged();



        Log.d(HomeActivity.TAG, "onPeersAvailable: " + peerList);


        if (peerList.size() == 0) {
            Log.d(HomeActivity.TAG, "No devices found");
            return;
        }
    }
}
