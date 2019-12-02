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
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lineDrawView.setLine(random.nextInt(point), random.nextInt(point));
            handler.sendEmptyMessage(100);
        }
    };
    final Random random = new Random();
    private int LineCount;
    private int point = 180;
    private List<Pair<Integer, Integer>> pairList = new ArrayList(64);
    private List<Integer> shuffleList;
    private int grayValue[][];
    private LineDrawView lineDrawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button rgb2greyBtn = findViewById(R.id.rgb2greybtn);
        ImageView imageView1 = findViewById(R.id.imageView1);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        imageView1.setImageBitmap(bitmap);
        rgb2greyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                convertGreyImg(bitmap);
                startDrawing();
            }
        });
        shuffleList = new ArrayList<>(200);
        lineDrawView = findViewById(R.id.line_draw);
        lineDrawView.setCount(point);
        for (int i = 0; i < point; i++) {
            shuffleList.set(i, i);
        }

//        startDrawing();
//        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private void startDrawing() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Collections.shuffle(shuffleList);
                    for (int i = 0, length = pairList.size(); i < length; i++) {
                        pairList.set(i, new Pair<>(shuffleList.get(i), shuffleList.get(i + point / 2)));
                    }
                    for (int i = 0, length2 = pairList.size(); i < length2; i++) {
//                        int slope = pairList
                    }

                    LineCount++;
                    handler.sendEmptyMessage(100);
                }
            }
        }).start();
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    public int[] convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高
//        pixels=new int[height][width];
        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组

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

//                pixels[width * i + j] = grey;
                grayValue[height][width] = grey;
            }
        }
//        StringBuilder a = new StringBuilder();
//        for (int i = 0; i < height; i++) {
//            a.append("[");
//            for (int j = 0; j < width; j++) {
//                a.append(pixels[width * i + j]).append(",");
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
