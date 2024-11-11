package com.example.qingting.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.core.app.ActivityCompat;

public class RecordUtils {
    public final static int SAMPLE_RATE = 44100;
    public final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    public final static int CHANNEL = CHANNEL_CONFIG == AudioFormat.CHANNEL_IN_MONO ? 1 : 2;
    public final static int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AudioFormat.ENCODING_PCM_16BIT);  // 最小录音时长
    private static AudioRecord record;
    public static AudioRecord getInstance(Context context) {
        if (record == null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            record = new AudioRecord(
                    MediaRecorder.AudioSource.DEFAULT,
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);
        } else {
            record.stop();
            record.release();
            record = new AudioRecord(
                    MediaRecorder.AudioSource.DEFAULT,
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);
        }
        return record;
    }

    // 用于activity destroy的时候释放资源，必须手动释放！！！
    public static void release() {
        if (record != null) {
            record.stop();
            record.release();
            record = null;
        }
    }

}
