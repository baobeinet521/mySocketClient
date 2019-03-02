package com.socket.client.util;

import android.util.Log;

import java.nio.ByteOrder;

public class DeviceUtils {

    public static String TAG="DeviceUtils";
    public static void judgeByteOrder() {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            System.out.println("BIG_ENDIAN");
            Log.d(TAG, "judgeByteOrder: " + "BIG_ENDIAN");
        } else {
            System.out.println("LITTLE_ENDIAN");
            Log.d(TAG, "judgeByteOrder: " + "LITTLE_ENDIAN");
        }
    }
}
