package com.example.nikhil.ice9.Fragments;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nikhil.ice9.Adapters.ServiceListFragmentAdapter;
import com.example.nikhil.ice9.Containers.WifiP2pService;
import com.example.nikhil.ice9.Interfaces.ServiceListItemClickListener;
import com.example.nikhil.ice9.R;

import java.util.ArrayList;

/**
 * Created by Nikhil on 09-09-2016.
 */
public class WifiDirectServiceList extends Fragment implements ServiceListItemClickListener {
    private View serviceListFragmentView;
    public ServiceListFragmentAdapter buddyNameListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView serviceList;
    private ArrayList<WifiP2pService> serviceNameDataSet = new ArrayList<WifiP2pService>();
    public static final String TAG = "WifiDirectServiceList";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        serviceListFragmentView = inflater.inflate(R.layout.wifi_direct_service_list ,container , false );
        initialize();
        return serviceListFragmentView;
    }
    private void initialize(){
        buddyNameListAdapter = new ServiceListFragmentAdapter(serviceNameDataSet ,  this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        serviceList = (RecyclerView) serviceListFragmentView.findViewById(R.id.service_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        serviceList.setHasFixedSize(true);

        serviceList.setLayoutManager(mLayoutManager);
        serviceList.setItemAnimator(new DefaultItemAnimator());
        serviceList.setAdapter(buddyNameListAdapter);



    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public interface  ServiceListFirstActivityInteraction{
        public void connectP2p(WifiP2pService wifiP2pService);
    }
    @Override
    public void itemClickListener(View v, int position) {
        ((ServiceListFirstActivityInteraction)getActivity()).connectP2p(serviceNameDataSet.get(position));
        ((TextView)v).setText(serviceNameDataSet.get(position).device.deviceName
                + " - " +  "Connecting");
        /*((TextView)v.findViewById(R.id.buddy_name)).setText(serviceNameDataSet.get(position).device.deviceName
                + " - " +  "Connecting");*/
    }
}
