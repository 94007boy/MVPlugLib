package com.djjie.mvpluglib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shf2 on 2016/12/17.
 */

public class MVPlug {

    private static MVPlug sInstance;
    private Object mInitLock = new Object();

    public MVPlugConfig getConfiguration() {
        return configuration;
    }

    private MVPlugConfig configuration;

    private MVPlug(){

    }

    public static MVPlug getInstance() {
        if (sInstance == null) {
            synchronized (MVPlug.class) {
                if (sInstance == null) {
                    sInstance = new MVPlug();
                }
            }
        }
        return sInstance;
    }

    public void init(MVPlugConfig configuration) {
        if (configuration == null) {
            throw new IllegalStateException("requires the field of configuration to be non-null !");
        }
        synchronized (mInitLock) {
            this.configuration = configuration;
        }
    }

    /**
     * 检测网络是否连接
     */
    public static boolean isNetConnected(Context context) {
        if (context == null)return true;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
