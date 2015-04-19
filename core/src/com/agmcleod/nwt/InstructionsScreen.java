package com.agmcleod.nwt;

/**
 * Created by aaronmcleod on 15-04-19.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/**
 * Created by aaronmcleod on 15-04-19.
 */
public class InstructionsScreen implements InputProcessor, Screen {
    private TransitionCallback fadeInCallback;
    private TransitionCallback fadeOutCallback;
    private CoreGame cg;
    private SpriteBatch batch;
    private Texture background;
    private boolean fade;
    private CoreGame.FadeMode fadeMode;
    private ShapeRenderer shapeRenderer;
    private Texture texture;
    private float fadeTimer = 0;

    public InstructionsScreen(CoreGame cg) {
        this.cg = cg;
        fadeMode = CoreGame.FadeMode.FADE_IN;
    }
    @Override
    public void show() {
        fade = true;
        fadeTimer = 0f;
        fadeInCallback = new TransitionCallback() {
            @Override
            public void callback() {
                fade = false;
                fadeTimer = 0f;
            }
        };

        fadeOutCallback = new TransitionCallback() {
            @Override
            public void callback() {
                fade = false;
                cg.startGame();
            }
        };
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        texture = new Texture("controls.png");
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();

        if (fade) {
            fadeTimer += delta;
            if (fadeMode == CoreGame.FadeMode.FADE_IN) {
                cg.drawBlackTransparentSquare(shapeRenderer, 1f - (fadeTimer / 0.5f), fadeInCallback);
            }
            else {
                cg.drawBlackTransparentSquare(shapeRenderer, fadeTimer / 0.5f, fadeOutCallback);
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
        texture.dispose();
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
        fadeMode = CoreGame.FadeMode.FADE_OUT;
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

