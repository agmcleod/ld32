package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by aaronmcleod on 15-04-17.
 */
public class GameScreen implements InputProcessor, Screen {
    private Sound attackSound;
    private float attackTimer;
    private Texture backgroundTexture;
    private BossRound bossRound;
    private SpriteBatch batch;
    TransitionCallback callback;
    private CoreGame cg;
    private int destroyCount;
    private Array<Disc> discs;
    private boolean fade;
    private CoreGame.FadeMode fadeMode;
    private float fadeTimer;
    private BitmapFont font;
    private BitmapFont uiFont;
    private boolean gameOver;
    private boolean gameWon;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private Sound shockSound;
    private float spawnTimeCounter;
    private Stun stun;
    private boolean restartNextFrame;
    private int round;
    private int roundSpawnCount;
    private Array<Bounds> worldBounds;


    public GameScreen(CoreGame cg) {
        this.cg = cg;
    }

    public void attemptAttack() {
        attackTimer = 0f;
        Bounds playerBounds = player.getAttackBounds();
        Iterator<Disc> it = discs.iterator();
        if (bossRound.engaged()) {
            Bounds bossBounds = bossRound.getBoss().getWorldBounds();
            if (playerBounds.bottomOverlapsWith(bossBounds) && player.getDirection() == Direction.DOWN) {
                bossRound.killBoss();
            } else if (playerBounds.rightOverlapsWith(bossBounds) && player.getDirection() == Direction.RIGHT) {
                bossRound.killBoss();
            } else if (playerBounds.leftOverlapsWith(bossBounds) && player.getDirection() == Direction.LEFT) {
                bossRound.killBoss();
            } else if (playerBounds.topOverlapsWith(bossBounds) && player.getDirection() == Direction.UP) {
                bossRound.killBoss();
            }
        }
        else {
            while (it.hasNext()) {
                Disc disc = it.next();
                Bounds discBounds = disc.getHitBox();
                if (disc.isAlive() && discBounds.overlaps(playerBounds)) {
                    boolean discDead = false;
                    boolean dealtDamage = false;
                    if (playerBounds.bottomOverlapsWith(discBounds) && player.getDirection() == Direction.DOWN) {
                        discDead = disc.takeDamage();
                        dealtDamage = true;
                    } else if (playerBounds.rightOverlapsWith(discBounds) && player.getDirection() == Direction.RIGHT) {
                        discDead = disc.takeDamage();
                        dealtDamage = true;
                    } else if (playerBounds.leftOverlapsWith(discBounds) && player.getDirection() == Direction.LEFT) {
                        discDead = disc.takeDamage();
                        dealtDamage = true;
                    } else if (playerBounds.topOverlapsWith(discBounds) && player.getDirection() == Direction.UP) {
                        discDead = disc.takeDamage();
                        dealtDamage = true;
                    }

                    if (dealtDamage) {
                        attackSound.play();
                    }

                    if (discDead) {
                        spawnTimeCounter = 0f;
                        destroyCount++;
                        if (destroyCount >= 5) {
                            roundSpawnCount = 0;
                            round++;
                            if (round == 2) {
                                discs.add(new Disc(font));
                            }
                            else if (round == 4) {
                                round = 3;
                                cg.bossRoundStarted();
                                bossRound.startBossRound();
                            }
                            destroyCount = 0;
                        }
                    }
                }
            }
        }
    }

