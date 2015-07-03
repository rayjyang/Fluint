package eia.fluint;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class ConnectionDetector {

    private Context mContext;

    public ConnectionDetector(Context context) {
        mContext = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

}
