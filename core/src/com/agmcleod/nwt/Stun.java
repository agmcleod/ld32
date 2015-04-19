package com.agmcleod.nwt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class Stun {
    private TextureRegion currentFrame;
    private Animation animation;
    private Texture texture;
    private final int W = 100;
    private final int H = 100;
    private Vector2 position;
    private float state;

    public Stun(float x, float y) {
        this.texture = new Texture("stun.png");
        position = new Vector2(x, y);

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight() / 2);
        TextureRegion[] frames = new TextureRegion[4];
        frames[0] = tmp[0][0];
        frames[1] = tmp[0][1];
        frames[2] = tmp[1][0];
        frames[3] = tmp[1][1];
        state = 0f;
        animation = new Animation(0.1f, frames);
    }

    public void dispose() {
        this.texture.dispose();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, position.x, position.y, W, H);
    }

    public boolean update(float dt) {
        state += dt;
        currentFrame = animation.getKeyFrame(state, false);
        return animation.isAnimationFinished(state);
    }
}
