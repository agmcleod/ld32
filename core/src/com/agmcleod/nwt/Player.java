package com.agmcleod.nwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 15-04-17.
 */
public class Player extends GameEntity {
    Vector2 velocity;
    private final int SPEED = 350;
    private TextureRegion currentFrame;
    private boolean attacking;
    private Animation downAttack;
    private Animation rightAttack;
    private Animation upAttack;
    private Animation walkRightAnimation;
    private Animation walkUpAnimation;
    private Animation walkDownAnimation;
    private Animation currentAnimation;
    private float stateTime = 0f;
    private boolean flipX;
    private Direction direction;

    public Player() {
        super("player.png");
        velocity = new Vector2();
        position.set(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 50);
        width = 100;
        height = 100;
        bounds.set(20, 0, 50, 100);

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight() / 3);
        TextureRegion[] walkRightFrames = new TextureRegion[4];
        walkRightFrames[0] = tmp[0][0];
        walkRightFrames[1] = tmp[1][0];
        walkRightFrames[2] = tmp[2][0];
        walkRightFrames[3] = tmp[1][0];
        walkRightAnimation = new Animation(0.1f, walkRightFrames);

        TextureRegion[] walkDownFrames = new TextureRegion[4];
        walkDownFrames[0] = tmp[0][2];
        walkDownFrames[1] = tmp[1][2];
        walkDownFrames[2] = tmp[2][2];
        walkDownFrames[3] = tmp[1][2];
        walkDownAnimation = new Animation(0.1f, walkDownFrames);

        TextureRegion[] walkUpFrames = new TextureRegion[4];
        walkUpFrames[0] = tmp[0][1];
        walkUpFrames[1] = tmp[1][1];
        walkUpFrames[2] = tmp[2][1];
        walkUpFrames[3] = tmp[1][1];
        walkUpAnimation = new Animation(0.1f, walkUpFrames);

        TextureRegion[] attackUpFrames = new TextureRegion[2];
        attackUpFrames[0] = tmp[0][3];
        attackUpFrames[1] = tmp[0][1];
        upAttack = new Animation(0.1f, attackUpFrames);

        TextureRegion[] attackDownFrames = new TextureRegion[2];
        attackDownFrames[0] = tmp[1][3];
        attackDownFrames[1] = tmp[0][1];
        downAttack = new Animation(0.1f, attackDownFrames);

        TextureRegion[] attackRightFrames = new TextureRegion[2];
        attackRightFrames[0] = tmp[2][3];
        attackRightFrames[1] = tmp[0][0];
        rightAttack = new Animation(0.1f, attackRightFrames);

        currentAnimation = walkRightAnimation;
        attacking = true;
        direction = Direction.RIGHT;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public Bounds getWorldBounds() {
        worldBounds = super.getWorldBounds();
        worldBounds.x -= this.width;
        return worldBounds;
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

    public void update(float d) {
        currentFrame = currentAnimation.getKeyFrame(stateTime, !attacking);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            flipX = true;
            velocity.x = - SPEED * d;
            currentAnimation = walkRightAnimation;
            direction = Direction.LEFT;
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            flipX = false;
            velocity.x = SPEED * d;
            currentAnimation = walkRightAnimation;
            direction = Direction.RIGHT;
        }
        else {
            velocity.x = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = SPEED * d;
            flipX = false;
            currentAnimation = walkUpAnimation;
            direction = Direction.UP;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = - SPEED * d;
            flipX = false;
            currentAnimation = walkDownAnimation;
            direction = Direction.DOWN;
        }
        else {
            velocity.y = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            if (!attacking) {
                if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                    currentAnimation = rightAttack;
                }
                else if (direction == Direction.DOWN) {
                    currentAnimation = downAttack;
                }
                else if (direction == Direction.UP) {
                    currentAnimation = upAttack;
                }
                stateTime = 0;
                attacking = true;
            }
        }
        else {
            attacking = false;
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                currentAnimation = walkRightAnimation;
            }
            else if (direction == Direction.DOWN) {
                currentAnimation = walkDownAnimation;
            }
            else if (direction == Direction.UP) {
                currentAnimation = walkUpAnimation;
            }
        }

        if (velocity.x != 0 || velocity.y != 0 || attacking) {
            stateTime += d;
        }

        position.add(velocity);
    }
}