    public void collisions() {
        Iterator<Bounds> it = worldBounds.iterator();
        ;Bounds overlap = new Bounds();
        Iterator<Disc> itDisc = discs.iterator();
        while (itDisc.hasNext()) {
            Disc disc = itDisc.next();
            if (disc.isAlive()) {
                handleBoundsInteraction(disc.getWorldBounds(), player, overlap);
            }
        }
        while (it.hasNext()) {
            handleBoundsInteraction(it.next(), player, overlap);
        }

        if (bossRound.engaged()) {
            handleBoundsInteraction(player.getWorldBounds(), bossRound.getBoss(), overlap);
            it = worldBounds.iterator();
            Bear bear = bossRound.getBear();
            if (bear != null) {
                if (!bear.isStopped()) {
                    while (it.hasNext()) {
                        handleBoundsInteraction(it.next(), bossRound.getBear(), overlap);
                    }
                }
                GirlBoss boss = bossRound.getBoss();
                if (!boss.hasBear()) {
                    if (bossRound.getBear().getWorldBounds().overlaps(boss.getWorldBounds())) {
                        boss.setHasBear(true);
                        bossRound.hideBear();
                    }
                }
            }
        }
    }

    public void handleBoundsInteraction(Bounds bounds, GameEntity toCorrect, Bounds overlap) {
        Bounds toCorrectBounds = toCorrect.getWorldBounds();
        if (bounds.overlaps(toCorrectBounds)) {
            Vector2 vel = player.getVelocity();
            if (Intersector.intersectRectangles(bounds, toCorrectBounds, overlap)) {
                boolean collided = false;
                if (overlap.width < overlap.height) {
                    if (toCorrectBounds.leftOverlapsWith(bounds)) {
                        toCorrect.getPosition().x += overlap.width;
                        collided = true;
                    } else if (toCorrectBounds.rightOverlapsWith(bounds)) {
                        toCorrect.getPosition().x -= overlap.width;
                        collided = true;
                    }
                }
                else {
                    if (toCorrectBounds.bottomOverlapsWith(bounds)) {
                        toCorrect.getPosition().y += overlap.height;
                        collided = true;
                    } else if (toCorrectBounds.topOverlapsWith(bounds)) {
                        toCorrect.getPosition().y -= overlap.height;
                        collided = true;
                    }
                }

                if (collided) {
                    toCorrect.collisionCallback();
                }
            }
        }
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        player.dispose();
        font.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        uiFont.dispose();
        stun.dispose();
        attackSound.dispose();
        shockSound.dispose();
    }

