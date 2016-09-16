package com.example.nikhil.ice9.Adapters;

import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nikhil.ice9.Containers.WifiP2pService;
import com.example.nikhil.ice9.Interfaces.ServiceListItemClickListener;
import com.example.nikhil.ice9.R;

import java.util.ArrayList;

/**
 * Created by Nikhil on 09-09-2016.
 */
public class ServiceListFragmentAdapter extends RecyclerView.Adapter<ServiceListFragmentAdapter.MyViewHolder> {


    public static final String TAG = "ServiceListAdapter";
    public static final String CONNECTED = "Connected" , INVITED = "Invited" , FAILED = "Failed" , AVAILABLE = "Available"
            , UNAVAILABLE = "Unavailable" , UNKNOWN = "Unknown";
    private ServiceListItemClickListener listener;
    private ArrayList<WifiP2pService> serviceNameList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView buddy_name;

        public MyViewHolder(View view) {

            super(view);
            Log.d(TAG, "MyViewHolder: ");
            buddy_name = (TextView) view.findViewById(R.id.buddy_name);
            view.setTag(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.itemClickListener(v , getAdapterPosition());
        }
    }

    public void add(WifiP2pService service){
        serviceNameList.add(service);

    }

    public ServiceListFragmentAdapter(ArrayList<WifiP2pService> serviceNameList , ServiceListItemClickListener listener) {
        this.listener = listener;
        this.serviceNameList = serviceNameList;
    }
    @Override
    public ServiceListFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_list_fragment_item, parent, false);

        Log.d(TAG, "onCreateViewHolder: ");
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ServiceListFragmentAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //get an object WifiP2pDevice from the list
        // and call access deviceName constant for name of the device
        holder.buddy_name.setText(serviceNameList.get(position).device.deviceName
         + " - " +  getDeviceStatus(serviceNameList.get(position).device.status));
        Log.d(TAG, "onBindViewHolder: ");
    }
    public static String getDeviceStatus(int statusCode) {
        switch (statusCode) {
            case WifiP2pDevice.CONNECTED:
                return CONNECTED;
            case WifiP2pDevice.INVITED:
                return INVITED;
            case WifiP2pDevice.FAILED:
                return FAILED;
            case WifiP2pDevice.AVAILABLE:
                return AVAILABLE;
            case WifiP2pDevice.UNAVAILABLE:
                return UNAVAILABLE;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return serviceNameList.size();
    }
}
