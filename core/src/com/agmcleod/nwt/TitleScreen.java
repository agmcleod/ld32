package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class TitleScreen implements Screen {
    private CoreGame cg;
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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            fade = true;
        }

        if (fade) {
            fadeTimer += delta;
            cg.drawBlackTransparentSquare(shapeRenderer, fadeTimer / 0.5f);
            if (fadeTimer >= 0.5f) {
                this.cg.startGame();
            }
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
}
