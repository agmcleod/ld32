package com.agmcleod.nwt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by aaronmcleod on 15-04-18.
 */
public class Disc extends GameEntity {
    private boolean alive;
    private Animation appearAnimation;
    private Bounds hitBox;
    private TextureRegion currentFrame;
    private float animationState;
    private int health;
    public Disc() {
        super("disc.png");
        alive = false;
        bounds.set(0, 0, 80, 80);
        width = 80;
        height = 80;

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight() / 2);
        TextureRegion[] appearFrames = new TextureRegion[4];

        appearFrames[0] = tmp[0][1];
        appearFrames[1] = tmp[1][0];
        appearFrames[2] = tmp[1][1];
        appearFrames[3] = tmp[0][0];

        appearAnimation = new Animation(0.15f, appearFrames);
        health = 5;
        hitBox = new Bounds();
    }

    public Bounds getHitBox() {
        hitBox.set(worldBounds.x - 10, worldBounds.y - 10, worldBounds.width + 10, worldBounds.height + 10);
        return hitBox;
    }

    public boolean isAlive() {
        return alive;
    }

    public void renderHealth(ShapeRenderer renderer) {
        if (isAlive() && appearAnimation.isAnimationFinished(animationState)) {
            Color c;
            if (health == 5) {
                c = Color.GREEN;
            }
            else if (health == 4 || health == 3) {
                c = Color.YELLOW;
            }
            else if (health == 2) {
                c = Color.ORANGE;
            }
            else {
                c = Color.RED;
            }
            renderer.setColor(c);
            renderer.rect(position.x, position.y + height, width * ((float) health / 5f), 5);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, position.x, position.y, width, height);
    }

    public void setAlive(boolean value) {
        if (!alive && value) {
            health = 5;
        }
        alive = value;
        animationState = 0f;
    }

    public boolean takeDamage() {
        health--;
        if (health <= 0) {
            alive = false;
            return true;
        }
        else {
            return false;
        }
    }

    public void update(float dt) {
        animationState += dt;
        currentFrame = appearAnimation.getKeyFrame(animationState, false);
    }
}
