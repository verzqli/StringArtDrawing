package com.verzqli.stringartdrawing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("kkkkkk", "handleMessage: " + bestLinePoint.x + "  " + bestLinePoint.y);
            lineDrawView.setLine(bestLinePoint.x, bestLinePoint.y);
            lineDrawView.invalidate();
            isDraw = true;
        }
    };
    final Random random = new Random();
    private int LineCount;
    private int pointNum = 180;
    private int selectNum = 70;
    private int pixels[];
    private int width, height, radius;
    private LineDrawView lineDrawView;
    private List<DrawPoint> pointArray;
    private List<DrawPoint> pointArrayDraw;
    private int maxGray = 255;
    private Point bestLinePoint;
    private static boolean isDraw = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button rgb2greyBtn = findViewById(R.id.rgb2greybtn);
        ImageView imageView1 = findViewById(R.id.imageView1);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qqq);
        imageView1.setImageBitmap(bitmap);
        rgb2greyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                startDrawing();
            }
        });

        lineDrawView = findViewById(R.id.line_draw);
        lineDrawView.setCount(pointNum);
        convertGreyImg(bitmap);
        radius = (width > height ? height : width) / 2;

        pointArray = new ArrayList<>();
        pointArrayDraw = new ArrayList<>();
        for (int i = 0; i < pointNum; i++) {
            pointArray.add(new DrawPoint(radius + radius * Math.sin(2 * Math.PI * i / pointNum), radius - radius * Math.cos(2 * Math.PI * i / pointNum)));
        }
        pointArrayDraw.addAll(pointArray);
    }


    private void startDrawing() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isDraw) {
                        bestLinePoint = getPixelArrayFromLine();
                        fadeLineFromImg(pointArray.get(bestLinePoint.x).x, pointArray.get(bestLinePoint.x).y
                                , pointArray.get(bestLinePoint.y).x, pointArray.get(bestLinePoint.y).y);
                        handler.sendEmptyMessage(1);
                        LineCount++;
                        isDraw = false;
                    }
                }
//                    handler.sendEmptyMessage(1000);
            }
        }).start();
    }

    private Point getPixelArrayFromLine() {
        List<Point> linePointList = new ArrayList<>();
        for (int i = 0; i < selectNum; i++) {
            int startPoint = random.nextInt(pointNum);
            int endPoint = random.nextInt(pointNum);
            if (startPoint == endPoint) {
                i--;
            } else {
                linePointList.add(new Point(startPoint, endPoint));
            }
        }
        double minbrightness = 255;
        int bestIndex = 0;
        for (int i = 0, length = linePointList.size(); i < length; i++) {
            Point item = linePointList.get(i);
            double startX = pointArray.get(item.x).x;
            double startY = pointArray.get(item.x).y;

            double endX = pointArray.get(item.y).x;
            double endY = pointArray.get(item.y).y;

            double averageBrightness = getLineBrightness(startX, startY, endX, endY);
            if (averageBrightness < minbrightness) {
                bestIndex = i;
                minbrightness = averageBrightness;
            }
        }
        return linePointList.get(bestIndex);
    }

    private double getLineBrightness(double x1, double y1, double x2, double y2) {
        int result = 0;
        double distanceX = Math.floor(Math.abs(x1 - x2));
        double distanceY = Math.floor(Math.abs(y1 - y2));
        double distance = distanceX > distanceY ? distanceX : distanceY;
        for (int i = 0; i < distance; i++) {
            int startX = (int) Math.floor(x1 + (x2 - x1) * i / distance);
            int startY = (int) Math.floor(y1 + (y2 - y1) * i / distance);
            int b = getPiexl(startX, startY);
            result += b;
        }
        return result / distance;
    }


    private void fadeLineFromImg(double x1, double y1, double x2, double y2) {
        double distanceX = Math.floor(Math.abs(x1 - x2));
        double distanceY = Math.floor(Math.abs(y1 - y2));
        double distance = distanceX > distanceY ? distanceX : distanceY;
        for (int i = 0; i < distance; i++) {
            int startX = (int) Math.floor(x1 + (x2 - x1) * i / distance);
            int startY = (int) Math.floor(y1 + (y2 - y1) * i / distance);

            int index = startY * width + startX;
            if (index >= pixels.length) {
                index = pixels.length-1;
            }
            pixels[index] +=80;
        }
    }

    private int getPiexl(int startX, int startY) {
        int index = startY * width + startX;
        if (index >= pixels.length) {
            index = pixels.length-1;
        }
        return pixels[index];
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    public int[] convertGreyImg(Bitmap img) {
        width = img.getWidth();         //获取位图的宽
        height = img.getHeight();       //获取位图的高
//        pixels=new int[height][width];
        pixels = new int[width * height]; //通过位图的大小创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
//                grey = alpha | (grey << 16) | (grey << 8) | grey;

                pixels[width * i + j] = grey;
            }
        }
//        StringBuilder a = new StringBuilder();
//        for (int i = 0; i < height; i++) {
//            a.append("[");
//            for (int j = 0; j < width; j++) {
//                a.append(pixels[]).append(",");
//            }
//            a.append("]");
//            Log.i("aaaa", "convertGreyImg: " + a.toString());
//            a = new StringBuilder();
//        }
//        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        result.setPixels(pixels, 0, width, 0, 0, width, height);
        Toast.makeText(this, "轉換完畢", Toast.LENGTH_SHORT).show();
        return pixels;
    }

    public Bitmap gray2Binary(Bitmap graymap) {
        //得到图形的宽度和长度
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        //创建二值化图像
        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组

        graymap.getPixels(pixels, 0, width, 0, 0, width, height);
        //依次循环，对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到当前像素的值
                int col = pixels[width * i + j];
                //得到alpha通道的值
                int alpha = col & 0xFF000000;
                //得到图像的像素RGB的值
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                //对图像进行二值化处理
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 新的ARGB
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                //设置新图像的当前像素值
                pixels[width * i + j] = newColor;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }
}
