package com.example.android.proxsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    TextView T;
    SensorManager sm;
    Sensor ps;
    Ringtone dr = null;
    RingtoneManager rm;
    int i;
    boolean RUN=false;
    ProgressBar P;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        T = (TextView) findViewById(R.id.tv);
        Uri tone = rm.getDefaultUri(RingtoneManager.TYPE_ALARM);
        dr = rm.getRingtone(this, tone);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ps = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        P = (ProgressBar) findViewById(R.id.pb);

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {

        float d = event.values[0];

        if(d==0)
        {
            new Task().execute();
        }

        else
        {
            dr.stop();
            P.setVisibility(View.GONE);
            T.setVisibility(View.GONE);
            RUN=true;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        dr.stop();
        RUN = true;
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        RUN = false;
        sm.registerListener(this,ps,SensorManager.SENSOR_DELAY_NORMAL);
    }


  class Task extends AsyncTask<Void,Void,Void>{

    @Override
    protected void onPreExecute() {

        i=10;
        P.setVisibility(View.VISIBLE);
        T.setVisibility(View.VISIBLE);
        T.setText(String.valueOf(i));
        RUN=false;

    }

    @Override
    protected void onCancelled() {
        RUN=true;
    }

    @Override
    protected Void doInBackground(Void... params) {

        while (i>=0 && !RUN)
        {
            publishProgress();

            try
            {
            Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
            e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        T.setText(String.valueOf(i));
        P.setProgress(i);

        if (i==0)
        {
            dr.play();
            P.setVisibility(View.GONE);
            T.setVisibility(View.GONE);
            cancel(true);
        }

        i--;
    }

  }

}
