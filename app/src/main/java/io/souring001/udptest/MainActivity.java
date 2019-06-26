package io.souring001.udptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int port = Integer.parseInt(getString(R.string.port));
        final String address = getString(R.string.address);
        Log.d(TAG, String.valueOf(port) + address);

        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    String msg = "Hello World! I'm Android";
                    try {
                        InetAddress IPAddress = InetAddress.getByName(address);
                        byte[] strByte = msg.getBytes("ASCII");

                        DatagramSocket sendUdpSocket = new DatagramSocket();
                        DatagramPacket sendPacket = new DatagramPacket(strByte, strByte.length, IPAddress, port);

                        sendUdpSocket.send(sendPacket);
                        sendUdpSocket.close();

                        Log.d(TAG, "Sent a packet:" + msg);
                    }catch(IOException e) {
                        Log.d(TAG, e.getMessage());
                    }

                    try {
                        Thread.sleep(1000); // 1 sec
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}
