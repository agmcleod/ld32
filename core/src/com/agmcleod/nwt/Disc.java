package com.agmcleod.nwt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class Disc extends GameEntity {
    private boolean alive;
    private Animation appearAnimation;
    private TextureRegion currentFrame;
    private float animationState;
    public Disc() {
        super("disc.png");
        alive = false;
        bounds.set(0, 0, 80, 80);
        width = 80;
        height = 80;

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight() / 2);
        TextureRegion[] appearFrames = new TextureRegion[4];

        appearFrames[0] = tmp[0][1];
        appearFrames[1] = tmp[1][0];
        appearFrames[2] = tmp[1][1];
        appearFrames[3] = tmp[0][0];

        appearAnimation = new Animation(0.15f, appearFrames);
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, position.x, position.y, width, height);
    }

    public void setAlive(boolean value) {
        alive = true;
        animationState = 0f;
    }

    public void update(float dt) {
        animationState += dt;
        currentFrame = appearAnimation.getKeyFrame(animationState, false);
    }
}
