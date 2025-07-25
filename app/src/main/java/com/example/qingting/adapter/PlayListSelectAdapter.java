package com.example.qingting.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.bean.PlayList;
import com.example.qingting.R;
import com.example.qingting.utils.DialogUtils;
import com.example.qingting.utils.ImageLoadUtils;

import java.util.List;

public class PlayListSelectAdapter extends RecyclerView.Adapter<PlayListSelectViewHolder> {
    final static String TAG = PlayListSelectAdapter.class.getName();
    List<PlayList> playListList;

    DialogUtils.MyOnCancelListener myOnCancelListener;

    public PlayListSelectAdapter(DialogUtils.MyOnCancelListener myOnCancelListener, List<PlayList> playListList) {
        this.playListList = playListList;
        this.myOnCancelListener = myOnCancelListener;
    }

    @NonNull
    @Override
    public PlayListSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_select_item_layout, parent, false);
        return new PlayListSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListSelectViewHolder holder, int position) {
        final PlayList playList = playListList.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnCancelListener.closeDialog(playList);
            }
        });
        if (playList.getUrl() != null) {
            if (playList.getUrl().startsWith("http")) {
                ImageLoadUtils.loadFromNet(holder.imageView, playList.getUrl());
            } else {
                ImageLoadUtils.loadFromLocal(holder.imageView, playList.getUrl());
            }
        } else {
            holder.imageView.setImageResource(ImageLoadUtils.DEFAULT_SRC_ID);
        }
        holder.playListName.setText(playList.getName());
        holder.playListInfo.setText(playList.getPlayTimes() + "播放 · " + playList.getLikes() + "点赞");
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




class PlayListSelectViewHolder extends RecyclerView.ViewHolder {
    View view;
    ImageView imageView;
    TextView playListName;
    TextView playListInfo;
    public PlayListSelectViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.imageView = itemView.findViewById(R.id.play_list_img);
        this.playListName = itemView.findViewById(R.id.play_list_name);
        this.playListInfo = itemView.findViewById(R.id.play_list_info);
    }
}
