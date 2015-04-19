package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class GameEntity {
    protected Texture texture;
    protected Bounds bounds;
    protected Bounds worldBounds;
    protected Vector2 position;
    protected float width;
    protected float height;

    public GameEntity(String textureName) {
        this.texture = new Texture(textureName);
        position = new Vector2(0, 0);
        bounds = new Bounds(0, 0, texture.getWidth(), texture.getHeight());
        worldBounds = new Bounds(bounds);
        width = bounds.width;
        height = bounds.height;
    }

    public void collisionCallback() {
    }

    public void dispose() {
        this.texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Bounds getWorldBounds() {
        worldBounds.set(bounds);
        worldBounds.x += position.x;
        worldBounds.y += position.y;
        return worldBounds;
    }

    public void render(SpriteBatch batch) {
        batch.draw(this.texture, position.x, position.y, width, height);
    }
}
