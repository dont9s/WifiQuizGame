package com.example.nikhil.ice9.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nikhil.ice9.R;

/**
 * Created by Nikhil on 14-08-2016.
 */
public class DeviceDetailFragment extends Fragment implements View.OnClickListener , WifiP2pManager.ConnectionInfoListener {
    private View deviceDetailView;
    private WifiP2pInfo  info;
    private Button connect_button;
    private TextView device_detail_text , groupOwnerCheck , inetAddressText , someOtherInfoText;
    private View connectProgressDialog;
    private WifiP2pDevice device;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        deviceDetailView = inflater.inflate(R.layout.device_detail_fragment , container , false);

        initialize();

        return deviceDetailView;
    }

    private  void initialize(){
        connectProgressDialog =  deviceDetailView.findViewById(R.id.device_detail_connect_progress);
        groupOwnerCheck = (TextView) deviceDetailView.findViewById(R.id.groupOwnerCheck);
        inetAddressText = (TextView) deviceDetailView.findViewById(R.id.inetAddressText);
        someOtherInfoText = (TextView) deviceDetailView.findViewById(R.id.someOtherInfoText);
        device_detail_text = (TextView) deviceDetailView.findViewById(R.id.device_detail_text);
        connect_button = (Button) deviceDetailView.findViewById(R.id.connect_button);
        connect_button.setOnClickListener(this);

    }

    public void hideDetails(){
        this.getView().setVisibility(View.GONE);
        device_detail_text.setText("");


    }
    public void showDetails(WifiP2pDevice device){

        this.device = device;

        this.getView().setVisibility(View.VISIBLE);
        device_detail_text.setText(device.deviceName);


    }

    public void resetView(){


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_button:
                if(connectProgressDialog.getVisibility() == View.VISIBLE){
                    return;
                }
                connectProgressDialog.setVisibility(View.VISIBLE);

                WifiP2pDevice deviceToConnect = this.device;
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = deviceToConnect.deviceAddress;
                config.wps.setup = WpsInfo.PBC;

                ((DeviceListFragment.DeviceActionListener) getActivity()).connect(config);
                break;

        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if(connectProgressDialog.getVisibility() == View.VISIBLE){
            connectProgressDialog.setVisibility(View.GONE);
        }
        this.info = info;
        this.getView().setVisibility(View.VISIBLE);

        // The owner IP is now known.
        groupOwnerCheck.setText(getResources().getString(R.string.group_owner_text)
                + ((info.isGroupOwner == true) ? getResources().getString(R.string.yes)
                : getResources().getString(R.string.no)));

        // InetAddress from WifiP2pInfo struct.
        inetAddressText.setText(getResources().getString(R.string.group_owner_ip_text) + info.groupOwnerAddress);



        // hide the connect button
        connect_button.setVisibility(View.GONE);


    }
}
