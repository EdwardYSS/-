package edward.bysj.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class MusicService extends Service implements MediaPlayer.OnErrorListener ,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
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

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
