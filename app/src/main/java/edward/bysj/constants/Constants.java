package edward.bysj.constants;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class Constants {

    public static final class Music{
        public static final int MUSIC_START = 1001;
        public static final int MUSIC_PAUSE = 1002;
        public static final int MUSIC_STOP = 1003;

    }

    public static final class BroadCastAction{
        public static final String SERVICE_SEND_ALL_ACTION = "edward.music.all.changemusic.service";
        public static final String SERVICE_SEND_ACTION = "edward.music.seek.service";
        public static final String SERVICE_SEND_CHANGE_ACTION = "edward.music.seek.service";
    }

    public static final class Code{
        public static  final  int MAIN_START_SECOND_REQUEST = 2001;
    }
}
