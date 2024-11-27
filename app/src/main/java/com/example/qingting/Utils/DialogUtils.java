package com.example.qingting.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qingting.R;


public class DialogUtils {
    public static interface DialogCallback {
        public void doSth(View view, String input);
    }


    /**
     * 带输入的确认框
     * @param context
     * @param title
     * @param message
     * @param sureMessage
     * @param dialogCallback
     * @param view
     */
    public static void showInputDialog(Context context, String title, String message, String sureMessage, DialogCallback dialogCallback, View view) {
        // 创建 EditText 输入框
        final EditText input;
        input = (EditText) View.inflate(context, R.layout.input_edittext_layout, null);
        // 创建对话框
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setView(input) // 设置自定义视图
                .setPositiveButton(sureMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString(); // 获取输入内容
                        // 处理输入内容
                        Activity activity = (Activity) context;
                        activity.runOnUiThread(()->{
                            dialogCallback.doSth(view, userInput);
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // 取消对话框
                    }
                })
                .show(); // 显示对话框
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Window window = dialog.getWindow();
            window.setBackgroundBlurRadius(32);
            window.setBackgroundDrawableResource(R.drawable.dialog_background_shape);
            window.setIcon(R.drawable.app_icon);
        }
    }


    /**
     * 确认框
     * @param context
     * @param title
     * @param message
     * @param sureMessage
     * @param dialogCallback
     * @param view
     */
    public static void showEnsureDialog(Context context, String title, String message, String sureMessage, DialogCallback dialogCallback, View view) {
        // 创建对话框
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(sureMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理输入内容
                        Activity activity = (Activity) context;
                        activity.runOnUiThread(()->{
                            dialogCallback.doSth(view, null);
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // 取消对话框
                    }
                })
                .show(); // 显示对话框
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Window window = dialog.getWindow();
            window.setBackgroundBlurRadius(32);
            window.setBackgroundDrawableResource(R.drawable.dialog_background_shape);
            window.setIcon(R.drawable.app_icon);
        }
    }

}
