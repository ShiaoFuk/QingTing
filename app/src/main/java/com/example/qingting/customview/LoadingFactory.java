package com.example.qingting.customview;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.qingting.R;

/**
 * 用于loading，只需要传入loadingLayout，实现loading时候的逻辑，会在执行任务前开始loading，执行完任务后正确处理loading的后续状态
 */
public class LoadingFactory {
    public static void loading(LoadingViewParent loadingViewParent) {
        loadingViewParent.loading();
        if (loadingViewParent.doSth()) {
            loadingViewParent.finishLoading();
        } else {
            loadingViewParent.errorLoading();
        }
    }

    /**
     * 实现需要做的任务在dosth中
     */
    public static abstract class LoadingViewParent implements Loading {
        LoadingViewController controller;
        Context context;

        public LoadingViewParent(View loadingView) {
            this.controller = new LoadingViewController(loadingView);
            context = loadingView.getContext();
            this.controller.setText(context.getString(R.string.loading));
        }

        @Override
        public void loading() {
            controller.setVisibility(View.VISIBLE);
        }

        /**
         * 执行一个任务
         * @return 任务失败返回false，任务成功返回true
         */
        @Override
        public abstract boolean doSth();


        @Override
        public void finishLoading() {
            controller.setVisibility(View.GONE);
        }

        @Override
        public void errorLoading() {
            controller.setVisibility(View.VISIBLE);
            controller.setText(context.getString(R.string.load_error));
        }
    }
}

class LoadingViewController {
    View loadingView;
    TextView tipText;

    /**
     * 初始化实例，默认可见性为GONE
     * @param loadingLayout
     */
    LoadingViewController(View loadingLayout) {
        this.loadingView = loadingLayout;
        this.tipText = this.loadingView.findViewById(R.id.loading_text);
        this.loadingView.setVisibility(View.GONE);
    }

    /**
     * 设置layout的提示文字
     * @param text 要设置的提示文字字符串s
     */
    public void setText(String text) {
        tipText.setText(text);
    }

    /**
     * 设置整个layout可见状态
     * @param visibility
     */
    public void setVisibility(int visibility) {
        loadingView.setVisibility(visibility);
    }

}


interface Loading {
    void loading();

    boolean doSth();

    void finishLoading();

    void errorLoading();
}




