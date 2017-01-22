package edward.bysj.broadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edward.bysj.service.MusicService;
import edward.bysj.util.MusicUtil;


/**
 * Created by Administrator on 2017/1/21 0021.
 */

public class AutoNextBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int progress = intent.getIntExtra("progressAll",-1);
        if (progress == -2) {
            AutoCricleNext(context);
        }
    }

    private void AutoCricleNext(Context context){

        Intent intent1 ;
        if (MusicUtil.CUR_MUSIC == MusicUtil.list.size()-1){
            MusicUtil.CUR_MUSIC = 0;
        }else{
            MusicUtil.CUR_MUSIC++;
        }
       intent1 = new Intent(context,MusicService.class);
        intent1.putExtra("option","start");
        intent1.putExtra("path",MusicUtil.list.get(MusicUtil.CUR_MUSIC).getPath());
        context.startService(intent1);
    }
}
