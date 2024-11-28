package com.example.qingting.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.Bean.Music;
import com.example.qingting.Bean.PlayList;
import com.example.qingting.MainActivity;
import com.example.qingting.MyApplication;
import com.example.qingting.R;
import com.example.qingting.UserPage.PlayListMusicFragment;
import com.example.qingting.Utils.DialogUtils;
import com.example.qingting.Utils.ImageLoadUtils;
import com.example.qingting.Utils.Play.AudioPlayUtils;
import com.example.qingting.Utils.ToastUtils;
import com.example.qingting.data.DB.PlayListDB;
import com.example.qingting.data.DB.PlayListMusicDB;
import com.example.qingting.data.SP.LoginSP;
import com.example.qingting.net.request.PlayListRequest.DeletePlayListRequest;
import com.example.qingting.net.request.PlayListRequest.UpdatePlayListRequest;
import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter<PlayListRecyclerViewViewHolder> {
    final static String TAG = PlayListRecyclerViewAdapter.class.getName();
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
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加fragment，同时设置navigation_bar不可见
                MainActivity.addFragment(PlayListMusicFragment.getInstance(playList.getId()));
                MainActivity.setNavigationBarVisibility(View.GONE);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogUtils.showInputDialog(holder.view.getContext(),
                        holder.view.getResources().getString(R.string.update_playlist_title),
                        holder.view.getResources().getString(R.string.update_playlist_message),
                        holder.view.getResources().getString(R.string.sure_message),
                        new DialogUtils.DialogCallback() {
                            @Override
                            public void doSth(View view, String input) {
                                UpdatePlayListRequest.deletePlayList(new RequestListener() {
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
                                        if (element.getAsJsonObject().get("code").getAsInt() == 200) {
                                            final Handler handler = new Handler(Looper.getMainLooper());
                                            MyApplication.getPlayListFromNet(holder.view.getContext());
                                            handler.post(()->{
                                                // 更新成功
                                                playListList.get(holder.getAdapterPosition()).setName(input);
                                                notifyItemChanged(holder.getAdapterPosition());
                                                ToastUtils.makeShortText(holder.view.getContext(), holder.view.getResources().getString(R.string.update_playlist_success));
                                            });
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        ToastUtils.makeShortText(holder.view.getContext(), holder.view.getResources().getString(R.string.update_playlist_fail));
                                        Log.e(TAG, e.getMessage());
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                }, LoginSP.getToken(holder.view.getContext()), playList.getId(), input);
                            }
                        }, holder.view
                );
                return false;
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
        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Music> musicList = PlayListMusicDB.getMusicListDefault(v.getContext(), playList.getId());
                AudioPlayUtils.playMusicList(musicList);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showEnsureDialog(holder.deleteBtn.getContext(),
                        holder.deleteBtn.getContext().getString(R.string.delete_playlist_title),
                        holder.deleteBtn.getContext().getString(R.string.delete_playlist_message),
                        holder.deleteBtn.getContext().getString(R.string.sure_message),
                        new DialogUtils.DialogCallback() {
                            @Override
                            public void doSth(View view, String input) {
                                DeletePlayListRequest.deletePlayList(new RequestListener() {
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
                                        JsonObject jsonObject = element.getAsJsonObject();
                                        if (jsonObject.get("code") != null && jsonObject.get("code").getAsInt() == 200) {
                                            PlayListDB.deletePlayList(v.getContext(), playList);
                                            ToastUtils.makeShortText(v.getContext(), v.getResources().getString(R.string.operate_playlist_success));
                                            MyApplication.deletePlayList(playList);
                                            final Handler handler = new android.os.Handler(Looper.getMainLooper());
                                            handler.post(() -> {
                                                notifyDataSetChanged();
                                            });
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        ToastUtils.makeShortText(v.getContext(), v.getResources().getString(R.string.operate_playlist_fail));
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                }, LoginSP.getToken(v.getContext()), playList.getId());
                            }
                        }, null);
            }
        });
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
    TextView playListName;
    TextView playListInfo;
    ImageView playBtn;
    ImageView deleteBtn;
    public PlayListRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.imageView = itemView.findViewById(R.id.play_list_img);
        this.playListName = itemView.findViewById(R.id.play_list_name);
        this.playListInfo = itemView.findViewById(R.id.play_list_info);
        this.playBtn = itemView.findViewById(R.id.play_btn);
        this.deleteBtn = itemView.findViewById(R.id.delete_btn);
    }
}
