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

    public Bounds(Bounds other) {
        set(other.x, other.y, other.width, other.height);
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

    public boolean bottomOverlapsWith(Bounds r) {
        return getTop() > r.getTop() && getY() < r.getTop();
    }

    public boolean leftOverlapsWith(Bounds r) {
        return getRight() > r.getRight() && getX() < r.getRight();
    }

    public boolean rightOverlapsWith(Bounds r) {
        return getX() < r.getX() && getRight() > r.getX();
    }

    public boolean topOverlapsWith(Bounds r) {
        return getY() < r.getY() && getTop() > r.getY();
    }

    public void sub(Bounds b) {
        this.x -= b.x;
        this.y -= b.y;
    }
}
