package com.example.qingting.Utils.Play;

public interface OnAudioPlayerListener {
        void onStarted();
        void onPaused();
        void onStopped();
        void onError(String errorMessage);
        void onComplete();
}