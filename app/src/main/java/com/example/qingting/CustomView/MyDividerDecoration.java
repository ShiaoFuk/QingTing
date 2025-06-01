package com.example.qingting.CustomView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MyDividerDecoration extends RecyclerView.ItemDecoration {
    final Paint paint = new Paint();

    int orientation;


    /**
     * @param orientation 选择RecyclerView的方向，
     *                    取值为：{@link RecyclerView#VERTICAL} 或 {@link RecyclerView#HORIZONTAL}
     * @param dividerLength 分割线的长度
     * @param color 分割线的颜色，默认黑色
     */
    public MyDividerDecoration(int orientation, int dividerLength, int color) {
        super();
        if (orientation != RecyclerView.VERTICAL && orientation != RecyclerView.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的方向参数！");
        }
        paint.setColor(color);
//        paint.setColor(Color.BLACK);  // debug测试分割线是否生效
        this.orientation = orientation;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) dividerLength);
    }

    /**
     * 等同于{@link #MyDividerDecoration(int, int, int)}，
     */
    public MyDividerDecoration(int orientation, int dividerLength) {
        this(orientation, dividerLength, Color.BLACK);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (orientation == RecyclerView.VERTICAL) {
                c.drawLine(child.getLeft(), child.getTop(), child.getRight(), child.getTop(), paint);
                c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), paint);
            } else if (orientation == RecyclerView.HORIZONTAL) {
                c.drawLine(child.getLeft(), child.getTop(), child.getLeft(), child.getBottom(), paint);
                c.drawLine(child.getRight(), child.getTop(), child.getRight(), child.getBottom(), paint);
            }
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
