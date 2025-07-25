package com.example.qingting.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.qingting.R;

public class ImageLoadUtils {
    public final static int DEFAULT_SRC_ID = R.drawable.note;
    public static void loadFromNet(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(DEFAULT_SRC_ID) // 加载中占位图
                .error(DEFAULT_SRC_ID)             // 加载失败占位图
                .into(imageView);
    }


    public static void loadFromLocal(ImageView imageView, String filePath) {
        Uri localUri = Uri.parse(filePath);
        Glide.with(imageView.getContext())
                .load(localUri)
                .placeholder(DEFAULT_SRC_ID)
                .error(DEFAULT_SRC_ID)
                .into(imageView);
    }


}
