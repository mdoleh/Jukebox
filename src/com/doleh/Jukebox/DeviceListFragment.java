package com.doleh.Jukebox;

import android.app.ListFragment;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;

public class DeviceListFragment extends ListFragment
{
    private List peers = new ArrayList();

    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            // Out with the old, in with the new.
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.
            ((WiFiPeerListAdapter)getListAdapter()).notifyDataSetChanged();
            if (peers.size() == 0) {
                println("No devices found");
                return;
            }
        }
    };

    private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice>
    {
        private List<WifiP2pDevice> items;

        public WiFiPeerListAdapter(Context context, int textViewResourceId,
                                   List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;
        }
    }
}
