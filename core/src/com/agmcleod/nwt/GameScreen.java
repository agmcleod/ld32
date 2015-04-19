package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private CoreGame cg;
    private Disc disc;
    private BitmapFont font;
    private boolean gameOver;
    private ShapeRenderer shapeRenderer;
    private float spawnTimeCounter;
    private Player player;
    private Stun stun;
    private boolean restartNextFrame;
    private Array<Bounds> worldBounds;
    private float attackTimer;

    public GameScreen(CoreGame cg) {
        this.cg = cg;
    }

    public void collisions() {
        Iterator<Bounds> it = worldBounds.iterator();
        Bounds playerBounds = player.getWorldBounds();
        Bounds overlap = new Bounds();
        handleIntersectPlayerBounds(disc.getWorldBounds(), playerBounds, overlap);
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
        if (disc.isAlive()) {
            disc.render(batch);
        }
        if (stun != null) {
            stun.render(batch);
        }
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        disc.renderHealth(shapeRenderer);
        shapeRenderer.end();
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
        disc = new Disc(font);
        spawnTimeCounter = 0;
        attackTimer = 0;
        shapeRenderer = new ShapeRenderer();
        stun = null;
        gameOver = false;
        restartNextFrame = false;
    }

    public void spawnDisc() {
        int x = MathUtils.random(0, 4) * 128 + ((128 - 80) / 2) + 64;
        int y = MathUtils.random(0, 4) * 128 + ((128 - 80) / 2);
        disc.getPosition().set(x, y);
        disc.setAlive(true);
    }

    public void update(float dt) {
        spawnTimeCounter += dt;
        if (restartNextFrame) {
            cg.setScreen(this);
        }
        if (spawnTimeCounter > 1.0f && !disc.isAlive()) {
            spawnDisc();
        }
        if (disc.isAlive()) {
            disc.update(dt);
        }
        if (!gameOver) {
            player.update(dt);
        }

        if (stun != null) {
            restartNextFrame = stun.update(dt);
        }

        if (player.isAttacking() && attackTimer > 0.2) {
            attackTimer = 0f;
            Bounds playerBounds = player.getAttackBounds();
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
                }
            }
        }

        if (disc.triggerStun() && !gameOver) {
            gameOver = true;
            stun = new Stun(player.position.x - 100, player.position.y);
            stun.update(dt);
        }

        attackTimer += dt;
    }
}
