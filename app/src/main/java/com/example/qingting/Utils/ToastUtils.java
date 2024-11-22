package com.example.qingting.Utils;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qingting.R;


/**
 * Toast类，Toast必须使用这个类发出，以确保整个App的Toast行为一致（包括ui、时长等）
 */
public class ToastUtils {
    private static Toast toast = null;
    private static TextView textView = null;
    private static LinearLayout layout = null;
    private static Toast getToastInstance(Context context) {
        if (toast == null) {
            Context context1 = context.getApplicationContext();
            View view = LayoutInflater.from(context1).inflate(R.layout.toast_layout, null, false);
            toast = new Toast(context1);
            textView = view.findViewById(R.id.toast_text);
            layout = view.findViewById(R.id.toast_layout);
            toast.setView(view);
        }
        return toast;
    }

    private static void setText(String text) {
        textView.setText(text);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }


    /**
     * 注意content不要太长，Toast的高度有限制，即使wrapContent也无法太长，换行之后第二行的字会看不全
     * @param context
     * @param content
     */
    public static void makeShortText(Context context, String content) {
        Activity activity = (Activity) context;
        activity.runOnUiThread(()->{
            Toast toast1 = getToastInstance(context);
            toast1.setDuration(Toast.LENGTH_SHORT);
            setText(content);
            toast1.show();
        });
    }

    public static void makeLongText(Context context, String content) {
        Activity activity = (Activity) context;
        activity.runOnUiThread(()->{
            Toast toast1 = getToastInstance(context);
            toast1.setDuration(Toast.LENGTH_LONG);
            setText(content);
            toast1.show();
        });
    }
}
