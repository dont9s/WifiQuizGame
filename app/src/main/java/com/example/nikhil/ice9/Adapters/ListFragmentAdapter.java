package com.example.nikhil.ice9.Adapters;

import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nikhil.ice9.Interfaces.DeviceItemClickListener;
import com.example.nikhil.ice9.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 30-07-2016.
 */
public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.MyViewHolder> {

    private DeviceItemClickListener listener;
    private static final String TAG = "ListFragmentAdapter";
    ArrayList<WifiP2pDevice> deviceNameList;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView device_name;

        public MyViewHolder(View view) {
            
            super(view);
            Log.d(TAG, "MyViewHolder: ");
            device_name = (TextView) view.findViewById(R.id.device_name);
            view.setTag(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.itemClickListener(v , getAdapterPosition());
        }
    }

    public ListFragmentAdapter(ArrayList<WifiP2pDevice> deviceNameList , DeviceItemClickListener listener) {
        this.listener = listener;
        this.deviceNameList = deviceNameList;
    }

    @Override
    public ListFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_fragment_item, parent, false);

        Log.d(TAG, "onCreateViewHolder: ");
        return new MyViewHolder(itemView);
        
    }

    @Override
    public void onBindViewHolder(ListFragmentAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //get an object WifiP2pDevice from the list
        // and call access deviceName constant for name of the device
        holder.device_name.setText(deviceNameList.get(position).deviceName);
        Log.d(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return deviceNameList.size();
    }
}
