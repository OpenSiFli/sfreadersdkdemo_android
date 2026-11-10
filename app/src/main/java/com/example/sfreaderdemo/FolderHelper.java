package com.example.sfreaderdemo;

import android.content.Context;

import java.io.File;

/**
 * @author hecq
 * @email 33912760@qq.com
 * create at 2023/9/14
 * description
 */
public class FolderHelper {
    public static String getDeviceLogPath(Context context){
        String root = context.getExternalFilesDir(null) + "/DeviceLogs";
        File file = new File(root);
        file.mkdirs();
        return root;
    }

    public static String getDeviceAssetPath(Context context){
        String root = context.getExternalFilesDir(null) + "/DeviceAssets";
        File file = new File(root);
        file.mkdirs();
        return root;
    }

    public static String getDeviceMetricsPath(Context context){
        String root = context.getExternalFilesDir(null) + "/DeviceMetrics";
        File file = new File(root);
        file.mkdirs();
        return root;
    }

    public static String getDeviceHCIPath(Context context){
        String root = context.getExternalFilesDir(null) + "/DeviceHCI";
        File file = new File(root);
        file.mkdirs();
        return root;
    }

    public static String getDeviceAudioDumpPath(Context context){
        String root = context.getExternalFilesDir(null) + "/DeviceAudioDump";
        File file = new File(root);
        file.mkdirs();
        return root;
    }
}
