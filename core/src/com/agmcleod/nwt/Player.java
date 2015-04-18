package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-17.
 */
public class Player extends GameEntity {
    Vector2 velocity;
    private final int SPEED = 350;
    private TextureRegion currentFrame;
    private Animation walkRightAnimation;
    private float stateTime = 0f;
    private boolean flip;

    public Player() {
        super("player.png");
        velocity = new Vector2();
        position.set(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 50);
        width = 100;
        height = 100;
        bounds.set(20, 0, 50, 100);

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight() / 3);
        TextureRegion[] walkRightFrames = new TextureRegion[4];
        walkRightFrames[0] = tmp[0][0];
        walkRightFrames[1] = tmp[1][0];
        walkRightFrames[2] = tmp[2][0];
        walkRightFrames[3] = tmp[1][0];
        walkRightAnimation = new Animation(0.1f, walkRightFrames);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public Bounds getWorldBounds() {
        worldBounds = super.getWorldBounds();
        worldBounds.x -= this.width;
        return worldBounds;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (flip) {
            batch.draw(currentFrame, position.x, position.y, -width, height);
        }
        else {
            batch.draw(currentFrame, position.x-this.width, position.y, width, height);
        }
    }

    public void update(float d) {
        currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            flip = true;
            velocity.x = - SPEED * d;
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            flip = false;
            velocity.x = SPEED * d;
        }
        else {
            velocity.x = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = SPEED * d;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = - SPEED * d;
        }
        else {
            velocity.y = 0;
        }

        if (velocity.x != 0 || velocity.y != 0) {
            stateTime += d;
        }

        position.add(velocity);
    }
}
