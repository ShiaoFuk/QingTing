package com.example.qingting.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class GalleryUtils {

    public static final int PICK_IMAGE_REQUEST = 1;

    /**
     * 启动相册选择图片
     *
     * @param activity 当前的 Activity
     */
    public static void openGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * 获取图片的绝对路径
     *
     * @param context 上下文
     * @param uri     图片的 Uri
     * @return 图片的绝对路径，如果未找到则返回 null
     */
    public static String getRealPathFromURI(Context context, Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(columnIndex);
            }
        }
        return result;
    }
}
