package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
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
    private GameScreen gs;
    private BitmapFont speechFont;
    private float speechTimeout;
    private float winTimeout = 0;
    private boolean started;

    public BossRound(GameScreen gs) {
        this.gs = gs;
        started = false;
        boss = new GirlBoss(this);
        speechFont = new BitmapFont(Gdx.files.internal("blackspeech.fnt"), Gdx.files.internal("blackspeech.png"), false);
        speechTimeout = 0f;
        bear = null;
        bossDefeated = false;
    }

    public void dispose() {
        boss.dispose();
        speechFont.dispose();
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
            speechFont.draw(batch, "Give me that bear!", boss.position.x - 100, boss.position.y + boss.height - 20);
        }
        if (bossDefeated) {
            speechFont.draw(batch, "No! You have", boss.position.x - 100, boss.position.y + boss.height - 20);
            speechFont.draw(batch, "beat me!", boss.position.x - 100, boss.position.y + boss.height - 40);
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
