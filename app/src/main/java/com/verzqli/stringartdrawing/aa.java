//// The count of points
//var pointNum = 180;
//// precision is between 1 and 100, the more precision would cost more time.
//        var precision = 50;
//// The count of lines
//        var lineNum = 4000;
//        var linecount = 0;
//
//        var radius = 200;
//        var pointPos = [];
//        var pointPosDraw = [];
//        var img;
//        var fadeStep = 50;
//        var lineAlpha = 0.3;
//        var substep = 10;
//
//
//        function preload() {
//        img = loadImage("test.png");
//        }
//
//        function setup() {
//        createCanvas(900, 500);
//        img.resize(500, 500);
//        img.loadPixels();
//        for (var i = 0 ; i < pointNum ; i++) {
//        pointPos[i]= createVector(img.width / 2 * sin(2*PI*i/pointNum) + img.width / 2, img.height / 2 * cos(2*PI*i/pointNum) + img.height / 2);
//        pointPosDraw[i] = createVector(radius * sin(2*PI*i/pointNum) + width / 2, radius * cos(2*PI*i/pointNum) + height / 2);
//        }
//        background(255, 245, 232);
//        }
//
//        function draw() {
//        console.log(frameRate());
//        for (var i = 0 ; i < substep ; i++) {
//        console.log(linecount);
//        if (linecount < lineNum) {
//        pointIndexs = getBestPointPair();
//        var p1 = pointIndexs[0];
//        var p2 = pointIndexs[1];
//        strokeWeight(0.5);
//        stroke(0, lineAlpha*255);
//        line(pointPosDraw[p1].x, pointPosDraw[p1].y, pointPosDraw[p2].x, pointPosDraw[p2].y);
//        fadeLineFromImg(pointPos[p1].x, pointPos[p1].y, pointPos[p2].x, pointPos[p2].y);
//        linecount++;
//
//        drawProgress(true);
//
//        /* Uncomment to Show Origin Image */
//        //img.updatePixels();
//        //image(img, 0, 0, 200, 200);
//
//        } else {
//        noLoop();
//        break;
//        }
//        }
//        }
//
///**
// * Draw Progress
// *
// * @param flag {boolean} Draw or Not Draw
// * @return {void}
// */
//        function drawProgress(flag) {
//        var progressWidth = 300;
//        var progressHeight = 12;
//        push();
//        translate(width/2, height-20);
//        noStroke();
//        fill(255, 245, 232);
//        rect(-progressWidth/2-1, -progressHeight/2-1, progressWidth+2, progressHeight+2)
//        if (flag) {
//        rectMode(CORNER);
//        var progress = linecount / lineNum;
//        fill(0, 20);
//        rect(-progressWidth/2, -progressHeight/2, progressWidth, progressHeight);
//        fill(0, 100);
//        rect(-progressWidth/2, -progressHeight/2, progressWidth*progress, progressHeight);
//        }
//        pop();
//        }
//
//
///**
// * Get Two Best Points for Connecting
// *
// * @return {Array} Return An Array Includes Two Point Indices
// */
//        function getBestPointPair() {
//        var selectedPointPair = [];
//        var selectedNum = precision;
//        for (var i = 0 ; i < selectedNum ; i++) {
//        var p1 = floor(random(1)*pointNum);
//        var p2 = floor(random(1)*pointNum);
//        if (p1 == p2) {
//        i--;
//        } else {
//        selectedPointPair[i] = [p1, p2];
//        }
//        }
//        var minBrightness = 255;
//        var bestIndex = 0;
//        for (var k = 0 ; k < selectedPointPair.length ; k++) {
//        var p1 = selectedPointPair[k][0];
//        var p2 = selectedPointPair[k][1];
//        var pointX1 = pointPos[p1].x;
//        var pointY1 = pointPos[p1].y;
//        var pointX2 = pointPos[p2].x;
//        var pointY2 = pointPos[p2].y;
//        var b = getLineBrightness(pointX1, pointY1, pointX2, pointY2);
//        if (b < minBrightness) {
//        bestIndex = k;
//        minBrightness = b;
//        }
//        }
//        return selectedPointPair[bestIndex];
//        }
//
///**
// * Get the Average Brightness of the Line Cross pointA[x1, y1] and pointB[x2, y2]
// *
// * @param x1 {numver} X of PointA
// * @param y1 {numver} Y of PointA
// * @param x2 {numver} X of PointB
// * @param y2 {numver} Y of PointB
// * @return {number} Average Brightness
// */
//        function getLineBrightness(x1, y1, x2, y2) {
//        var result = 0;
//        var xOffset = floor(abs(x1 - x2));
//        var yOffset = floor(abs(y1 - y2));
//        var step = xOffset < yOffset ? yOffset : xOffset;
//        for (var i = 0 ; i < step ; i++) {
//        var x = floor(x1 + (x2 - x1) * i / step);
//        var y = floor(y1 + (y2 - y1) * i / step);
//        var b = brightness(fget(x, y));
//        result += b;
//        }
//        return result / step;
//        }
//
///**
// * Fade the Pixels of the Line Cross pointA[x1, y1] and pointB[x2, y2]
// *
// * @param x1 {numver} X of PointA
// * @param y1 {numver} Y of PointA
// * @param x2 {numver} X of PointB
// * @param y2 {numver} Y of PointB
// * @return {void}
// */
//        function fadeLineFromImg(x1, y1, x2, y2) {
//        var xOffset = floor(abs(x1 - x2));
//        var yOffset = floor(abs(y1 - y2));
//        var step = xOffset < yOffset ? yOffset : xOffset;
//        for (var i = 0 ; i < step ; i++) {
//        var x = floor(x1 + (x2 - x1) * i / step);
//        var y = floor(y1 + (y2 - y1) * i / step);
//
//        var index = y * img.width + x;
//        index *= 4;
//        img.pixels[index] += fadeStep;
//        img.pixels[index+1] += fadeStep;
//        img.pixels[index+2] += fadeStep;
//        }
//        }
//
///**
// * Fast Get Function for P5.js
// *
// * @param i {numver} pixelX
// * @param j {numver} pixelY
// * @return {color} Color of Pixel[x, y]
// */
//        function fget(i, j) {
//        var index = j * img.width + i;
//        index *= 4;
//        return color(img.pixels[index], img.pixels[index+1], img.pixels[index+2], img.pixels[index+3]);
//        }