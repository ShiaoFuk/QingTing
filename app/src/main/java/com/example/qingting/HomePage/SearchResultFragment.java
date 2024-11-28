package com.example.qingting.HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qingting.Adapter.SearchResultAdapter;
import com.example.qingting.Bean.Music;
import com.example.qingting.R;
import com.example.qingting.Utils.JsonUtils;
import com.example.qingting.net.request.MusicRequest;
import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class SearchResultFragment extends Fragment {
    static SearchResultFragment fragment;
    final static String TAG = SearchResultFragment.class.getName();
    final static String CONTENT_KEY = "content";
    View rootView;
    View loadingView;
    TextView loadingText;
    private Handler handler = new Handler(Looper.getMainLooper());
    private SearchResultFragment() {

    }


    static SearchResultFragment getInstance() {
        if (fragment == null)
            fragment = new SearchResultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        loadingView = rootView.findViewById(R.id.loading_view);
        loadingText = rootView.findViewById(R.id.loading_text);
        init();
        return rootView;
    }

    private void init() {
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        String content = bundle.getString(CONTENT_KEY);
        RecyclerView recyclerView = rootView.findViewById(R.id.music_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(new SearchResultAdapter());
        MusicRequest.getMusic(new RequestListener() {
            @Override
            public Object onPrepare(Object object) {
                return object;
            }

            @Override
            public void onRequest() {
                // 添加loading
                handler.post(()->{
                    loadingView.setVisibility(View.VISIBLE);
                    loadingText.setVisibility(View.VISIBLE);
                });

            }

            @Override
            public void onReceive() {
                // 移除loading
                handler.post(()->{
                    loadingView.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                });
            }

            @Override
            public void onSuccess(JsonElement element) throws Exception {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.get("code").getAsInt() == 200) {
                    String jsonArray = jsonObject.get("data").toString();
                    Type personListType = new TypeToken<List<Music>>(){}.getType();
                    List<Music> musicList = JsonUtils.getJsonParser().fromJson(jsonArray, personListType);
                    handler.post(()->{
                        SearchResultAdapter adapter = (SearchResultAdapter) recyclerView.getAdapter();
                        adapter.setMusicList(musicList);
                        adapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.getMessage());
                // 添加错误标识
                handler.post(()->{
                    loadingView.setVisibility(View.VISIBLE);
                    loadingText.setText(getText(R.string.load_error));
                    loadingText.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onFinish() {
            }
        }, content);
    }

}