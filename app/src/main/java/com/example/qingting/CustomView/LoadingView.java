package com.example.qingting.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.qingting.R;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoadingView extends View {
    Paint paint;
    private float maxRadius;
    private float actualRadius;
    private float increaseVal;
    private int[] centerColor;
    private int[] outerColor;
    private float[] transparent;
    private float interval;  // 每10ms增长的半径
    private float innerRadius;
    private float[] radiusList;
    private float r1;
    private ScheduledExecutorService scheduledExecutorService;
    private Runnable drawRunnable;
    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        radiusList = new float[] {0f, -1f, -1f};
        increaseVal = -1f;
        actualRadius = -1f;
        initColor();
        r1 = 2f;
        maxRadius = 3f;
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        drawRunnable = new Runnable() {
            @Override
            public void run() {
                // 实心圆半径变化
                if (Math.abs(actualRadius - -1f) < 10e-6) {
                    actualRadius = innerRadius;
                }
                actualRadius += increaseVal;
                int coff = 20;
                float range = Math.abs(increaseVal) * coff;  // 半径变化范围
                if (Math.abs(actualRadius - innerRadius) > range) {
                    actualRadius = innerRadius + increaseVal * coff;
                    increaseVal = -increaseVal;
                }
                // 波纹半径变化
                for (int i = 0; i < radiusList.length; ++i) {
                    if (radiusList[i] > -1f) {
                        // 未初始化为-1
                        if (Math.abs(radiusList[i] - 0f) < 10e-6) {
                            radiusList[i] = innerRadius;  // 为0的初始化
                        }
                        radiusList[i] += interval * (1f + (radiusList[i] - innerRadius) / (maxRadius - innerRadius));  // 加速增长
                        transparent[i] = (1f - (radiusList[i] - innerRadius) / (maxRadius - innerRadius));
                        if (radiusList[i] > r1) {
                            if (i + 1 < radiusList.length && Math.abs(radiusList[i + 1] - -1f) < 10e-6) {
                                radiusList[i + 1] = innerRadius;
                            }

                            else if (radiusList[i] > maxRadius) {
                                if (i == radiusList.length - 1) {
                                    // 最后一个达到最大
                                    radiusList = new float[] {innerRadius, -1, -1f};
                                    break;
                                } else {
                                    radiusList[i] = -1;
                                }
                            }
                        }
                    }
                }
                invalidate(); // Trigger a redraw
            }
        };
    }

    private void initColor() {
        centerColor = new int[] { 118, 194, 175 };;
        transparent = new float[3];
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
//        super.onDraw(canvas);
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        float cx = width / 2f;
        float cy = height / 2f;
        // 计算半径
        maxRadius = (cx > cy) ? cy : cx;  // 取宽高较小值为radius
        innerRadius = (77f / 164f) * maxRadius;
        interval = (maxRadius - innerRadius) / 100f;
        if (Math.abs(increaseVal - -1f) < 10e-6) {
            increaseVal = interval / 5f;
        }
        r1 = (100f / 164f) * maxRadius;


        scheduledExecutorService.schedule(drawRunnable, 1000 / 100, TimeUnit.MILLISECONDS);

        // 绘制外部波纹
        // 设定r1 r2, r1 < r2，这里r2是maxRadius
        // 每过10ms半径增长，增长到r1时下一道出现，增长到r2时消失
        // 共3道波纹，3道都消失时重置
        for (int i = 0; i < radiusList.length; ++i) {
            paint.setColor(Color.argb(transparent[i], centerColor[0] / 255f, centerColor[1] / 255f, centerColor[2] / 255f));
            canvas.drawCircle(cx, cy, radiusList[i], paint);
        }
        Log.e("gg", ""+transparent[0] + transparent[1] + transparent[2]);
        // 绘制中心实心圆
        paint.setColor(Color.rgb(centerColor[0], centerColor[1], centerColor[2]));
        canvas.drawCircle(cx, cy, actualRadius, paint);
    }
}