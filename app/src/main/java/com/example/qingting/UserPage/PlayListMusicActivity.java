package com.example.qingting.UserPage;


import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.Adapter.SearchResultAdapter;
import com.example.qingting.Bean.Music;
import com.example.qingting.CustomView.LoadingFactory;
import com.example.qingting.R;
import com.example.qingting.Utils.ToastUtils;

import java.util.List;

public class PlayListMusicActivity extends AppCompatActivity {
    RecyclerView playListMusicView;
    Integer playListId;
    List<Music> musicList;
    Context context;
    public final static String ID_INTENT_KEY = "play_list_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_list_music);
        playListMusicView = findViewById(R.id.play_list_music);
        context = this;
        init();
    }


    private void init() {
        LoadingFactory.loading(new LoadingFactory.LoadingViewParent(findViewById(R.id.loading_layout)) {
            @Override
            public boolean doSth() {
                if (initPlayListId()) {
                    initPlayList();
                    initRecyclerView();
                    return true;
                }
                ToastUtils.makeShortText(context, getString(R.string.get_music_fail));
                return false;
            }
        });
    }


    /**
     * 获取playListId，获取成功则插入
     * @return
     */
    private boolean initPlayListId() {
        playListId = getIntent().getIntExtra(ID_INTENT_KEY, -1);
        if (playListId == null || playListId == -1) {
            return false;
        }
        return true;
    }


    private void initPlayList() {
        if (playListId == null) return;
//         TODO:从数据库读取歌单
//        musicList = getMusicList(playListId);
    }


    private void initRecyclerView() {
        if (playListId == null) return;
        playListMusicView.setAdapter(new SearchResultAdapter(musicList));
    }

}

