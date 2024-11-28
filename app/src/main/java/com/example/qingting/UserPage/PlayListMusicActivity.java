package com.example.qingting.UserPage;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.Adapter.SearchResultAdapter;
import com.example.qingting.Bean.Music;
import com.example.qingting.Bean.PlayList;
import com.example.qingting.CustomView.LoadingFactory;
import com.example.qingting.R;
import com.example.qingting.Utils.JsonUtils;
import com.example.qingting.Utils.ToastUtils;
import com.example.qingting.data.DB.MusicDB;
import com.example.qingting.data.DB.PlayListMusicDB;
import com.example.qingting.data.SP.LoginSP;
import com.example.qingting.net.CheckSuccess;
import com.example.qingting.net.request.PlayListMusicRequest.GetMusicRequest;
import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class PlayListMusicActivity extends AppCompatActivity {
    static final String TAG = PlayListMusicActivity.class.getName();
    RecyclerView playListMusicView;
    Integer playListId;
    List<Music> musicList;
    Context context;
    public final static String ID_INTENT_KEY = "play_list_id";

    static boolean isEnd;  // 判断activity是否结束，以避免空指针操作

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_list_music);
        isEnd = false;
        playListMusicView = findViewById(R.id.play_list_music);
        context = this;
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isEnd = true;
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
     * @return true初始化成功，false初始化失败
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
        GetMusicRequest.getAllPlayList(new RequestListener() {
            @Override
            public Object onPrepare(Object object) {
                return null;
            }

            @Override
            public void onRequest() {

            }

            @Override
            public void onReceive() {

            }

            @Override
            public void onSuccess(JsonElement element) throws Exception {
                CheckSuccess checkSuccess = new CheckSuccess() {
                    @Override
                    public void doWithSuccess(JsonElement data) {
                        List<Music> musicList1 = JsonUtils.getJsonParser().fromJson(data, new TypeToken<List<Music>>() {}.getType());
                        // 插入数据库
                        PlayListMusicDB.insertList(context, playListId, musicList1);
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            final SearchResultAdapter adapter = (SearchResultAdapter) playListMusicView.getAdapter();
                            @Override
                            public void run() {
                                // 避免子线程进行中activity已经结束的情况
                                if (!isEnd) {
                                    // 更新视图
                                    musicList = musicList1;
                                    adapter.updateData(musicList);
                                    ToastUtils.makeShortText(playListMusicView.getContext(), playListMusicView.getContext().getString(R.string.get_playlist_music_success));
                                }
                            }
                        });
                    }

                    @Override
                    public void doWithFailure() { }
                };
                checkSuccess.checkIfSuccess(element);
            }

            @Override
            public void onError(Exception e) {
                ToastUtils.makeShortText(context, context.getString(R.string.get_playlist_music_fail));
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onFinish() {

            }
        }, LoginSP.getToken(context), playListId);
        musicList = PlayListMusicDB.getMusicListDefault(context, playListId);
    }


    private void initRecyclerView() {
        if (playListId == null) return;
        playListMusicView.setAdapter(new SearchResultAdapter(musicList));
        playListMusicView.setLayoutManager(new LinearLayoutManager(context));
    }

}

