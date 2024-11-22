package com.example.qingting.Utils.Play;

public interface OnAudioPlayerListener {
        // 开始播放的回调事件，包括暂停后重新播放的情况
        void onStarted();
        // 暂停播放
        void onPaused();
        // 播放器被销毁
        void onStopped();
        // 播放器播放或获取资源出错
        void onError(String errorMessage);
        // 播放完毕
        void onComplete();
}