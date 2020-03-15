package com.example.dell.batterylevelcontrol;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class First extends AppCompatActivity {
    public TextView TV;
    public SeekBar S;
    public ToggleButton TB;
    public Button B1, B2, B3;
    public int i, Level, L;
    public TextView T3;
    public ImageView I;

    private BroadcastReceiver b = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        this.registerReceiver(this.b, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        TV = (TextView) findViewById(R.id.textView);
        S = (SeekBar) findViewById(R.id.seekBar);
        TB = (ToggleButton) findViewById(R.id.toggleButton);
        B1 = (Button) findViewById(R.id.button);
        B2 = (Button) findViewById(R.id.button2);
        B3 = (Button) findViewById(R.id.button3);
        I = (ImageView) findViewById(R.id.imageView);
        T3 = (TextView) findViewById(R.id.textView3);
        S.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int P, boolean b) {
                TV.setText(String.valueOf(P)+"%");
                L =P;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                T3.setVisibility(View.INVISIBLE);
                TB.setVisibility(View.VISIBLE);
                B1.setVisibility(View.VISIBLE);
            }
        });
    }//end of on create

    @Override
    protected void onRestart() {
        super.onRestart();
        unregisterReceiver(b);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(b);
    }

    public void ON(View view) {

        Intent in = new Intent();
        in.setAction(Intent.ACTION_MAIN);
        in.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(in);

        i = 1;
        Toast.makeText(First.this, "ALarm Will Start At " + TV.getText() + " %", Toast.LENGTH_SHORT).show();
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(First.this)
                .setSmallIcon(R.drawable.power)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.power));
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(First.this);

        B1.setVisibility(View.INVISIBLE);
        B2.setVisibility(View.VISIBLE);




        final Thread t = new Thread() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                if ((i == 1) && (Level >= L) && (!TB.isChecked())) {
                                        notificationBuilder.setContentText("Battery Level Exceeding");
                                        notificationManager.notify(1, notificationBuilder.build());
                                        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
                                    TV.setVisibility(View.INVISIBLE);
                                    S.setVisibility(View.INVISIBLE);
                                    TB.setVisibility(View.INVISIBLE);
                                    I.setVisibility(View.VISIBLE);
                                    B3.setVisibility(View.VISIBLE);
                                    B2.setText("Stop");
                                    B2.setVisibility(View.VISIBLE);
                                    B2.setBackgroundColor(Color.parseColor("#00FF00"));
                                }
                                if ((i == 1) && (Level <= L) && (TB.isChecked())) {
                                 notificationBuilder.setContentText("Battery Level Depleting");
                                        notificationManager.notify(1, notificationBuilder.build());
                                        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
                                    TV.setVisibility(View.INVISIBLE);
                                    S.setVisibility(View.INVISIBLE);
                                    TB.setVisibility(View.INVISIBLE);
                                    I.setVisibility(View.VISIBLE);
                                    B3.setVisibility(View.VISIBLE);
                                    B2.setText("Stop");
                                    B2.setVisibility(View.VISIBLE);
                                    B2.setBackgroundColor(Color.parseColor("#00FF00"));
                                }
                            }
                        });
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                }
            }

        };
        t.start();
    }


    public void OFF(View view) {
        i = 0;
        B3.setVisibility(View.INVISIBLE);
        B1.setVisibility(View.VISIBLE);
        I.setVisibility(View.INVISIBLE);
        TV.setVisibility(View.VISIBLE);
        S.setVisibility(View.VISIBLE);
        TB.setVisibility(View.VISIBLE);
        B2.setText("OFF");
        B2.setBackgroundColor(Color.parseColor("#00BCD4"));
        B2.setVisibility(View.INVISIBLE);
        S.setVisibility(View.VISIBLE);
        TB.setVisibility(View.INVISIBLE);
        B1.setVisibility(View.INVISIBLE);
        T3.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Exit(View view) {
        i = 0;
       finish();
    }
}

