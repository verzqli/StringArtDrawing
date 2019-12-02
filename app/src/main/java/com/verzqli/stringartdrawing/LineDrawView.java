package com.verzqli.stringartdrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class LineDrawView extends View {
    private Bitmap cacheBitmap = null;
    private Canvas cacheCanvas = null;
    private int width;
    private int height;
    private Paint paint;
    private Paint bitmapPaint;
    private int count = 180;
    private ArrayList linePoint = new ArrayList(count);
    private int pointX, pointY;

    public LineDrawView(Context context) {
        this(context, null);
    }

    public LineDrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        bitmapPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cacheBitmap);
        int perimeter = (width + height) << 1;
        int distance = perimeter / count;
        for (int i = 0; i < count; i++) {
            int result = i * distance;
            if (result >= 0 && result < width) {
                linePoint.add(new Point(result, 0));
            } else if (result >= width && result < (width + height)) {
                linePoint.add(new Point(width, result - width));
            } else if (result >= (width + height) && result < (width * 2 + height)) {
                linePoint.add(new Point(width - (result - (width + height)), height));
            } else {
                linePoint.add(new Point(0, height - (result - (width * 2 + height))));
            }
        }
    }

    public LineDrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cacheBitmap, 0, 0, bitmapPaint);
    }

    public void setLine(int x, int y) {
        double angle = (Math.PI * 2 / count);
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = centerX;
        if (width > height) {
            radius = centerY;
        }

        cacheCanvas.drawLine((float) (centerX + radius * Math.sin(x * angle)), (float) (centerY - radius * Math.cos(x * angle)),
                (float) (centerX + radius * Math.sin(y * angle)), (float) (centerY - radius * Math.cos(y * angle)), paint);
//        cacheCanvas.drawLine(0,0,100,100,paint);
        invalidate();
    }

    public void setCount(int count) {
        this.count = count;
    }
}
