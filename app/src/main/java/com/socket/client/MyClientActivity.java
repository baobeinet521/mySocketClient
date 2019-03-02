package com.socket.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socket.client.util.ReceiveThread;
import com.socket.client.util.WifiInfoUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyClientActivity extends AppCompatActivity {

    private static final String TAG = "MyClientActivity";
    private Socket socket;
    private TextView mWifiName;
    private TextView mIpAddress;
    private EditText mSendMessageEdit;
    private boolean mIsWifiEnable;
    private EditText ipEdt;
    private Button mSendBtn;
    private Button mStartConnectBtn;
    private String ipStr;
    private TextView mReceveMesFromServicerText;

    private String mIpStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mWifiName = findViewById(R.id.wifi_name);
        mIpAddress = findViewById(R.id.ip_address);
        mSendMessageEdit = findViewById(R.id.message_edit);
        mIsWifiEnable = WifiInfoUtil.isWifiEnabled(this);
        mSendBtn = findViewById(R.id.send_message_toservice_btn);
        ipEdt = findViewById(R.id.ip_server);
        mStartConnectBtn = findViewById(R.id.start_connect_btn);
        mReceveMesFromServicerText = findViewById(R.id.receve_message_from_service__text);

        if (mIsWifiEnable) {
            String ssidStr = WifiInfoUtil.getSSID(this);
            if (!TextUtils.isEmpty(ssidStr)) {
                mWifiName.setText(ssidStr);
            }

            int wifiAdress = WifiInfoUtil.getWifiIp(this);
            if (wifiAdress != -1) {
                ipStr = WifiInfoUtil.intToIp(wifiAdress);
                mIpAddress.setText(ipStr);
            }
        }

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServiceMessage();
            }
        });

        mStartConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isKeepHeartBeat = true;
                if (!TextUtils.isEmpty(ipEdt.getText())) {
                    connectService(ipEdt.getText().toString());
                } else {
                    Toast.makeText(MyClientActivity.this, "请输入服务端的ip地址再连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void connectService(String ipStr) {
        try {
//            ipStr = "192.168.43.153";
            mIpStr = ipStr;
            socket = new Socket(mIpStr, 9999);
//            socket.setSoTimeout(50000);
            Log.d(TAG, "与服务器建立连接：" + socket);
            String str = "与服务器建立连接：" + socket;
//            Toast.makeText(MyClientActivity.this, str, Toast.LENGTH_LONG).show();
            Log.d(TAG, "connectService, socket.isConnected ： " + socket.isConnected());
            if (socket.isConnected()) {
//                keepHeartBeat();
//                recvProtobufMsg();
//            recvStringMsg();
            }
//            initView(socket);
            implSepPackage(socket);
        } catch (UnknownHostException e) {
            Log.e(TAG, "connectService: UnknownHostException e = " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "connectService: IOException e = " + e);
            e.printStackTrace();
        }
    }

    /**
     * 实现分包
     */
    public void implSepPackage(Socket socket){
        new ReceiveThread(socket).start();
    }

    /**
     * 发送消息给服务端
     */
    public void sendToServiceMessage() {
        try {
            // socket.getInputStream()
            Log.d(TAG, "sendToServiceMessage: socket = " + socket);
            Log.d(TAG, "sendToServiceMessage: socket.isConnected = " + socket.isConnected());
            Log.d(TAG, "sendToServiceMessage: socket.isInputShutdown = " + socket.isInputShutdown());
            Log.d(TAG, "sendToServiceMessage: socket.isOutputShutdown = " + socket.isOutputShutdown());
            Log.d(TAG, "sendToServiceMessage: socket.isClosed = " + socket.isClosed());
//            socket.setKeepAlive(true);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            Log.d(TAG, "sendToServiceMessage: writer = " + writer);
            String sendStr = mSendMessageEdit.getText().toString();
            Log.d(TAG, "sendToServiceMessage: sendStr = " + sendStr);
            if (TextUtils.isEmpty(sendStr)) {
                writer.writeUTF("嘿嘿，你好啊，服务器.."); // 写一个UTF-8的信息
            } else {
                writer.writeUTF(sendStr); // 写一个UTF-8的信息
            }
            Toast.makeText(MyClientActivity.this, "发送消息", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "sendToServiceMessage: e = " + e);
            e.printStackTrace();
        }
    }

    private void initView(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    while (true) {
                        // 获取读取流
                        InputStream inputStream = socket.getInputStream();

//                        与服务端对应的  DataOutputStream输出方式，DataInputStream接收数据  暂时不用
//                        reader = new DataInputStream(socket.getInputStream());
//                        Log.d(TAG, "initView, *等待服务端输入*");
//                        // 读取数据
//                        String msg = reader.readUTF();
//                        Log.d(TAG, "initView, 获取到服务端的信息：" + msg);
//                        Message mMessage = new Message();
//                        mMessage.what = 1;
//                        mMessage.obj = msg;
//                        handler.sendMessage(mMessage);



//                       byte[] datalen = new byte[10000];
//                        inputStream.read(datalen);
//                        String strbody = new String(datalen,0, datalen.length, "utf-8");
//                        Message mMessage = new Message();
//                        mMessage.what = 1;
//                        mMessage.obj = strbody;
//                        handler.sendMessage(mMessage);

                        testReceve(socket);
                    }

                } catch (IOException e) {
                    Log.d(TAG, "initView, run: e = " + e);
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 模拟收包粘包的情况   接收数据
     * @param socket
     */
    public void testReceve(Socket socket) {
        int count = 0;
        while (true) {
            try {
                byte[] byteBuffer = new byte[50];
                StringBuffer receivBuffer = new StringBuffer();
                InputStream reader = socket.getInputStream();
                count = reader.read(byteBuffer);
                if (count > 0) {
                    receivBuffer.append(new String(byteBuffer, 0, count));
                    Log.d(TAG, "receive data from client:   "  + receivBuffer.toString());
                }
                count = 0;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String info = msg.obj.toString();
                mReceveMesFromServicerText.setText("这是来自服务器的数据:" + msg.obj.toString());
                Toast.makeText(MyClientActivity.this, "收到服务端消息  " + info, Toast.LENGTH_LONG).show();
            }
        }
    };

    //开启心跳检测
    boolean isKeepHeartBeat = false;

    private final int HEARTBEART_PERIOD = 6 * 1000;
    ScheduledExecutorService executor;//定位定时器
    HeartBeatTask mHeartBeatTask;
    /**
     * 心跳维护
     */
    private void keepHeartBeat() {
        //设置心跳频率，启动心跳
        Log.d(TAG, "keepHeartBeat: isKeepHeartBeat = " + isKeepHeartBeat);
        if (isKeepHeartBeat) {
            if (mHeartBeatTask == null) {
                mHeartBeatTask = new HeartBeatTask();
            }
            try {
                if (executor != null) {
                    executor.shutdownNow();
                    executor = null;
                }
                executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(
                        mHeartBeatTask,
                        1000,  //initDelay
                        HEARTBEART_PERIOD,  //period
                        TimeUnit.MILLISECONDS);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class HeartBeatTask implements Runnable {
        @Override
        public void run() {
            //执行发送心跳
            try {
                Log.d(TAG, "HeartBeatTask, run: sendUrgentData ");
                socket.sendUrgentData(65);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Log.e(TAG, "socket心跳异常，尝试断开，重连");
                    socket.close();
                    socket = null;
                    //然后尝试重连
                    connectService(mIpStr);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            Log.e(TAG, "HeartBeatTask, 发送心跳，Socket.isClosed() = " + socket.isClosed() + "; connect = " + socket.isConnected());
        }
    }
}
