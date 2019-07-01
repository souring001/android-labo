package io.souring001.androidlabocontroller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.os.Vibrator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    final static double RAD2DEG = 180/Math.PI;

    final static String controller = "pedal";
//    final static String controller = "handle";

    SensorManager sensorManager;

    float[] rotationMatrix = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] attitude = new float[3];

    TextView yawText;
    TextView pitchText;
    TextView rollText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initSensor();

        final int port = Integer.parseInt(getString(R.string.port));
        final String address = getString(R.string.address);
        Log.d(TAG, String.valueOf(port) + address);

        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){

                    String header = "{ \"controller\" : \"" + controller + "\", \"pitch\" : " ;
                    String val = Integer.toString((int)(attitude[1] * RAD2DEG));
                    String footer = " }";

                    // send message in JSON format as follows:
                    // { "controller" : "pedal", "pitch" : -4 }

                    String msg = header + val + footer;

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

                    if((int)(attitude[1] * RAD2DEG) > -20 && controller.equals("pedal")) {
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(66);
                    }

                    try {
                        Thread.sleep(66);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    protected void findViews(){
        yawText = findViewById(R.id.yaw);
        pitchText = findViewById(R.id.pitch);
        rollText = findViewById(R.id.roll);
    }

    protected void initSensor(){
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if (sensorManager == null) {
            Toast.makeText(this, R.string.toast_no_sensor_manager, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
        }

        if(geomagnetic != null && gravity != null){

            SensorManager.getRotationMatrix(
                    rotationMatrix, null,
                    gravity, geomagnetic);

            SensorManager.getOrientation(
                    rotationMatrix,
                    attitude);

            yawText.setText(Integer.toString(
                    (int)(attitude[0] * RAD2DEG)));
            pitchText.setText(Integer.toString(
                    (int)(attitude[1] * RAD2DEG)));
            rollText.setText(Integer.toString(
                    (int)(attitude[2] * RAD2DEG)));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged: accuracy=" + accuracy);
    }
}
