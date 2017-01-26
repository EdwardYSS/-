package edward.bysj.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Random;

import edward.bysj.constants.Constants;
import edward.bysj.service.MusicService;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class PlayWaysUtils {

    private static void playMusic(Context context){
        Intent intent = new Intent(context,MusicService.class);
        intent.putExtra("option","start");
        intent.putExtra("path",MusicUtil.list.get(MusicUtil.CUR_MUSIC).getPath());
        context.startService(intent);
    }

    //单曲循环
    private static void single(Context context){

        playMusic(context);
    }

    //随机
    private static void random(Context context){
        MusicUtil.CUR_MUSIC = new Random().nextInt(MusicUtil.list.size());
        playMusic(context);
    }
    //列表循环
    private static void listNext(Context context){
        if (MusicUtil.CUR_MUSIC < MusicUtil.list.size()-1){
            MusicUtil.CUR_MUSIC++;
            //Log.e("main1","---------"+MusicUtil.CUR_MUSIC);
        }else {
            MusicUtil.CUR_MUSIC = 0;
        }
        Log.e("main1",MusicUtil.CUR_MUSIC+"--start_position");
        playMusic(context);
    }

    private static void listLast(Context context){
        if (MusicUtil.CUR_MUSIC > 0){
            MusicUtil.CUR_MUSIC--;
        }else {
            MusicUtil.CUR_MUSIC = MusicUtil.list.size()-1;
        }
        playMusic(context);
    }

    public static void nextMusic(Context context){

        if (MusicUtil.CUR_PLAYWAY == Constants.Play.LIST){
            PlayWaysUtils.listNext(context);
        }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.SINGLE){
            PlayWaysUtils.single(context);
        }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.RANDOM){
            PlayWaysUtils.random(context);
        }
    }

    public static void lastMusic(Context context){

       if (MusicUtil.CUR_PLAYWAY == Constants.Play.LIST){
            PlayWaysUtils.listLast(context);
        }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.SINGLE){
            PlayWaysUtils.single(context);
        }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.RANDOM){
            PlayWaysUtils.random(context);
        }
    }
}
