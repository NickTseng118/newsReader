package com.ntseng.cnn_top_headlines.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class RoutineUpdateService extends Service {

    private final static long ROUTINE_UPDATE_TIME = 1000 * 60 * 30;
    public RoutineUpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent startParserService = new Intent(RoutineUpdateService.this, XMLParserService.class);
                startService(startParserService);
                Log.e("routine", "update");
            }
        };
        timer.schedule(timerTask, ROUTINE_UPDATE_TIME, ROUTINE_UPDATE_TIME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
