package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by aaronmcleod on 15-04-17.
 */
public class GameScreen implements Screen {
    private float attackTimer;
    private Texture backgroundTexture;
    private SpriteBatch batch;
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
    private ShapeRenderer shapeRenderer;
    private float spawnTimeCounter;
    private Player player;
    private Stun stun;
    private boolean restartNextFrame;
    private Array<Bounds> worldBounds;
    private int round;

    public GameScreen(CoreGame cg) {
        this.cg = cg;
        fadeMode = CoreGame.FadeMode.FADE_IN;
    }

    public void collisions() {
        Iterator<Bounds> it = worldBounds.iterator();
        Bounds playerBounds = player.getWorldBounds();
        Bounds overlap = new Bounds();
        Iterator<Disc> itDisc = discs.iterator();
        while (itDisc.hasNext()) {
            handleIntersectPlayerBounds(itDisc.next().getWorldBounds(), playerBounds, overlap);
        }
        while (it.hasNext()) {
            handleIntersectPlayerBounds(it.next(), playerBounds, overlap);
        }
    }

    public void handleIntersectPlayerBounds(Bounds bounds, Bounds playerBounds, Bounds overlap) {
        if (bounds.overlaps(playerBounds)) {
            Vector2 vel = player.getVelocity();
            if (Intersector.intersectRectangles(bounds, playerBounds, overlap)) {
                if (playerBounds.leftOverlapsWith(bounds)) {
                    player.getPosition().x += overlap.width;
                } else if (playerBounds.rightOverlapsWith(bounds)) {
                    player.getPosition().x -= overlap.width;
                }

                if (playerBounds.bottomOverlapsWith(bounds)) {
                    player.getPosition().y += overlap.height;
                } else if (playerBounds.topOverlapsWith(bounds)) {
                    player.getPosition().y -= overlap.height;
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
        player.render(batch);
        Iterator<Disc> it = discs.iterator();
        while (it.hasNext()) {
            Disc disc = it.next();
            if (disc.isAlive()) {
                disc.render(batch);
            }
        }
        if (stun != null) {
            stun.render(batch);
        }
        uiFont.draw(batch, "Round: " + round + " of " + 3, 20, 670);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        it = discs.iterator();
        while (it.hasNext()) {
            it.next().renderHealth(shapeRenderer);
        }
        shapeRenderer.end();

        if (fade) {
            fadeTimer += dt;
            float percent;
            if (fadeMode == CoreGame.FadeMode.FADE_IN) {
                percent = 1f - fadeTimer / 0.5f;
            }
            else {
                percent = fadeTimer / 0.5f;
            }
            cg.drawBlackTransparentSquare(shapeRenderer, percent);
            if (percent >= 1f) {
                fade = false;
                if (fadeMode == CoreGame.FadeMode.FADE_OUT) {
                    if (gameWon) {
                        cg.gotoEndScreen();
                    }
                    else {
                        fadeMode = CoreGame.FadeMode.FADE_IN;
                        cg.setScreen(this);
                    }
                }
            }
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
        player = new Player();
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
        destroyCount = 0;
        discs = new Array<Disc>();
        discs.add(new Disc(font));
        fadeTimer = 0;
        fade = false;
        gameWon = false;
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
    }

    public void spawnBasedOnTimer() {
        if (discs.size > 0) {
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

    public void update(float dt) {
        spawnTimeCounter += dt;
        if (restartNextFrame) {
            fadeMode = CoreGame.FadeMode.FADE_OUT;
            fade = true;
        }

        if (!gameOver) {
            spawnBasedOnTimer();
            player.update(dt);
        }

        Iterator<Disc> it = discs.iterator();
        while (it.hasNext()) {
            Disc disc = it.next();
            if (disc.isAlive()) {
                disc.update(dt);
            }
            if (disc.triggerStun() && !gameOver) {
                gameOver = true;
                stun = new Stun(player.position.x - 100, player.position.y);
            }
        }

        if (stun != null) {
            // don't restart here, so last frame gets drawn. Restart at top of update
            restartNextFrame = stun.update(dt);
        }

        if (player.isAttacking() && attackTimer > 0.2) {
            attackTimer = 0f;
            Bounds playerBounds = player.getAttackBounds();
            it = discs.iterator();
            while (it.hasNext()) {
                Disc disc = it.next();
                Bounds discBounds = disc.getHitBox();
                if (discBounds.overlaps(playerBounds)) {
                    boolean discDead = false;
                    if (playerBounds.bottomOverlapsWith(discBounds) && player.getDirection() == Direction.DOWN) {
                        discDead = disc.takeDamage();
                    } else if (playerBounds.rightOverlapsWith(discBounds) && player.getDirection() == Direction.RIGHT) {
                        discDead = disc.takeDamage();
                    } else if (playerBounds.leftOverlapsWith(discBounds) && player.getDirection() == Direction.LEFT) {
                        discDead = disc.takeDamage();
                    } else if (playerBounds.topOverlapsWith(discBounds) && player.getDirection() == Direction.UP) {
                        discDead = disc.takeDamage();
                    }

                    if (discDead) {
                        spawnTimeCounter = 0f;
                        destroyCount++;
                        if (destroyCount >= 5) {
                            round++;
                            if (round == 2) {
                                discs.add(new Disc(font));
                            }
                            else if (round == 4) {
                                round = 3;
                                winCondition();
                            }
                            destroyCount = 0;
                        }
                    }
                }
            }
        }

        attackTimer += dt;
    }

    public void winCondition() {
        gameWon = true;
        discs.clear();
        fade = true;
        fadeMode = CoreGame.FadeMode.FADE_OUT;
    }
}
