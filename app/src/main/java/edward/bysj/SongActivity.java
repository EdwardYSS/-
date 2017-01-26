package edward.bysj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edward.bysj.bean.Music;
import edward.bysj.constants.Constants;
import edward.bysj.service.MusicService;
import edward.bysj.util.MusicUtil;
import edward.bysj.util.PlayWaysUtils;

public class SongActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    @InjectView(R.id.song_back)
    Button back;
    @InjectView(R.id.song_title)
    TextView song_title;
    @InjectView(R.id.running_time)
    TextView running_time;
    @InjectView(R.id.total_time)
    TextView total_time;
    @InjectView(R.id.song_seek)
    SeekBar seekBar;
    @InjectView(R.id.song_last)
    ImageView song_last;
    @InjectView(R.id.song_next)
    ImageView song_next;
    @InjectView(R.id.song_start_stop)
    ImageView song_start_stop;
    @InjectView(R.id.song_text_list)
    ImageView song_text_list;
    @InjectView(R.id.play_change)
    ImageView play_change;
    private LocalBroadcastManager mManager;
    private MyBroadCastReceiver myBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        ButterKnife.inject(this);
        registerMyReceiver();
        mManager = LocalBroadcastManager.getInstance(this);
        initView();
    }

    private void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //导航栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        Intent intent1 = getIntent();
        int position = intent1.getIntExtra("current_music",-1);
        Music music = MusicUtil.list.get(position);
        song_title.setText(music.getName());
        back.setOnClickListener(this);
        song_last.setOnClickListener(this);
        song_next.setOnClickListener(this);
        song_start_stop.setOnClickListener(this);
        play_change.setOnClickListener(this);
        song_text_list.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MusicService.class);
        switch (v.getId()){
            case R.id.song_back:
                finish();
                break;
            case R.id.song_text_list:
                finish();
                break;
            case R.id.play_change:
                if (MusicUtil.CUR_PLAYWAY == Constants.Play.LIST){
                    play_change.setImageResource(R.mipmap.single);
                    MusicUtil.CUR_PLAYWAY = Constants.Play.SINGLE;
                    Toast.makeText(this,"单曲循环",Toast.LENGTH_SHORT).show();
                }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.SINGLE){
                    play_change.setImageResource(R.mipmap.sjbf);
                    MusicUtil.CUR_PLAYWAY = Constants.Play.RANDOM;
                    Toast.makeText(this,"随机播放",Toast.LENGTH_SHORT).show();
                }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.RANDOM){
                    play_change.setImageResource(R.mipmap.sxbf);
                    MusicUtil.CUR_PLAYWAY = Constants.Play.LIST;
                    Toast.makeText(this,"列表循环",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.song_last:
                PlayWaysUtils.lastMusic(this);
                Music music = MusicUtil.list.get(MusicUtil.CUR_MUSIC);
                refreUI(music);
                break;
            case R.id.song_next:
                PlayWaysUtils.nextMusic(this);
                Music music1 = MusicUtil.list.get(MusicUtil.CUR_MUSIC);
                refreUI(music1);
                break;
            case R.id.song_start_stop:

                if (MusicUtil.CUR_STATUS == Constants.Music.MUSIC_STOP){
                    intent.putExtra("option","start");
                    intent.putExtra("path",MusicUtil.list.get(MusicUtil.CUR_MUSIC).getPath());
                    startService(intent);
                    song_start_stop.setImageResource(R.mipmap.stop_text);
                    MusicUtil.CUR_STATUS = Constants.Music.MUSIC_START;

                }else if (MusicUtil.CUR_STATUS == Constants.Music.MUSIC_START){
                    intent.putExtra("option","pause");
                    startService(intent);
                    song_start_stop.setImageResource(R.mipmap.start_text);
                    MusicUtil.CUR_STATUS = Constants.Music.MUSIC_PAUSE;

                }else if (MusicUtil.CUR_STATUS ==Constants.Music.MUSIC_PAUSE){
                    intent.putExtra("option","continue");
                    startService(intent);
                    song_start_stop.setImageResource(R.mipmap.stop_text);
                    MusicUtil.CUR_STATUS = Constants.Music.MUSIC_START;
                }
                break;
        }
    }

    public void refreUI(final Music music){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                song_title.setText(music.getName());
                song_start_stop.setImageResource(R.mipmap.stop_text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentMusicWay();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String min;
        String sed;
        min = progress / 60000 < 10 ? 0 + "" + progress / 60000 : progress / 60000 + "";
        sed = (progress / 1000 % 60 < 10) ? (0 + "" + progress / 1000 % 60) : (progress / 1000 % 60 + "");
        running_time.setText(min + ":" + sed);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        Intent intent = new Intent(SongActivity.this, MusicService.class);
        intent.putExtra("option", "progress");
        intent.putExtra("progress_stop", seekBar.getProgress());
        startService(intent);
    }

    //更新seekBar随着歌曲的播放
    public void refureProgress(int max,int progress){

        String min;
        String sed;
        seekBar.setMax(max);
        seekBar.setProgress(progress);

        min = max / 60000 < 10 ? 0 + "" + max / 60000 : max / 60000 + "";
        sed = (max / 1000 % 60 < 10) ? (0 + "" + max / 1000 % 60) : (max / 1000 % 60 + "");
        String t = min + ":" + sed;
        total_time.setText(t);

        min = progress / 60000 < 10 ? 0 + "" + progress / 60000 : progress / 60000 + "";
        sed = (progress / 1000 % 60 < 10) ? (0 + "" + progress / 1000 % 60) : (progress / 1000 % 60 + "");
        String p = min + ":" + sed;
        running_time.setText(p);
    }



    class MyBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra("progressb", -1);
            int total = intent.getIntExtra("total", -1);
            if (progress == -2){
                //PlayWaysUtils.nextMusic(context);
                refreUI(MusicUtil.list.get(MusicUtil.CUR_MUSIC));
            }else {
                refureProgress(total, progress);
            }
        }
    }

    /***
     * 注册广播
     */
    private void registerMyReceiver() {
        mManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();

        filter.addAction(Constants.BroadCastAction.SERVICE_SEND_ACTION);
        myBroadCastReceiver = new MyBroadCastReceiver();
        mManager.registerReceiver(myBroadCastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        mManager.unregisterReceiver(myBroadCastReceiver);
        super.onDestroy();
    }

    private void currentMusicWay(){

        if (MusicUtil.CUR_PLAYWAY == Constants.Play.RANDOM){
            play_change.setImageResource(R.mipmap.sjbf);
        }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.LIST){
            play_change.setImageResource(R.mipmap.sxbf);
        }else if (MusicUtil.CUR_PLAYWAY == Constants.Play.SINGLE){
            play_change.setImageResource(R.mipmap.single);
        }
    }
}
