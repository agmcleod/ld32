package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-19.
 */
public class BossRound {
    private Bear bear;
    private GirlBoss boss;
    private boolean bossDefeated;
    private Texture dialogue1;
    private Texture dialogue2;
    private GameScreen gs;
    private float speechTimeout;
    private float winTimeout = 0;
    private boolean started;

    public BossRound(GameScreen gs) {
        this.gs = gs;
        started = false;
        boss = new GirlBoss(this);
        speechTimeout = 0f;
        bear = null;
        bossDefeated = false;
        dialogue1 = new Texture("bossleveld1.png");
        dialogue2 = new Texture("bossleveld2.png");
    }

    public void dispose() {
        boss.dispose();
        dialogue1.dispose();
        dialogue2.dispose();
    }

    public boolean engaged() {
        return speechTimeout >= 2f;
    }

    public Bear getBear() {
        return bear;
    }

    public GirlBoss getBoss() {
        return boss;
    }

    public void hideBear() {
        bear.setAlive(false);
    }

    public boolean isStarted() {
        return started;
    }

    public void killBoss() {
        bossDefeated = true;
    }

    public void render(SpriteBatch batch) {
        boss.render(batch);
        if (!engaged()) {
            batch.draw(dialogue1, Gdx.graphics.getWidth() / 2 - dialogue2.getWidth() / 2, 500);
        }
        if (bossDefeated) {
            batch.draw(dialogue2, Gdx.graphics.getWidth() / 2 - dialogue2.getWidth() / 2, 500);
        }

        if (bear != null && bear.isAlive()) {
            bear.render(batch);
        }
    }

    public void startBossRound() {
        started = true;
    }

    public void stunPlayer() {
        gs.stunPlayer();
    }

    public void update(float dt) {
        if (!engaged()) {
            speechTimeout += dt;
        }
        else {
            if (bear == null) {
                Player player = gs.getPlayer();
                boss.update(dt, player.getWorldBounds());
            }
            else {
                boss.update(dt, bear.getWorldBounds());
            }
            if (bear != null && bear.isAlive()) {
                bear.update(dt);
            }
        }

        if (bossDefeated) {
            winTimeout += dt;
            if (winTimeout > 3f) {
                gs.winCondition();
            }
        }
    }

    public void throwBear(Direction direction) {
        Vector2 pos = gs.getPlayer().getPosition();
        bear = new Bear(pos.x, pos.y, direction);
    }
}
