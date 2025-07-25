package com.example.qingting.adapter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.bean.Music;
import com.example.qingting.bean.PlayList;
import com.example.qingting.page.HomePage.SearchResultFragment;
import com.example.qingting.R;
import com.example.qingting.page.UserPage.PlayListMusicFragment;
import com.example.qingting.utils.CoroutineAdapter;
import com.example.qingting.utils.DialogUtils;
import com.example.qingting.utils.NetworkUtils;
import com.example.qingting.utils.Play.AudioPlayUtils;
import com.example.qingting.utils.ToastUtils;
import com.example.qingting.data.DB.PlayListMusicDB;
import com.example.qingting.data.SP.LoginSP;
import com.example.qingting.net.CheckSuccess;
import com.example.qingting.net.request.PlayListMusicRequest.AddMusicRequest;
import com.example.qingting.net.request.PlayListMusicRequest.DeleteMusicRequest;
import com.example.qingting.net.request.listener.RequestImpl;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import lombok.Setter;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    final static String TAG = SearchResultAdapter.class.getName();
    @Setter
    List<Music> musicList = new ArrayList<>();
    Integer playListId;  // 仅用于歌单展示音乐的fragment(PlayListMusicFragment)
    public SearchResultAdapter(Integer playListId, List<Music> musicList) {
        this.musicList = musicList;
        this.playListId = playListId;
        isFromDialog = false;
    }

    boolean isFromDialog;

    public SearchResultAdapter(boolean isFromDialog) {
        this.isFromDialog = isFromDialog;
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
        initView(holder.view, music);
        initPlayBtn(holder.playBtn, music);
        initAddBtn(holder.addBtn, music);
        initMenu(holder.menuBtn, music);
        holder.title.setText(music.getName());
        holder.genre.setText(music.getGenre());
    }


    private void initMenu(ImageView view, Music music) {
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        // 确保当前被绑定到哪里了: 1.SearchResultFragment 2. PlayListMusicFragment
        if (isFromDialog || SearchResultFragment.getInstance().isAdded()) {
            menu.inflate(R.menu.search_music_menu);
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.add_to_playlist) {
                        addToPlayList(view, music);
                    }
                    return false;
                }
            });
        }
        if (PlayListMusicFragment.getInstance(null).isAdded()) {
            menu.inflate(R.menu.playlist_music_menu);
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.add_to_playlist) {
                        addToPlayList(view, music);
                    }
                    if (item.getItemId() == R.id.delete_from_playlist) {
                        DialogUtils.showEnsureDialog(
                                view.getContext(),
                                view.getContext().getString(R.string.delete_from_playlist_title),
                                view.getContext().getString(R.string.delete_from_playlist_msg),
                                view.getContext().getString(R.string.sure_message),
                                view1 -> {
                                    List<Music> musicList1 = new ArrayList<>();
                                    musicList1.add(music);
                                    RequestImpl deleteMusicRequest = new DeleteMusicRequest() {
                                        @Override
                                        public void onSuccess(JsonElement element) throws Exception {
                                            new CheckSuccess() {
                                                @Override
                                                public void doWithSuccess(JsonElement data) {
                                                    if (PlayListMusicDB.deleteMusicsFromPlayList(view1.getContext(), playListId, musicList1) > 0) {
                                                        int index = musicList.indexOf(music);
                                                        musicList.remove(index);
                                                        final Handler handler = new Handler(Looper.getMainLooper());
                                                        handler.post(()->notifyItemRemoved(index));
                                                        ToastUtils.makeShortText(view1.getContext(), view1.getContext().getString(R.string.delete_from_playlist_success));
                                                    } else {
                                                        ToastUtils.makeShortText(view1.getContext(), view1.getContext().getString(R.string.delete_from_playlist_fail));
                                                    }
                                                }

                                                @Override
                                                public void doWithFailure() throws Exception {

                                                }
                                            }.checkIfSuccess(element);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.e(TAG, e.getMessage());
                                            ToastUtils.makeShortText(view1.getContext(), view1.getContext().getString(R.string.delete_from_playlist_fail));
                                        }
                                    };
                                    new CoroutineAdapter() {

                                        @Nullable
                                        @Override
                                        public Object runInCoroutineScope(@NonNull Continuation<? super Unit> $completion) {
                                            return deleteMusicRequest.request(new Object[]{LoginSP.getToken(view1.getContext()), playListId, musicList1}, $completion);
                                        }
                                    }.runInBlocking();
                                },
                                view
                        );
                    }
                    return false;
                }
            });
        }
        view.setOnClickListener(v->menu.show());
    }

    private void addToPlayList(View view, Music music) {
        List<Music> musicList1 = new ArrayList<>();
        musicList1.add(music);
        DialogUtils.showSelectPlayListDialog(view.getContext(), new DialogUtils.MyOnCancelListener() {
            @Override
            public void cancel(PlayList playlist) {
                if (playListId != null && Objects.equals(playlist.getId(), playListId)) {
                    ToastUtils.makeShortText(view.getContext(), view.getContext().getString(R.string.add_to_playlist_duplicate));
                    return;
                }
                RequestImpl addMusicRequest = new AddMusicRequest() {
                    @Override
                    public void onSuccess(JsonElement element) throws Exception {
                        CheckSuccess checkSuccess = new CheckSuccess() {
                            @Override
                            public void doWithSuccess(JsonElement data) {
                                PlayListMusicDB.insert(view.getContext(), playlist.getId(), music);
                                ToastUtils.makeShortText(view.getContext(), view.getContext().getString(R.string.add_to_playlist_success));
                            }

                            @Override
                            public void doWithFailure() {
                                ToastUtils.makeShortText(view.getContext(), view.getContext().getString(R.string.add_to_playlist_fail));
                            }
                        };
                        checkSuccess.checkIfSuccess(element);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                };
                new CoroutineAdapter() {
                    @Nullable
                    @Override
                    public Object runInCoroutineScope(@NonNull Continuation<? super Unit> $completion) {
                        return addMusicRequest.request(new Object[]{LoginSP.getToken(view.getContext()), playlist.getId(), musicList1}, $completion);
                    }
                }.runInBlocking();
            }
        });
    }

    private void initView(View view, Music music) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNow(music, v);
            }
        });
    }

    private void initPlayBtn(ImageView view, Music music) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNow(music, v);
            }
        });
    }

    private void initAddBtn(ImageView view, Music music) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(v.getContext())) {
                    AudioPlayUtils.addMusicToNext(music, null);
                    ToastUtils.makeShortText(v.getContext(), v.getResources().getString(R.string.add_music_to_next));
                    return;
                }
                ToastUtils.makeShortText(v.getContext(), v.getContext().getString(R.string.no_net));
            }
        });
    }


    /**
     * 马上播放音乐
     * @param music
     * @param view
     */
    private void playNow(Music music, View view) {
        if (NetworkUtils.isNetworkAvailable(view.getContext())) {
            AudioPlayUtils.playMusic(music, null);
            return;
        }
        ToastUtils.makeShortText(view.getContext(), view.getContext().getString(R.string.no_net));
    }


    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public void updateData(final List<Music> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }


}


class SearchResultViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView title;
    TextView genre;
    ImageView playBtn;
    ImageView addBtn;
    ImageView menuBtn;
    public SearchResultViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        title = view.findViewById(R.id.music_name);
        genre = view.findViewById(R.id.music_genre);
        playBtn = view.findViewById(R.id.play_btn);
        addBtn = view.findViewById(R.id.add_btn);
        menuBtn = view.findViewById(R.id.menu_btn);
    }
}
