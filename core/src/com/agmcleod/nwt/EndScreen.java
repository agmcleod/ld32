package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by aaronmcleod on 15-04-19.
 */
public class EndScreen implements Screen {
    private Texture background;
    private SpriteBatch batch;
    private TransitionCallback callback;
    private CoreGame cg;
    private boolean fade;
    private float fadeTimer;
    private float musicTimer;
    private Music music;
    private ShapeRenderer shapeRenderer;

    public EndScreen(CoreGame cg) {
        this.cg = cg;
    }

    @Override
    public void show() {
        background = new Texture("end.png");
        batch = new SpriteBatch();
        fade = true;
        fadeTimer = 0.5f;
        shapeRenderer = new ShapeRenderer();
        musicTimer = 0f;
        callback = new TransitionCallback() {
            @Override
            public void callback() {
                fade = false;
            }
        };
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();

        if (fade) {
            fadeTimer += delta;
            float percent = 1f - (fadeTimer / 0.5f);
            cg.drawBlackTransparentSquare(shapeRenderer, percent, callback);
        }
        else {
            musicTimer += delta;

            Music music = cg.getMusic();
            float vol = 1f - (musicTimer / 5f);
            music.setVolume(vol);
            if (vol <= 0f) {
                music.stop();
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
