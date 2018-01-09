package com.mygdx.thedescent;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    private Texture platformImage;
    private Texture playerImage;
    private Sound dropSound;
    private Music rainMusic;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Rectangle player;
    private float playerdy;
    private Array<Rectangle> platforms;
    private long lastPlatformTime;
    private boolean onPlat;



    private void spawnPlatform() {
        Rectangle platform = new Rectangle();
        platform.x = MathUtils.random(0, 1080 - 384);
        platform.y = camera.position.y - 1920 / 2;
        platform.width = 384;
        platform.height = 64;
        platforms.add(platform);
        lastPlatformTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        // load sprites
        platformImage = new Texture(Gdx.files.internal("redplatform.png"));
        playerImage = new Texture(Gdx.files.internal("player.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        batch = new SpriteBatch();

        player = new Rectangle();
        player.x = 1920 / 2 - 128 / 2;
        player.y = 1920;
        player.width = 128;
        player.height = 128;
        playerdy = 0;

        platforms = new Array<Rectangle>();
        spawnPlatform();
    }

    @Override
    public void render(float delta) {
        // background color
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // move x based on touch
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            player.x = touchPos.x - 128 / 2;
        }

        // spawn platforms
        if(TimeUtils.nanoTime() - lastPlatformTime > 1000000000) spawnPlatform();

        // adjust y velocity and position
        playerdy += -30;
        player.y += playerdy * Gdx.graphics.getDeltaTime();

        boolean lastOnPlat = onPlat; // was player on the platform last frame
        onPlat = false; // assume this frame false for now

        Iterator<Rectangle> iter = platforms.iterator(); // this instead of for so we can delete rectangles (i think)
        while(iter.hasNext()) {
            Rectangle platform = iter.next();
            if (platform.y + 64 > 1920) iter.remove(); // remove if off screen
            if (platform.overlaps(player)) {
                if (playerdy > 0) {
                    player.y = platform.y - 1; // assume hits bottom if moving up
                } else if (playerdy < 0) {
                    player.y = platform.y + 32; // assume hits top if moving down
                    onPlat = true; // change to true if on platform
                    if (!lastOnPlat) { // to prevent ear pain
                        dropSound.play();
                    }
                }
                playerdy = 0; // reset velocity
            }
        }

        // update camera
        camera.translate(0, -300 * Gdx.graphics.getDeltaTime());
        camera.update();

        // draw
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(playerImage, player.x, player.y);
        for(Rectangle platform: platforms) {
            batch.draw(platformImage, platform.x, platform.y);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        platformImage.dispose();
        playerImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
}