//package com.verzqli.stringartdrawing;
//
//import android.util.Log;
//import android.util.Pair;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class a {
//    private void startDrawing() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    Collections.shuffle(shuffleList);
//                    pairList.clear();
//                    minGray = 0;
//                    for (int i = 0; i < 80; i++) {
//                        pairList.add(new Pair<>(shuffleList.get(i), shuffleList.get(i + 80)));
//                    }
//                    for (int i = 0, length2 = pairList.size(); i < length2; i++) {
//                        int startPoint = pairList.get(i).first;
//                        int endPoint = pairList.get(i).second;
//                        int startX = (int) (radius + radius * Math.sin(startPoint * angle));
//                        int startY = (int) (radius - radius * Math.cos(startPoint * angle));
//                        int endX = (int) (radius + radius * Math.sin(endPoint * angle));
//                        int endY = (int) (radius - radius * Math.cos(endPoint * angle));
//                        int distanceX = endX - startX;
//                        int distanceY = endY - startY;
//                        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
//                        double angle = Math.asin(Math.abs(distanceY) / distance);
//                        if (startX > endX) {
//                            int a = startX;
//                            startX = endX;
//                            endX = a;
//                        }
//                        if (startY > endY) {
//                            int b = startY;
//                            startY = endY;
//                            endY = b;
//                        }
//                        int grey = 0;
//                        int count = 0;
//                        List<Pair<Integer, Integer>> list = new ArrayList<>();
//                        for (int j = 1; j < distance; j++) {
//                            int x = startX + (int) (j * Math.cos(angle));
//                            int y = startY + (int) (j * Math.sin(angle));
//                            list.add(new Pair<>(y, x));
//                            grey += grayValue[y][x];
//                            count++;
////                            Log.i("mmmmmm" + distance, "run: " + x + "  " + y + "   " + grayValue[y][x]);
//                        }
////                    Log.i("aaaaaa", "x=" + startPoint + ",y=" + endPoint);
////                    Log.i("aaaaaa", "grey=" + grey);
//                        float aver = grey * 1f / count;
//                        Log.i("aaaaaa", "mingray=" + minGray + "aver=" + aver);
//                        if (minGray == 0) {
//                            minGray = aver;
//                            minStartPoint = startPoint;
//                            minEndPoint = endPoint;
//                        } else {
//                            if (minGray > aver) {
//                                minGray = aver;
//                                minStartPoint = startPoint;
//                                minEndPoint = endPoint;
//                                minangle = angle;
//                                minLinePoint = list;
//                                Log.i("gggggg", "aver=" + minStartPoint + "   " + endPoint);
//                            }
//                        }
//                    }
//                    int z, x, y;
//                    for (int i = 0, length = minLinePoint.size(); i < length; i++) {
//                        x = minLinePoint.get(i).first;
//                        y = minLinePoint.get(i).second;
//                        z = grayValue[x][y];
//                        z += 80;
//                        if (z > 255) {
//                            z = 255;
//                        }
//                        grayValue[x][y] = z;
//                    }
//                    handler.sendEmptyMessage(1);
//                    LineCount++;
//
//                }
////                    handler.sendEmptyMessage(1000);
//            }
//        }).start();
//    }
//}
