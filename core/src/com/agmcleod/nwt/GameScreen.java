package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.w3c.dom.css.Rect;

import java.util.Iterator;

/**
 * Created by aaronmcleod on 15-04-17.
 */
public class GameScreen implements InputProcessor, Screen {
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private Player player;
    private Array<Bounds> worldBounds;

    public void collisions() {
        Iterator<Bounds> it = worldBounds.iterator();
        Bounds playerBounds = player.getBounds();
        Bounds overlap = new Bounds();
        while (it.hasNext()) {
            Bounds bounds = it.next();

            if (bounds.overlaps(playerBounds)) {
                Vector2 vel = player.getVelocity();
                if (Intersector.intersectRectangles(bounds, playerBounds, overlap)) {
                    player.getBounds().sub(overlap);
                }
            }
        }
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        batch.dispose();
        player.dispose();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void render(float dt) {
        update();
        collisions();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        player.render(batch);
        batch.end();
    }

    @Override
    public void resize(int w, int h) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        backgroundTexture = new Texture("backdrop.png");
        batch = new SpriteBatch();
        player = new Player();
        worldBounds = new Array<Bounds>() {{
            add(new Bounds(0, 0, 64, Gdx.graphics.getHeight()));
            add(new Bounds(0, Gdx.graphics.getHeight() - 64, Gdx.graphics.getWidth(), 64));
            add(new Bounds(Gdx.graphics.getWidth(), 0, 64, Gdx.graphics.getHeight()));
            add(new Bounds(0, -64, Gdx.graphics.getWidth(), 64));
        }};
    }

    public void update() {
        player.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
