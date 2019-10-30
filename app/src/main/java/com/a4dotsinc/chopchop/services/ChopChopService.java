package com.a4dotsinc.chopchop.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;

import com.a4dotsinc.chopchop.R;
import com.squareup.seismic.ShakeDetector;

import static com.a4dotsinc.chopchop.App.CHANNEL_ID;

public class ChopChopService extends Service implements ShakeDetector.Listener {
    private static final String TAG = "ClipboardManager";
    static boolean isOn = false;
    static String cameraId = null;
    static long time = 0;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("text");


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Chop Chop Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_flash_on_black_24dp)
                .build();

        startForeground(1, notification);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void hearShake() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }
        if(isOn && (time+200 < System.currentTimeMillis())){
            try {
                camManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis();
            isOn = false;

        }else{

            try {
                cameraId = camManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            try {
                camManager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis();
            isOn = true;
        }
    }
}