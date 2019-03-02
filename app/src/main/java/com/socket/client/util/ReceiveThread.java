package com.socket.client.util;

import android.util.Log;

import java.io.InputStream;
import java.net.Socket;

public class ReceiveThread extends Thread{

    public static String TAG = "MyClientActivity";

    public static final int PACKET_HEAD_LENGTH=4;//包头长度
    private Socket socket;
    private volatile byte[] bytes = new byte[0];

    public ReceiveThread(Socket socket) {
        this.socket = socket;
    }

    public byte[] mergebyte(byte[] a, byte[] b, int begin, int end) {
        byte[] add = new byte[a.length + end - begin];
        int i = 0;
        for (i = 0; i < a.length; i++) {
            add[i] = a[i];
        }
        for (int k = begin; k < end; k++, i++) {
            add[i] = b[k];
        }
        return add;
    }

    /*//用String传送数据长度  粘包处理方式
    @Override
    public void run() {
        int count =0;
        while (true) {
            try {
                InputStream reader = socket.getInputStream();
                if (bytes.length < PACKET_HEAD_LENGTH) {
                    byte[] head = new byte[PACKET_HEAD_LENGTH - bytes.length];
                    int couter = reader.read(head);
                    if (couter < 0) {
                        continue;
                    }
                    bytes = mergebyte(bytes, head, 0, couter);
                    if (couter < PACKET_HEAD_LENGTH) {
                        continue;
                    }
                }
                // 下面这个值请注意，一定要取2长度的字节子数组作为报文长度，你懂得
                byte[] temp = new byte[0];
                temp = mergebyte(temp, bytes, 0, PACKET_HEAD_LENGTH);
                String templength = new String(temp);
                int bodylength = Integer.parseInt(templength);//包体长度
                if (bytes.length - PACKET_HEAD_LENGTH < bodylength) {//不够一个包
                    byte[] body = new byte[bodylength + PACKET_HEAD_LENGTH - bytes.length];//剩下应该读的字节(凑一个包)
                    int couter = reader.read(body);
                    if (couter < 0) {
                        continue;
                    }
                    bytes = mergebyte(bytes, body, 0, couter);
                    if (couter < body.length) {
                        continue;
                    }
                }
                byte[] body = new byte[0];
                body = mergebyte(body, bytes, PACKET_HEAD_LENGTH, bytes.length);
                count++;
                Log.d(TAG, "server receive body:  " + count+"     "+new String(body));
                bytes = new byte[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/



    //用int传送数据长度 拆包方法
    @Override
    public void run() {
        int count =0;
        while (true) {
            try {
                InputStream reader = socket.getInputStream();
                if (bytes.length < PACKET_HEAD_LENGTH) {
                    byte[] head = new byte[PACKET_HEAD_LENGTH - bytes.length];
                    int couter = reader.read(head);
                    if (couter < 0) {
                        continue;
                    }
                    bytes = mergebyte(bytes, head, 0, couter);
                    if (couter < PACKET_HEAD_LENGTH) {
                        continue;
                    }
                }
                // 下面这个值请注意，一定要取4长度的字节子数组作为报文长度，你懂得
                byte[] temp = new byte[0];
                temp = mergebyte(temp, bytes, 0, PACKET_HEAD_LENGTH);
                int bodylength = DataDealWithUtil.Byte2Int(temp);
                Log.d(TAG, "receviePacket: 头部内容长度是：" + bodylength);
                if (bytes.length - PACKET_HEAD_LENGTH < bodylength) {//不够一个包
                    byte[] body = new byte[bodylength + PACKET_HEAD_LENGTH - bytes.length];//剩下应该读的字节(凑一个包)
                    int couter = reader.read(body);
                    if (couter < 0) {
                        continue;
                    }
                    bytes = mergebyte(bytes, body, 0, couter);
                    if (couter < body.length) {
                        continue;
                    }
                }
                byte[] body = new byte[0];
                body = mergebyte(body, bytes, PACKET_HEAD_LENGTH, bytes.length);
                count++;
                Log.d(TAG, "server receive body:  " + count+"     "+new String(body));
                bytes = new byte[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}