    public BossRound getBossRound() {
        return bossRound;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void render(float dt) {
        update(dt);
        collisions();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        Iterator<Disc> it = discs.iterator();
        while (it.hasNext()) {
            Disc disc = it.next();
            if (disc.isAlive()) {
                disc.render(batch);
            }
        }

        if (bossRound.isStarted()) {
            bossRound.render(batch);
        }

        player.render(batch);
        if (stun != null) {
            stun.render(batch);
        }
        if (!bossRound.isStarted()) {
            uiFont.draw(batch, "Round: " + round + " of " + 3, 20, 670);
        }
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        it = discs.iterator();
        while (it.hasNext()) {
            it.next().renderHealth(shapeRenderer);
        }
        shapeRenderer.end();

        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Bounds pb = player.getWorldBounds();
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(pb.x, pb.y, pb.width, pb.height);
        if (bossRound.isStarted()) {
            Bounds bb = bossRound.getBoss().getWorldBounds();
            shapeRenderer.rect(bb.x, bb.y, bb.width, bb.height);
        }
        shapeRenderer.end(); */

        if (fade) {
            fadeTimer += dt;
            float percent;
            if (fadeMode == CoreGame.FadeMode.FADE_IN) {
                percent = 1f - fadeTimer / 0.5f;
            }
            else {
                percent = fadeTimer / 0.5f;
            }
            cg.drawBlackTransparentSquare(shapeRenderer, percent, callback);
        }
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
        player = new Player(this);
        worldBounds = new Array<Bounds>() {{
            add(new Bounds(0, 0, 64, Gdx.graphics.getHeight()));
            add(new Bounds(0, Gdx.graphics.getHeight() - 64, Gdx.graphics.getWidth(), 64));
            add(new Bounds(Gdx.graphics.getWidth(), 0, 64, Gdx.graphics.getHeight()));
            add(new Bounds(0, -64, Gdx.graphics.getWidth(), 64));
        }};
        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        uiFont = new BitmapFont(Gdx.files.internal("white-ui.fnt"), Gdx.files.internal("white-ui.png"), false);
        spawnTimeCounter = 0;
        attackTimer = 0;
        shapeRenderer = new ShapeRenderer();
        stun = null;
        gameOver = false;
        restartNextFrame = false;
        round = 1;
        roundSpawnCount = 0;
        destroyCount = 0;
        discs = new Array<Disc>();
        discs.add(new Disc(font));
        fadeTimer = 0;
        fade = true;
        fadeMode = CoreGame.FadeMode.FADE_IN;
        gameWon = false;
        attackSound = Gdx.audio.newSound(Gdx.files.internal("hurt.mp3"));
        shockSound = Gdx.audio.newSound(Gdx.files.internal("shock.mp3"));
        bossRound = new BossRound(this);
        if (cg.bossRoundStarted) {
            bossRound.startBossRound();
        }
        Gdx.input.setInputProcessor(this);

        final GameScreen gameScreen = this;
        callback = new TransitionCallback() {
            @Override
            public void callback() {
                fade = false;
                if (fadeMode == CoreGame.FadeMode.FADE_OUT) {
                    if (gameWon) {
                        cg.gotoEndScreen();
                    }
                    else {
                        fadeMode = CoreGame.FadeMode.FADE_IN;
                        cg.setScreen(gameScreen);
                    }
                }
            }
        };
    }

    public void spawnDisc() {
        spawnDisc(0);
    }

    public void spawnDisc(int i) {
        int x, y;
        boolean spotAvailable = false;
        do {
            x = MathUtils.random(0, 4) * 128 + ((128 - 80) / 2) + 64;
            y = MathUtils.random(0, 4) * 128 + ((128 - 80) / 2);

            Iterator<Disc> it = discs.iterator();
            while (it.hasNext()) {
                Disc d = it.next();
                if (d.position.x != x || d.position.y != y) {
                    spotAvailable = true;
                }
            }
        } while (!spotAvailable);

        Disc disc = discs.get(i);
        disc.getPosition().set(x, y);
        disc.setAlive(true);
        roundSpawnCount++;
    }

    public void spawnBasedOnTimer() {
        if (discs.size > 0 && roundSpawnCount < 5) {
            Disc disc = discs.get(0);
            if (spawnTimeCounter > 1.0f && !disc.isAlive()) {
                spawnDisc();
            }
            if (discs.size > 1) {
                disc = discs.get(1);
                if (spawnTimeCounter > 1.2f && !disc.isAlive()) {
                    spawnDisc(1);
                }
            }
        }
    }

    public void stunPlayer() {
        if (!gameOver) {
            gameOver = true;
            stun = new Stun(player.position.x - 100, player.position.y);
            shockSound.play();
        }
    }

    public void update(float dt) {
        spawnTimeCounter += dt;
        if (restartNextFrame) {
            fadeMode = CoreGame.FadeMode.FADE_OUT;
            fade = true;
        }

        if (!gameOver && !bossRound.isStarted()) {
            spawnBasedOnTimer();
        }

        if (!gameOver) {
            player.update(dt);
        }

        Iterator<Disc> it = discs.iterator();
        while (it.hasNext()) {
            Disc disc = it.next();
            if (disc.isAlive()) {
                disc.update(dt);
                if (disc.triggerStun() && !gameOver) {
                    stunPlayer();
                }
            }
        }

        if (bossRound.isStarted()) {
            bossRound.update(dt);
        }

        if (stun != null) {
            // don't restart here, so last frame gets drawn. Restart at top of update
            restartNextFrame = stun.update(dt);
        }

        if (player.isAttacking() && attackTimer > 0.2) {
            attemptAttack();
        }

        attackTimer += dt;
    }

    public void winCondition() {
        gameWon = true;
        discs.clear();
        fade = true;
        fadeMode = CoreGame.FadeMode.FADE_OUT;
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
