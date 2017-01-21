package edward.bysj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edward.bysj.R;
import edward.bysj.bean.Music;
import edward.bysj.util.MusicUtil;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class MusicAdapter extends BaseAdapter {

    private Context context;
    private List<Music> list;

    public MusicAdapter(Context context){
        this.context = context;
        list = MusicUtil.list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
            holder.name = (TextView) convertView.findViewById(R.id.item_song_name);
            holder.time = (TextView) convertView.findViewById(R.id.item_song_time);
            holder.view =  convertView.findViewById(R.id.song_tag);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Music music = list.get(position);
        holder.view.setTag(position);
        if (position != MusicUtil.CUR_MUSIC){
            holder.view.setVisibility(View.GONE);
        }else {
            holder.view.setVisibility(View.VISIBLE);
        }
        holder.name.setText(music.getName());
        holder.time.setText(music.getTime());


        return convertView;
    }

    class ViewHolder{
        TextView name,time;
        View view;
    }
}
