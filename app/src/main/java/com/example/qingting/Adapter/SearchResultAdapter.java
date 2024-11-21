package com.example.qingting.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.Bean.Music;
import com.example.qingting.MyApplication;
import com.example.qingting.R;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {

    @Setter
    List<Music> musicList = new ArrayList<>();
    public SearchResultAdapter(List<Music> musicList) {
        this.musicList = musicList;
    }




    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        final Music music = musicList.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.playMusic(music, null);
            }
        });
        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.playMusic(music, null);
            }
        });
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.addMusicToNext(music, null);
            }
        });
        holder.title.setText(music.getName());
        holder.genre.setText(music.getGenre());
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }


}


class SearchResultViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView title;
    TextView genre;
    ImageView playBtn;
    ImageView addBtn;
    public SearchResultViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        title = view.findViewById(R.id.music_name);
        genre = view.findViewById(R.id.music_genre);
        playBtn = view.findViewById(R.id.play_btn);
        addBtn = view.findViewById(R.id.add_btn);
    }
}