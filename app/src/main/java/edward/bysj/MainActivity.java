package edward.bysj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edward.bysj.Adapter.MusicAdapter;
import edward.bysj.bean.Music;
import edward.bysj.constants.Constants;
import edward.bysj.service.MusicService;
import edward.bysj.util.MusicUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    @InjectView(R.id.main_song_list)
     ListView list;
    @InjectView(R.id.song_next_main)
     ImageView main_next;
    @InjectView(R.id.title_main)
     TextView title_main;
    @InjectView(R.id.main_start_stop)
     ImageView start_stop;
    @InjectView(R.id.main_song_name)
     TextView song_name;
    @InjectView(R.id.main_buttom_ll)
    RelativeLayout buttom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        MusicUtil.getMusic(this);
        list.setAdapter(new MusicAdapter(this));
        list.setOnItemClickListener(this);
        start_stop.setOnClickListener(this);
        main_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,MusicService.class);
        switch (v.getId()){
            case R.id.main_start_stop:

                if (MusicUtil.CUR_STATUS == Constants.Music.MUSIC_STOP){
                    intent.putExtra("option","start");
                    intent.putExtra("path",MusicUtil.list.get(MusicUtil.CUR_MUSIC).getPath());
                    startService(intent);
                    start_stop.setImageResource(R.mipmap.stop_list);
                    MusicUtil.CUR_STATUS = Constants.Music.MUSIC_START;

                }else if (MusicUtil.CUR_STATUS == Constants.Music.MUSIC_START){
                    intent.putExtra("option","pause");
                    startService(intent);
                    start_stop.setImageResource(R.mipmap.start_list);
                    MusicUtil.CUR_STATUS = Constants.Music.MUSIC_PAUSE;

                }else if (MusicUtil.CUR_STATUS ==Constants.Music.MUSIC_PAUSE){
                    intent.putExtra("option","continue");
                    startService(intent);
                    start_stop.setImageResource(R.mipmap.stop_list);
                    MusicUtil.CUR_STATUS = Constants.Music.MUSIC_START;
                }
                break;
            case R.id.song_next_main:
                View view = list.findViewWithTag(MusicUtil.CUR_MUSIC);
                if(view != null){
                view.setVisibility(View.GONE);
            }
                if (MusicUtil.CUR_MUSIC == MusicUtil.list.size()-1){
                    MusicUtil.CUR_MUSIC = 0;
                }else{
                    MusicUtil.CUR_MUSIC++;
                }
                intent.putExtra("option","start");
                intent.putExtra("path",MusicUtil.list.get(MusicUtil.CUR_MUSIC).getPath());
                startService(intent);
                song_name.setText(MusicUtil.list.get(MusicUtil.CUR_MUSIC).getName());
                start_stop.setImageResource(R.mipmap.stop_list);
                MusicUtil.CUR_STATUS = Constants.Music.MUSIC_START;
                View viewWithTag = list.findViewWithTag(MusicUtil.CUR_MUSIC);
                viewWithTag.setVisibility(View.VISIBLE);
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent;
        if (MusicUtil.CUR_MUSIC == position){
            intent = new Intent(this, SongActivity.class);
            intent.putExtra("current_music",MusicUtil.CUR_MUSIC);
            startActivity(intent);
            //startActivityForResult(intent,Constants.Code.MAIN_START_SECOND_REQUEST);
        }else{
            View viewWithTag ;
            if (MusicUtil.CUR_MUSIC != -1){//以前播放过歌曲
                viewWithTag = list.findViewWithTag(MusicUtil.CUR_MUSIC);
                if (viewWithTag != null){
                    viewWithTag.setVisibility(View.GONE);
                }
            }
            MusicUtil.CUR_MUSIC =position;
            viewWithTag = list.findViewWithTag(MusicUtil.CUR_MUSIC);
            viewWithTag.setVisibility(View.VISIBLE);

            Music music = MusicUtil.list.get(position);
            song_name.setText(music.getName());
            buttom.setVisibility(View.VISIBLE);
            start_stop.setImageResource(R.mipmap.stop_list);

            intent = new Intent(this, MusicService.class);
            intent.putExtra("option", "start");
            intent.putExtra("path", MusicUtil.list.get(position).getPath());
            MusicUtil.CUR_STATUS = Constants.Music.MUSIC_START;
            startService(intent);
        }
    }
}
