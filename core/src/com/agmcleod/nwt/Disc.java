package com.agmcleod.nwt;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class Disc extends GameEntity {
    private boolean alive;
    public Disc() {
        super("disc.png");
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean value) {
        alive = true;
    }
}
