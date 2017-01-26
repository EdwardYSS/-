package edward.bysj.broadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edward.bysj.util.PlayWaysUtils;


/**
 * Created by Administrator on 2017/1/21 0021.
 */

public class AutoNextBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int progress = intent.getIntExtra("progressAll",-1);
        if (progress == -2) {
            PlayWaysUtils.nextMusic(context);
        }
    }
}
