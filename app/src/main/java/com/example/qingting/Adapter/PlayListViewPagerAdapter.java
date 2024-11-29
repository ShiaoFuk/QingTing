package com.example.qingting.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.Bean.PlayList;
import com.example.qingting.R;

import java.util.List;

public class PlayListViewPagerAdapter extends RecyclerView.Adapter<PlayListViewPagerViewHolder> {
    List<List<PlayList>> playListListList;

    public PlayListViewPagerAdapter(List<List<PlayList>> playListListList) {
        this.playListListList = playListListList;
    }

    @NonNull
    @Override
    public PlayListViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (RecyclerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_layout, parent, false);
        return new PlayListViewPagerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PlayListViewPagerViewHolder holder, int position) {
        if (holder.recyclerView.getAdapter() == null) {
            // 首次进来设置adapter
            holder.recyclerView.setAdapter(new PlayListRecyclerViewAdapter(this, playListListList.get(position)));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.recyclerView.getContext());
            linearLayoutManager.setInitialPrefetchItemCount(7);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            PlayListRecyclerViewAdapter recyclerViewAdapter = (PlayListRecyclerViewAdapter) holder.recyclerView.getAdapter();
            recyclerViewAdapter.updateData(playListListList.get(position));
        }
    }


    public void updataData(Integer playListId) {
        for (int i = 0;  i < playListListList.size(); ++i) {
            playListListList.get(i).removeIf(v->v.getId() == playListId);
        }
        notifyDataSetChanged();
    }

    public void updateData(List<List<PlayList>> playListListList) {
        this.playListListList = playListListList;
        notifyDataSetChanged();
    }

    public void addPlayList(PlayList playList) {
        for (int i = 0; i < this.playListListList.size(); ++i) {
            playListListList.get(i).add(0, playList);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return playListListList.size();
    }
}


class PlayListViewPagerViewHolder extends RecyclerView.ViewHolder {
    RecyclerView recyclerView;
    public PlayListViewPagerViewHolder(@NonNull View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView;
    }
}
