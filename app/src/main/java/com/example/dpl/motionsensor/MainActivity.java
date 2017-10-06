package com.example.dpl.motionsensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{
    private TextView tvAccelerometer;//Accelerometer:加速计
    private SensorManager sensorManager;
    private float[] gravity=new float[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAccelerometer= (TextView) findViewById(R.id.Tv);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()){
            case Sensor.TYPE_GRAVITY://重力传感器
                gravity[0]=sensorEvent.values[0];
                gravity[1]=sensorEvent.values[1];
                gravity[2]=sensorEvent.values[2];
                break;
            case Sensor.TYPE_ACCELEROMETER://加速度传感器
                final float alpha=(float)0.8;//系数去误差的方法（消除重力的影响）
                gravity[0]=alpha*gravity[0]+(1-alpha)*sensorEvent.values[0];
                gravity[1]=alpha*gravity[1]+(1-alpha)*sensorEvent.values[1];
                gravity[2]=alpha*gravity[0]+(1-alpha)*sensorEvent.values[2];
                String accelerometer="加速度传感器:\n"+"X:"+(sensorEvent.values[0]-gravity[0])+"\n"+"Y:"+(sensorEvent.values[1]-gravity[1])+"\n"+"Z:"+(sensorEvent.values[2]-gravity[2]);
                tvAccelerometer.setText(accelerometer);
                break;
            case Sensor.TYPE_PROXIMITY://临近传感器，有的设备没反应
                setTitle(String.valueOf(sensorEvent.values[0]));//临近的距离
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //onResume()是当该activity与用户能进行交互时被执行，用户可以获得activity的焦点，能够与用户交互。
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);//注册加速度传感器
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_FASTEST);//注册重力传感器
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_UI);//临近传感器
    }
    //onPause 用于由一个Activity转到另一个Activity、设备进入休眠状态(屏幕锁住了)、或者有dialog弹出时
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
