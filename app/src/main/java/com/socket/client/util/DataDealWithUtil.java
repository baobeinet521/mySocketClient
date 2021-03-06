package com.socket.client.util;

public class DataDealWithUtil {
    /**
     * byte数组转int类型的对象
     * @param bytes
     * @return
     */
    public static int Byte2Int(byte[] bytes) {
        return (bytes[0]&0xff)<<24
                | (bytes[1]&0xff)<<16
                | (bytes[2]&0xff)<<8
                | (bytes[3]&0xff);
    }


    /**
     * int转byte数组
     * @param num
     * @return
     */
    public static byte[] IntToByte(int num){
        byte[]bytes=new byte[4];
        bytes[0]=(byte) ((num>>24)&0xff);
        bytes[1]=(byte) ((num>>16)&0xff);
        bytes[2]=(byte) ((num>>8)&0xff);
        bytes[3]=(byte) (num&0xff);
        return bytes;
    }
}
