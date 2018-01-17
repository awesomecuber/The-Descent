package com.mygdx.thedescent;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Aweso on 1/12/2018.
 */

public class Platform {
    Rectangle rect = new Rectangle();
    boolean willDel = false;
    private Camera camera;

    public Platform(Camera _camera) {
        this.rect.x = MathUtils.random(0, 1080 - 384);
        this.rect.y = _camera.position.y - 1920 / 2;
        this.rect.width = 384;
        this.rect.height = 64;
        this.camera = _camera;
    }

    void update() {
        Vector3 platScreenPos = new Vector3();
        platScreenPos.set(this.rect.x, this.rect.y, 0); // platform rect coords relative to screen
        camera.project(platScreenPos);

        if (platScreenPos.y + 64 > 1920) {
            willDel = true;
        }
    }
}
