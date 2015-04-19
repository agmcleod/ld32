package com.agmcleod.nwt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CoreGame extends Game {
    SpriteBatch batch;
    Texture img;

    enum FadeMode {
        FADE_IN,FADE_OUT
    }

    @Override
    public void create () {
        setScreen(new TitleScreen(this));
    }

    public void gotoEndScreen() {
        setScreen(new EndScreen(this));
    }

    public void startGame() {
        setScreen(new GameScreen(this));
    }

    public void drawBlackTransparentSquare(ShapeRenderer shapeRenderer, float percent, TransitionCallback callback) {
        Gdx.gl.glEnable(GL20.GL_BLEND);

        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, percent));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (percent > 1.0f || percent < 0f) {
            callback.callback();
        }
    }
}
