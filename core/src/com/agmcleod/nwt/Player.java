package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-17.
 */
public class Player {
    private Texture texture;
    private Bounds bounds;

    Vector2 velocity;
    private final int SPEED = 250;

    public Player() {
        this.texture = new Texture("player.png");
        velocity = new Vector2();
        bounds = new Bounds(Gdx.graphics.getWidth() / 2 - texture.getWidth() / 2, Gdx.graphics.getHeight() / 2 - texture.getHeight() / 2, texture.getWidth(), texture.getHeight());
    }

    public void dispose() {
        this.texture.dispose();
    }

    public Bounds getBounds() {
        return bounds;
    }

    public final Vector2 getVelocity() {
        return velocity;
    }

    public void render(SpriteBatch batch) {
        batch.draw(this.texture, bounds.getX(), bounds.getY());
    }

    public void update() {
        float d = Gdx.graphics.getDeltaTime();
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

        bounds.setPosition(bounds.getX() + velocity.x, bounds.getY() + velocity.y);
    }
}
