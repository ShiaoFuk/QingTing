package com.example.qingting.page.UserPage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingting.adapter.SearchResultAdapter;
import com.example.qingting.bean.Music;
import com.example.qingting.customview.LoadingFactory;
import com.example.qingting.R;
import com.example.qingting.utils.CoroutineAdapter;
import com.example.qingting.utils.JsonUtils;
import com.example.qingting.utils.Play.AudioPlayUtils;
import com.example.qingting.utils.ToastUtils;
import com.example.qingting.data.DB.PlayListMusicDB;
import com.example.qingting.data.SP.LoginSP;
import com.example.qingting.net.CheckSuccess;
import com.example.qingting.net.request.PlayListMusicRequest.GetMusicRequest;
import com.example.qingting.net.request.listener.RequestImpl;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;


public class PlayListMusicFragment extends Fragment {
    static final String TAG = PlayListMusicFragment.class.getName();
    RecyclerView playListMusicView;
    Integer playListId;
    List<Music> musicList;
    Context context;
    static boolean isEnd;  // 判断activity是否结束，以避免空指针操作

    static PlayListMusicFragment fragment;

    View rootView;
    private PlayListMusicFragment() {

    }

    /**
     * 获取单例化的实例，仅第一次获取初始化，传入音乐列表的id
     * @param playListId 如果playListId为null则不会修改原来的playListId
     * @return fragment instance
     */
    public static PlayListMusicFragment getInstance(Integer playListId) {
        if (fragment == null) {
            fragment = new PlayListMusicFragment();
        }
        if (playListId != null) {
            fragment.playListId = playListId;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play_list_music, container, false);
        isEnd = false;
        playListMusicView = rootView.findViewById(R.id.play_list_music);
        context = rootView.getContext();
        init();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isEnd = true;
    }

    private void init() {
        LoadingFactory.loading(new LoadingFactory.LoadingViewParent(rootView.findViewById(R.id.loading_layout)) {
            @Override
            public boolean doSth() {
                if (initPlayListId()) {
                    // 初始化要做的事情
                    initPlayList();
                    initRecyclerView();
                    initTopBar();
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
        if (playListId == null || playListId == -1) {
            return false;
        }
        return true;
    }


    private void initPlayList() {
        if (playListId == null) return;
        RequestImpl getMusicRequest = new GetMusicRequest() {
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
                            SearchResultAdapter adapter = (SearchResultAdapter) playListMusicView.getAdapter();
                            @Override
                            public void run() {
                                // 避免子线程进行中activity已经结束的情况
                                if (!isEnd) {
                                    // 更新视图
                                    musicList = musicList1;
                                    if (adapter == null) {
                                        adapter = (SearchResultAdapter)PlayListMusicFragment.getInstance(null).playListMusicView.getAdapter();
                                    }
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
        };
        new CoroutineAdapter() {
            @Nullable
            @Override
            public Object runInCoroutineScope(@NonNull Continuation<? super Unit> $completion) {
                return getMusicRequest.request(new Object[]{LoginSP.getToken(context), playListId}, $completion);
            }
        }.runInBlocking();
        musicList = PlayListMusicDB.getMusicListDefault(context, playListId);
    }


    private void initRecyclerView() {
        if (playListId == null) return;
        playListMusicView.setAdapter(new SearchResultAdapter(playListId, musicList));
        playListMusicView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initTopBar() {
        View view = rootView.findViewById(R.id.top_bar);
        view.setOnClickListener((v)->{
            // 当前歌单添加到播放列表
            AudioPlayUtils.playMusicList(musicList);
        });
    }
}