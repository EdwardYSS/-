package edward.bysj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edward.bysj.bean.Music;
import edward.bysj.constants.Constants;
import edward.bysj.service.MusicService;
import edward.bysj.util.MusicUtil;

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
    private int position;
    private LocalBroadcastManager mManager;
    private MyBroadCastReceiver myBroadCastReceiver;
    private Intent intentb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        ButterKnife.inject(this);
        registerMyReceiver();
        mManager = LocalBroadcastManager.getInstance(this);
        intentb = new Intent(Constants.BroadCastAction.SERVICE_SEND_ACTION);
        initView();
    }

    private void initView() {
        Intent intent1 = getIntent();
        position = intent1.getIntExtra("current_music",-1);
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
                break;
            case R.id.play_change:
                break;
            case R.id.song_last:
                if (position == 0){
                    position = MusicUtil.list.size()-1;
                }else{
                    position--;
                }
                Music music = MusicUtil.list.get(position);
                intent.putExtra("option","start");
                intent.putExtra("path",music.getPath());
                refreUI(music);
                startService(intent);
                break;
            case R.id.song_next:
                circleNext(intent);
                break;
            case R.id.song_start_stop:

                if (MusicUtil.CUR_STATUS == Constants.Music.MUSIC_STOP){
                    intent.putExtra("option","start");
                    intent.putExtra("path",MusicUtil.list.get(position).getPath());
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

    private void refreUI(final Music music){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                song_title.setText(music.getName());
                song_start_stop.setImageResource(R.mipmap.stop_text);
            }
        });
    }

    /***
     * 列表循环模式下的下一首
     * @param intent
     */
        private void circleNext(Intent intent){
        if (position == MusicUtil.list.size()-1){
            position = 0;
        }else{
            position++;
        }
        Music music1 = MusicUtil.list.get(position);
        intent.putExtra("option","start");
        intent.putExtra("path",music1.getPath());
        refreUI(music1);
        startService(intent);
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
    private void refureProgress(int max,int progress){

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

    private void cricleAutoNext(){

        Intent intent;
        if (position == MusicUtil.list.size()-1){
            position = 0;
        }else {
            position++;
        }
        intent = new Intent(this,MusicService.class);
        intent.putExtra("option","start");
        intent.putExtra("path",MusicUtil.list.get(position).getPath());
        refreUI(MusicUtil.list.get(position));

        startService(intent);

    }

    class MyBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra("progressb", -1);
            int total = intent.getIntExtra("total", -1);
            if (progress == -2){
                cricleAutoNext();
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
}
