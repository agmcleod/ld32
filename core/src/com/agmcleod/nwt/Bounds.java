package com.agmcleod.nwt;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class Bounds extends Rectangle {
    public Bounds() {
        super();
    }
    public Bounds(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void add(Bounds b) {
        this.x += b.x;
        this.y += b.y;
    }

    public float getRight() {
        return x + width;
    }

    public float getTop() {
        return y + height;
    }

    public void sub(Bounds b) {
        this.x -= b.x;
        this.y -= b.y;
    }
}
