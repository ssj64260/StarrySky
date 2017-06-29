package com.cxb.starrysky.model;

import android.graphics.Point;

/**
 * 圆形相关信息
 */

public class CircleInfo {

    private float angle;//所在角度
    private float radius;//圆的半径

    public CircleInfo(float angle ,float radius){
        this.angle = angle;
        this.radius = radius;
    }

    public Point CircularPoint(int originX, int originY) {
        Point circularPoint = new Point();
        circularPoint.x = (int) (originX + radius * Math.cos(angle * Math.PI / 180f));
        circularPoint.y = (int) (originY + radius * Math.sin(angle * Math.PI / 180f));
        return circularPoint;
    }
}
