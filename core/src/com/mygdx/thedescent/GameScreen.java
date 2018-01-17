package com.mygdx.thedescent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    private Texture platformImage;
    private Texture playerImage;

    private Music rainMusic;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player player;
    private Array<Platform> platforms;
    private long lastPlatformTime;

    private int score;
    private BitmapFont scoreBitmap;

    private void spawnPlatform() {
        platforms.add(new Platform(camera));
        lastPlatformTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        // load sprites
        platformImage = new Texture(Gdx.files.internal("redplatform.png"));
        playerImage = new Texture(Gdx.files.internal("player.png"));

        // load the drop sound effect and the rain background "music"
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        batch = new SpriteBatch();

        player = new Player();

        platforms = new Array<Platform>();
        spawnPlatform();

        score = 0;
        scoreBitmap = new BitmapFont();
        scoreBitmap.getData().setScale(2);
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
            player.rect.x = touchPos.x - 128 / 2;
        }

        if (player.onPlat) {
            score++;
        }

        // spawn platforms
        if(TimeUtils.nanoTime() - lastPlatformTime > 1000000000) spawnPlatform();

        // update camera
        camera.translate(0, -300 * delta);
        camera.update();

        player.update(camera, platforms, delta);

        // draw
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(playerImage, player.rect.x, player.rect.y);
        for(Platform platform: platforms) {
            if (platform.willDel) {
                // platform = null;
            } else {
                batch.draw(platformImage, platform.rect.x, platform.rect.y);
            }
        }
        scoreBitmap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        scoreBitmap.draw(batch, "score: " + score, 25, camera.position.y + 1920 / 2 - 25);
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
        player.dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
}