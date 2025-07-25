package com.example.qingting.aop;

import android.util.Log;

import java.util.Date;

public class TimeCostTest {
    final static String TAG = TimeCostTest.class.getName();
    public static interface Task {
        void doSomeThing();
    }

    /**
     * 实现task为要执行的任务，会打印执行前后的时间
     * @param task 要执行的任务
     * @param message 作为区分不同任务的文字，传入null则不会打印message
     */
    public static void test(Task task, String message) {
        long startTime = new Date().getTime();
        task.doSomeThing();
        startTime = new Date().getTime() - startTime;
        Log.d(TAG, String.format("task %s time cost = %d ms", message, startTime));
    }
}
