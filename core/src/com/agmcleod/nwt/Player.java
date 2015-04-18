package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
    private TextureRegion firstFrame;

    public Player() {
        super("player.png");
        velocity = new Vector2();
        firstFrame = new TextureRegion(this.texture, 100, 100);
        position.set(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 50);
        width = 100;
        height = 100;
        bounds.set(20, 0, 50, 100);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(firstFrame, position.x, position.y, width, height);
    }

    public void update(float d) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = - SPEED * d;
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
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

        position.add(velocity);
    }
}
