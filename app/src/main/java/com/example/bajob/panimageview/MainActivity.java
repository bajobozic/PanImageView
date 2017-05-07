package com.example.bajob.panimageview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;
import com.diegocarloslima.byakugallery.lib.TouchImageView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private TouchImageView touchImageView;
    private PanImageView panImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        touchImageView = (TouchImageView) findViewById(R.id.touch_image);
        InputStream inputStream = getResources().openRawResource(R.raw.photo2);
        TileBitmapDrawable.attachTileBitmapDrawable(touchImageView,inputStream,null,null);
        panImageView = new PanImageView(touchImageView);
        ((ViewGroup)touchImageView.getParent()).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    protected void onResume() {
        super.onResume();
        panImageView.register();
    }

    @Override
    protected void onPause() {
        panImageView.unregister();
        super.onPause();
    }

    @Override
    protected void onStop() {
        panImageView = null;
        super.onStop();
    }
}
