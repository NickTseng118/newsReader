package com.ntseng.cnn_top_headlines.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class RoutineUpdateService extends Service {

    private Handler handler;
    private Runnable runnable;
    Intent startParserService;

    private final static long ROUTINE_UPDATE_TIME = 1000 * 60 * 30;
    public RoutineUpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startParserService = new Intent(RoutineUpdateService.this, XMLParserService.class);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startService(startParserService);
                Log.e("routine", "update");
            }
        };
        timer.schedule(timerTask, ROUTINE_UPDATE_TIME, ROUTINE_UPDATE_TIME);

        IntentFilter grabCompletion = new IntentFilter(XMLParserService.GRAB_COMPLETTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onEvent, grabCompletion);
        IntentFilter grabFailure = new IntentFilter(XMLParserService.GRAB_FAILURE);
        LocalBroadcastManager.getInstance(this).registerReceiver(onEvent, grabFailure);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onEvent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ( intent.getAction() == XMLParserService.GRAB_FAILURE){
                retryParseCNNFeeds();
            } else if ( intent.getAction() == XMLParserService.GRAB_COMPLETTION ){

            }

        }
    };
    private void retryParseCNNFeeds(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("download status", "retry" );
                startService(startParserService);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
