package com.mygdx.thedescent;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Aweso on 1/12/2018.
 */

public class Enemy {
    Rectangle rect = new Rectangle();
    int health;

    public Enemy(int x, int y, int _health) {
        this.rect.x = x;
        this.rect.y = y;
        this.health = _health;
    }
}
