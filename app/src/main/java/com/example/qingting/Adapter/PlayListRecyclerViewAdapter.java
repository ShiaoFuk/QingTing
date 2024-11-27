package com.example.qingting.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.Bean.PlayList;
import com.example.qingting.R;
import com.example.qingting.Utils.ImageLoadUtils;

import java.util.List;

public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter<PlayListRecyclerViewViewHolder> {
    List<PlayList> playListList;

    public PlayListRecyclerViewAdapter(List<PlayList> playListList) {
        this.playListList = playListList;
    }

    @NonNull
    @Override
    public PlayListRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_item_layout, parent, false);
        return new PlayListRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListRecyclerViewViewHolder holder, int position) {
        final PlayList playList = playListList.get(position);
        if (playList.getUrl() != null) {
            if (playList.getUrl().startsWith("http")) {
                ImageLoadUtils.loadFromNet(holder.imageView, playList.getUrl());
            } else {
                ImageLoadUtils.loadFromLocal(holder.imageView, playList.getUrl());
            }
        } else {
            holder.imageView.setImageResource(ImageLoadUtils.DEFAULT_SRC_ID);
        }
        holder.songName.setText(playList.getName());
        holder.songInfo.setText(playList.getPlayTimes() + "播放 · " + playList.getLikes() + "点赞");
    }

    @Override
    public int getItemCount() {
        return playListList.size();
    }

    public void updateData(List<PlayList> playListList) {
        this.playListList = playListList;
        notifyDataSetChanged();
    }
}




class PlayListRecyclerViewViewHolder extends RecyclerView.ViewHolder {
    View view;
    ImageView imageView;
    TextView songName;
    TextView songInfo;
    public PlayListRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.imageView = itemView.findViewById(R.id.play_list_img);
        this.songName = itemView.findViewById(R.id.play_list_name);
        this.songInfo = itemView.findViewById(R.id.play_list_info);
    }
}
