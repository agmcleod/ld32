package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-19.
 */
public class GirlBoss extends GameEntity {
    private final int SPEED = 270;
    private BossRound bossRound;
    private Texture girlTexture;
    private Vector2 velocity;
    private Animation currentAnimation;
    private TextureRegion currentFrame;
    private boolean flipX;
    private boolean hasBear;
    private float stateTime;
    private Animation walkDownAnimation;
    private Animation walkLeftAnimation;
    private Animation walkUpAnimation;

    public GirlBoss(BossRound br) {
        super("girlboss.png");
        this.bossRound = br;
        velocity = new Vector2();
        width = 80;
        height = 80;
        bounds.set(20, 0, 40, 80);
        position.set(Gdx.graphics.getWidth() - 180, MathUtils.random(0, Gdx.graphics.getHeight() - bounds.getHeight() - 120));
        stateTime = 0f;
        hasBear = false;

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 3, texture.getHeight() / 2);

        TextureRegion[] walkLeftFrames = new TextureRegion[2];
        walkLeftFrames[0] = tmp[0][0];
        walkLeftFrames[1] = tmp[1][0];
        walkLeftAnimation = new Animation(0.1f, walkLeftFrames);

        TextureRegion[] walkUpFrames = new TextureRegion[2];
        walkUpFrames[0] = tmp[0][1];
        walkUpFrames[1] = tmp[1][1];
        walkUpAnimation = new Animation(0.1f, walkUpFrames);

        TextureRegion[] walkDownFrames = new TextureRegion[2];
        walkDownFrames[0] = tmp[0][2];
        walkDownFrames[1] = tmp[1][2];
        walkDownAnimation = new Animation(0.1f, walkDownFrames);

        currentAnimation = walkLeftAnimation;
        currentFrame = currentAnimation.getKeyFrame(0, true);
    }

    @Override
    public void collisionCallback() {
        if (!hasBear) {
            bossRound.stunPlayer();
        }
    }

    public void dispose() {
        girlTexture.dispose();
    }

    @Override
    public Bounds getWorldBounds() {
        worldBounds = super.getWorldBounds();
        worldBounds.x -= this.width;
        return worldBounds;
    }

    public boolean hasBear() {
        return hasBear;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (flipX) {
            batch.draw(currentFrame, position.x, position.y, -width, height);
        }
        else {
            batch.draw(currentFrame, position.x-this.width, position.y, width, height);
        }
    }

    public void setHasBear(boolean hasBear) {
        this.hasBear = hasBear;
    }

    public void update(float dt, Bounds target) {
        if (hasBear) {
            return;
        }
        currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        Bounds b = getWorldBounds();

        if (b.x != target.x || b.y != target.y) {
            float diffX = b.x - target.x;
            float diffY = b.y - target.y;
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0) {
                    currentAnimation = walkLeftAnimation;
                    flipX = false;
                }
                else {
                    flipX = true;
                    currentAnimation = walkLeftAnimation;
                }
            }
            else {
                if (diffY > 0) {
                    flipX = false;
                    currentAnimation = walkDownAnimation;
                }
                else {
                    flipX = false;
                    currentAnimation = walkUpAnimation;
                }
            }

            if (diffX > 0) {
                velocity.x = -SPEED * dt;
            }
            else {
                velocity.x = SPEED * dt;
            }

            if (diffY > 0) {
                velocity.y = -SPEED * dt;
            }
            else {
                velocity.y = SPEED * dt;
            }
        }
        position.add(velocity);
        if (velocity.x != 0 || velocity.y != 0) {
            stateTime += dt;
        }
    }
}
