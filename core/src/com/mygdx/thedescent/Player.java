package com.mygdx.thedescent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Aweso on 1/11/2018.
 */

class Player {
    Rectangle rect = new Rectangle();
    private float dy;
    boolean onPlat;
    Sound dropSound;

    Player() {
        this.rect.x = 1920 / 2 - 128 / 2;
        this.rect.y = 1920;
        this.rect.width = 128;
        this.rect.height = 128;
        this.dy = 0;

        this.dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
    }

    void update(Camera camera, Array<Platform> platforms, float delta) {
        // adjust y velocity and position
        this.dy += -30;
        this.rect.y += this.dy * delta;

        Vector3 rectScreenPos = new Vector3();
        rectScreenPos.set(this.rect.x, this.rect.y, 0); // player rect coords relative to screen
        camera.project(rectScreenPos);

        if (rectScreenPos.y > 2000 || rectScreenPos.y < -200) {
            // "death"
        }

        boolean lastOnPlat = this.onPlat; // was player on the platform last frame
        this.onPlat = false; // assume this frame false for now

        for (Platform platform : platforms) {


            if (platform.rect.overlaps(this.rect)) {
                if (this.dy > 0) {
                    this.rect.y = platform.rect.y - 1; // assume hits bottom if moving up
                } else if (this.dy < 0) {
                    this.rect.y = platform.rect.y + 32; // assume hits top if moving down
                    onPlat = true; // change to true if on platform
                    if (!lastOnPlat) { // to prevent ear pain
                        dropSound.play();
                    }
                }
                this.dy = 0; // reset velocity
            }
        }
    }
}
