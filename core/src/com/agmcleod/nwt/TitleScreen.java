package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class TitleScreen implements InputProcessor, Screen {
    private CoreGame cg;
    private TransitionCallback callback;
    private SpriteBatch batch;
    private Texture background;
    private boolean fade;
    private ShapeRenderer shapeRenderer;
    private float fadeTimer = 0;

    public TitleScreen(CoreGame cg) {
        this.cg = cg;
    }

    @Override
    public void show() {
        background = new Texture("intro.png");
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        fade = false;
        callback = new TransitionCallback() {
            @Override
            public void callback() {
                cg.startGame();
            }
        };
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();

        if (fade) {
            fadeTimer += delta;
            cg.drawBlackTransparentSquare(shapeRenderer, fadeTimer / 0.5f, callback);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        shapeRenderer.dispose();
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
        fade = true;
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
