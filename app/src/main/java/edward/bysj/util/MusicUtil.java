package edward.bysj.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import edward.bysj.bean.Music;
import edward.bysj.constants.Constants;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class MusicUtil {

    public static int CUR_MUSIC = -1;
    public static int CUR_STATUS = Constants.Music.MUSIC_STOP;
    public static List<Music> list = new ArrayList<>();

    public static void getMusic(Context context){

        list.clear();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String projection[] = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        //断言 如果cursor 为null 程序结束
        assert cursor != null;

        while(cursor.moveToNext()){
            Music music = new Music();
            String name = cursor.getString(cursor.getColumnIndex(projection[0]));
            music.setName(name);
            int time = cursor.getInt(cursor.getColumnIndex(projection[1]));
            String min = time/60000<10? 0 +"" +time/60000 : ""+time/60000;
            String sec = (time/1000%60<10)? (0 +""+time/1000%60):(""+time/1000%60);
            music.setTime(min+":"+sec);
            String path = cursor.getString(cursor.getColumnIndex(projection[2]));
            music.setPath(path);
            list.add(music);

        }
        cursor.close();
    }


}
