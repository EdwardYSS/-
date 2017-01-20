package edward.bysj.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import edward.bysj.constants.Constants;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class MusicService extends Service implements MediaPlayer.OnErrorListener ,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private LocalBroadcastManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        manager =LocalBroadcastManager.getInstance(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String option = intent.getStringExtra("option");

        if ("start".equals(option)){
            play(intent.getStringExtra("path"));
        }else if ("pause".equals(option)){
            pause();
        }else if ("stop".equals(option)){
            stop();
        }else if ("continue".equals(option)){
            continueMusic();
        }else if ("progress".equals(option)){
            int p = intent.getIntExtra("progress_stop",-1);
            mediaPlayer.seekTo(p);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void  play(String path){
        try {
            //重置
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pause(){

        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    private void continueMusic(){
        if (mediaPlayer != null && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    private void stop(){

        if (mediaPlayer != null){
            mediaPlayer.stop();
        }
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    /***
     * 完成的时候发广播切换到下一首
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

        Intent intent;
        intent = new Intent(Constants.BroadCastAction.SERVICE_SEND_ACTION);
        intent.putExtra("progressb",-2);

        manager.sendBroadcast(intent);
    }

    //将歌曲播放数据回调
    @Override
    public void onPrepared(MediaPlayer mp) {

        final Intent intent;
        intent = new Intent(Constants.BroadCastAction.SERVICE_SEND_ACTION);
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (manager != null){
                    intent.putExtra("progressb",mediaPlayer.getCurrentPosition());
                    intent.putExtra("total",mediaPlayer.getDuration());
                    manager.sendBroadcast(intent);
                }
            }
        },0,1000);
    }
}
