package com.doleh.Jukebox.Static;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Utils
{
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Email.sendErrorReport(ex);
        }
        return "";
    }

    public static String millisecondsToTime(long milliseconds)
    {
        long minutes = (int)((float)milliseconds / 1000 / 60);
        long seconds = (int)((((float)milliseconds / 1000 / 60) - minutes) * 60);

        String secondsString = Long.toString(seconds);
        if (seconds < 10) { secondsString = "0" + secondsString; }
        return Long.toString(minutes) + ":" + secondsString;
    }

    public static boolean isOnWifi(Activity activity)
    {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi != null && mWifi.isConnected();
    }

    public static boolean isOnEthernet(Activity activity)
    {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo mEthernet = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        return mEthernet != null && mEthernet.isConnected();
    }

    public static boolean is3G(Activity activity)
    {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return m3G != null && m3G.isConnected();
    }

    public static void closeApplication()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
