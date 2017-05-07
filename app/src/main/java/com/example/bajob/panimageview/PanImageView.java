package com.example.bajob.panimageview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Display;

import com.diegocarloslima.byakugallery.lib.TouchImageView;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by bajob on 5/7/2017.
 */

public class PanImageView implements SensorEventListener {
    public static final int NEGATIVE_DIRECTION = -1;
    public static final int POSITIVE_DIRECTION = 1;
    private SensorManager sensorManager;
    private Sensor sensor;
    private TouchImageView touchImageView;
    private long lastUpdateTime;
    public static final long SAMPLING_PERIOD = 100L;
    private float speed = 26.0F;

    public PanImageView(TouchImageView touchImageView) {
        this.touchImageView = touchImageView;
        sensorManager = (SensorManager)touchImageView.getContext().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void register() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if (getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                if (currentTime - lastUpdateTime > SAMPLING_PERIOD) {
                    float X = - sensorEvent.values[0]*speed;
                    float Y = 0;//sensorEvent.values[2];
                    touchImageView.getImageMatrix().postTranslate(X, Y);
                    if (!touchImageView.canScrollHorizontally(NEGATIVE_DIRECTION)) {
                        touchImageView.getImageMatrix().postTranslate(-X, 0);
                    } else if (!touchImageView.canScrollHorizontally(POSITIVE_DIRECTION)) {
                        touchImageView.getImageMatrix().postTranslate(-X, 0);
                    }
                    touchImageView.invalidate();
                }
            } else if (getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                if (currentTime - lastUpdateTime > SAMPLING_PERIOD) {
                    float Z = -sensorEvent.values[1]*speed;
                    float Y = 0;//sensorEvent.values[2]*speed;
                    touchImageView.getImageMatrix().postTranslate(Z, Y);
                    if (!touchImageView.canScrollHorizontally(NEGATIVE_DIRECTION)) {
                        touchImageView.getImageMatrix().postTranslate(-Z, 0);
                    } else if (!touchImageView.canScrollHorizontally(POSITIVE_DIRECTION)) {
                        touchImageView.getImageMatrix().postTranslate(-Z, 0);
                    }
                    touchImageView.invalidate();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public int getScreenOrientation()
    {
        Display getOrient = ((Activity)touchImageView.getContext()).getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
}
