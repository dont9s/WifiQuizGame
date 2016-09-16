package com.example.nikhil.ice9.Fragments;

import android.app.Fragment;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nikhil.ice9.Adapters.ListFragmentAdapter;
import com.example.nikhil.ice9.HomeActivity;
import com.example.nikhil.ice9.Interfaces.DeviceItemClickListener;
import com.example.nikhil.ice9.Interfaces.MyPeerListener;
import com.example.nikhil.ice9.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 30-07-2016.
 */
public class DeviceListFragment extends Fragment implements WifiP2pManager.PeerListListener, DeviceItemClickListener {
    private View deviceListFragmentView;
    private static final String TAG = "DeviceListFrament";
    private ListFragmentAdapter peerListAdapter;
    private RecyclerView device_list;
    private ArrayList<WifiP2pDevice> peerList = new ArrayList<WifiP2pDevice>();
    private RecyclerView.LayoutManager mLayoutManager;
    private WifiP2pDevice clickedDevice;
    private ProgressBar loadingBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        deviceListFragmentView = inflater.inflate(R.layout.device_list_fragment , container , false);

        initialize();
        return deviceListFragmentView;
    }
    private void initialize(){
        loadingBar = (ProgressBar) deviceListFragmentView.findViewById(R.id.loading);
        loadingBar.setVisibility(View.GONE);
        peerListAdapter = new ListFragmentAdapter(peerList , this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        device_list = (RecyclerView) deviceListFragmentView.findViewById(R.id.device_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        device_list.setHasFixedSize(true);

        device_list.setLayoutManager(mLayoutManager);
        device_list.setItemAnimator(new DefaultItemAnimator());
        device_list.setAdapter(peerListAdapter);


    }
    private void setLoadingBarVisibility(boolean isVisible){

    }



    public void onInitiateDiscovery(){
        if(loadingBar.getVisibility() == View.GONE){
            loadingBar.setVisibility(View.VISIBLE);
            /*if(device_list.getVisibility() == View.VISIBLE){
                device_list.setVisibility(View.GONE);
            }*/
        }
    }
    public void clearPeers(){
        peerList.clear();
        peerListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

        // Out with the old, in with the new.
        peerList.clear();
        peerList.addAll(peers.getDeviceList());

        if(loadingBar.getVisibility() == View.VISIBLE){
            loadingBar.setVisibility(View.GONE);
            /*if(device_list.getVisibility() == View.GONE){
                device_list.setVisibility(View.VISIBLE);
            }*/
        }
        peerListAdapter.notifyDataSetChanged();

        ((DeviceActionListener)getActivity()).discoverPeers();
        if(!peerList.contains(clickedDevice)){
            ((DeviceActionListener)getActivity()).hideDetails();

        }


        Log.d(TAG, "onPeersAvailable: " + peerList);


        if (peerList.size() == 0) {

            Toast.makeText(getActivity() , R.string.no_devices_found , Toast.LENGTH_LONG).show();
            Log.d(HomeActivity.TAG, "No devices found");
            return;
        }

    }

    @Override
    public void itemClickListener(View v, int position) {
        Log.d(TAG, "itemClickListener: "+ peerList.get(position).deviceAddress);
        Toast.makeText(getActivity() ,peerList.get(position).deviceAddress , Toast.LENGTH_LONG ).show();
     /*   WifiP2pDevice deviceToConnect = peerList.get(position);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = deviceToConnect.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        ((DeviceActionListener)getActivity()).connect(config);

*/
        clickedDevice = peerList.get(position);
        WifiP2pDevice deviceToShowDetails = peerList.get(position);
        ((DeviceActionListener)getActivity()).showDetails(deviceToShowDetails);


    }

    /**
     * An interface-callback for the activity to listen to fragment interaction
     * events.
     */
    public interface DeviceActionListener {

        void showDetails(WifiP2pDevice device);
        void hideDetails();
        void cancelDisconnect();

        void connect(WifiP2pConfig config);

        void discoverPeers();
        void disconnect();
    }
}
