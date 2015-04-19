package com.agmcleod.nwt;

/**
 * Created by aaronmcleod on 15-04-19.
 */
public class Bear extends GameEntity {
    private boolean alive;
    private Direction direction;
    private final float SPEED = 500;
    private boolean stopped;
    public Bear(float x, float y, Direction direction) {
        super("bear.png");
        position.set(x, y);
        this.direction = direction;
        stopped = false;
        alive = true;
    }
    @Override
    public void collisionCallback() {
        stopped = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setAlive(boolean value) {
        alive = value;
    }

    public void update(float dt) {
        if (!stopped) {
            switch (direction) {
                case UP:
                    position.add(0, SPEED * dt);
                    break;
                case DOWN:
                    position.add(0, -SPEED * dt);
                    break;
                case LEFT:
                    position.add(-SPEED * dt, 0);
                    break;
                case RIGHT:
                    position.add(SPEED * dt, 0);
                    break;
            }
        }
    }
}
