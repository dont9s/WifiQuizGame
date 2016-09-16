package com.example.nikhil.ice9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.nikhil.ice9.Adapters.ServiceListFragmentAdapter;
import com.example.nikhil.ice9.Containers.WifiP2pService;
import com.example.nikhil.ice9.Fragments.WifiDirectServiceList;
import com.example.nikhil.ice9.Receivers.WifiDirectBroadcastReceiver;

import java.util.HashMap;
import java.util.Map;

public class FirstActivity extends AppCompatActivity implements WifiDirectServiceList.ServiceListFirstActivityInteraction {

    public static final String TAG = "FirstActivity";
    private TextView statusText;

    //TXT RECORD properties
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "quiz_game";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";
    public static final String BUDDY_NAME = "buddy_name";
    public static final String BASE_BUDDY_NAME = "Buddy";
    public static final String SERVICE_LIST_FRAGMENT_TAG = "services";

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private WifiP2pDnsSdServiceRequest mServiceRequest;
    //this hasmap is for storing incoming buddy name and populating in the list view
    final HashMap<String , String> buddies = new HashMap<>();
    private boolean isWifiP2pEnabled;
    private WifiDirectServiceList mWifiDirectServiceListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        setupIntentFilter();

        initialize();


        startServiceRegistrationAndDiscovery();
        mWifiDirectServiceListFragment = new WifiDirectServiceList();
        getFragmentManager().beginTransaction().
                add(R.id.container_service_list_fragment , mWifiDirectServiceListFragment , SERVICE_LIST_FRAGMENT_TAG).commit();

    }
    private void initialize(){
        statusText  = (TextView) findViewById(R.id.status_text);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

    }
    private void startServiceRegistrationAndDiscovery(){
        //  Create a string map containing information about your service.
        Map<String , String> record = new HashMap();
        record.put(TXTRECORD_PROP_AVAILABLE , "visible");
        record.put(BUDDY_NAME ,BASE_BUDDY_NAME + (int)(Math.random()*1000)   );
        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance(SERVICE_INSTANCE, SERVICE_REG_TYPE, record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
                appendStatus(getResources().getString(R.string.added_local_service));
            }

            @Override
            public void onFailure(int arg0) {

                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                appendStatus (getResources().getString(R.string.failed_aading_local_service));
            }
        });
        discoverService();
    }

    private void discoverService(){
        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */


        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
        /* Callback includes:
         * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         */

            public void onDnsSdTxtRecordAvailable(
                    String fullDomain, Map record, WifiP2pDevice device) {
                buddies.put(device.deviceAddress , record.get(BUDDY_NAME).toString());
                Log.d(TAG,
                        device.deviceName + " is "
                                + record.get(TXTRECORD_PROP_AVAILABLE));
            }
        };

        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType) {
                Log.d(TAG, "onDnsSdServiceAvailable: " + registrationType);

                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                // A service has been discovered. Is this our app?


                if(instanceName.equalsIgnoreCase(SERVICE_INSTANCE)){
                    // Update the device name with the human-friendly version from
                    // the DnsTxtRecord, assuming one arrived

                    resourceType.deviceName = buddies.containsKey(resourceType.deviceAddress) ?
                            buddies.get(resourceType.deviceAddress):
                            resourceType.deviceName;
                    //this would change the device name from the actual device
                    // update the UI and add the item the discovered
                    // device.
                    WifiDirectServiceList serviceListFragment = (WifiDirectServiceList) getFragmentManager()
                            .findFragmentByTag(SERVICE_LIST_FRAGMENT_TAG);
                    if(serviceListFragment != null){
                        ServiceListFragmentAdapter serviceListFragmentAdapter = serviceListFragment.buddyNameListAdapter;
                        WifiP2pService service = new WifiP2pService();
                        service.device = resourceType;
                        service.instanceName = instanceName;
                        service.serviceRegistrationType = registrationType;
                        serviceListFragmentAdapter.add(service);
                        serviceListFragmentAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onBonjourServiceAvailable "
                                + instanceName);
                    }
                }

            }
        };

        mManager.setDnsSdResponseListeners(mChannel , servListener , txtListener);
        // After attaching listeners, create a service request and initiate
        // discovery.
        mServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.addServiceRequest(mChannel,
                mServiceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        // Success!
                        appendStatus(getResources().getString(R.string.added_service_discovery_request));
                    }

                    @Override
                    public void onFailure(int code) {
                        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                        appendStatus(getResources().getString(R.string.failed_adding_discovery_request));
                    }
                });
        mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus(getResources().getString(R.string.service_discovery_initiatetd));
            }
            @Override
            public void onFailure(int arg0) {
                appendStatus(getResources().getString(R.string.service_discovery_failed));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WifiDirectBroadcastReceiver(mManager , mChannel , this);
        registerReceiver(mReceiver , intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void setupIntentFilter (){
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        //Indicate if p2p discovery is running or stopped
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);


        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


    }

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
    private void appendStatus(String status){
        String currentStatus = statusText.getText().toString();
        statusText.setText(currentStatus + "\n" + status);

    }

    @Override
    public void connectP2p(WifiP2pService wifiP2pService) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pService.device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        if (mServiceRequest != null)
            mManager.removeServiceRequest(mChannel, mServiceRequest,
                    new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            appendStatus(getResources().getString(R.string.succesfully_removed_service_request));
                        }
                        @Override
                        public void onFailure(int arg0) {
                            Log.d(TAG, "onFailure: ErrorCode : " + arg0);
                            appendStatus(getResources().getString(R.string.failed_removing_service_request));
                        }
                    });
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus(getResources().getString(R.string.connecting_to_service));
            }
            @Override
            public void onFailure(int errorCode) {
                appendStatus(getResources().getString(R.string.failed_to_connect));
            }
        });
    }
}
