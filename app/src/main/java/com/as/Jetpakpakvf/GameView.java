package com.as.Jetpakpakvf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score ,coinscore= 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Bird[] birds;
    private Coin[] coins;
    private Bunker[] bunkers;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    private Flight flight;
    private Bullet bullet;
    private GameActivity activity;
    private Background background1, background2;


    private Bitmap coin ;
    private int coinX;
    private int coinY;
    private int coinSpeed;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        coin = BitmapFactory.decodeResource(getResources(), R.drawable.dogecoin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 2400f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources());
        bullet = new Bullet(getResources());
        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        birds = new Bird[4];
        coins = new Coin[4];
        bunkers = new Bunker[4];

        for (int i = 0;i < 4;i++) {

            Bird bird = new Bird(getResources());
            birds[i] = bird;

        }

        for (int j = 0; j<4; j++){
            Coin coin = new Coin(getResources());
            coins[j] = coin;
        }
        for (int x = 0; x<4; x++){
            Bunker bunker = new Bunker(getResources());
            bunkers[x] = bunker;
        }
        random = new Random();

    }

    @Override
    public void run() {

        while (isPlaying) {

            update ();
            try{

                draw ();
            }catch (ConcurrentModificationException e){

            }
            sleep ();

        }

    }

    private void update () {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (flight.isGoingUp)
            flight.y -= 20 * screenRatioY;
        else
            flight.y += 20 * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.y > screenY)
                trash.add(bullet);

            bullet.y += 20 * screenRatioY;
/*
            for (Bird bird : birds) {

                if (Rect.intersects(bird.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    bird.x = -500;
                    bullet.y = screenY + 500;
                    bird.wasShot = true;

                }

            }*/
            for (Bunker bunker : bunkers) {

                if (Rect.intersects(bunker.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    bunker.x = -500;
                    bullet.y = screenY + 500;
                    bunker.wasShot = true;

                }

            }

        }

        for (Bullet bullet : trash)
            try {
                bullets.remove(bullet);
            }catch (ConcurrentModificationException e){

            }


        for (Bird bird : birds) {

            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {
/*
                if (!bird.wasShot) {
                    isGameOver = true;
                    return;
                }*/

                int bound = (int) (10 * screenRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);

                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height);

                bird.wasShot = false;
            }

            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                score++;
                isGameOver = true;
                return;
            }

        }
        for (Bunker bunker : bunkers) {

            bunker.x -= bunker.speed;

            if (bunker.x + bunker.width < 0) {

                int bound = (int) (10 * screenRatioX);
                bunker.speed = random.nextInt(bound);

                if (bunker.speed < 10 * screenRatioX)
                    bunker.speed = (int) (10 * screenRatioX);

                bunker.x = screenX;
                bunker.y = 1000;

                bunker.wasShot = false;
            }
/*
            if (Rect.intersects(bunker.getCollisionShape(), flight.getCollisionShape())) {
                score++;
                return;
            }*/

        }
        for (Coin coin : coins){
            coin.x -= coin.speed;

            if (coin.x + coin.width < 0) {

                int bound = (int) (10 * screenRatioX);
                coin.speed = random.nextInt(bound);

                if (coin.speed < 10 * screenRatioX)
                    coin.speed = (int) (10 * screenRatioX);

                coin.x = screenX;
                coin.y = random.nextInt(screenY - coin.height);
                coin.wasCollected = false;
            }
                if (Rect.intersects(coin.getCollisionShape(), flight.getCollisionShape())) {
                    coinscore++;

                    coin.x = -500;
                    bullet.y = screenY +500;
                    coin.wasCollected = true;
                    return;
                } 
        }
    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            if(score>10){

                canvas.drawBitmap(background1.background1, background1.x, background1.y, paint);
                canvas.drawBitmap(background2.background1, background2.x, background2.y, paint);
            }
            if(score>20){

                canvas.drawBitmap(background1.background2, background1.x, background1.y, paint);
                canvas.drawBitmap(background2.background2, background2.x, background2.y, paint);
            }
            if(score>100){

                canvas.drawBitmap(background1.background3, background1.x, background1.y, paint);
                canvas.drawBitmap(background2.background3, background2.x, background2.y, paint);
            }
            if(score>200){

                canvas.drawBitmap(background1.background4, background1.x, background1.y, paint);
                canvas.drawBitmap(background2.background4, background2.x, background2.y, paint);
            }
            if(score>500){

                canvas.drawBitmap(background1.background5, background1.x, background1.y, paint);
                canvas.drawBitmap(background2.background5, background2.x, background2.y, paint);
            }
            if(score>1000){

                canvas.drawBitmap(background1.background5, background1.x, background1.y, paint);
                canvas.drawBitmap(background2.background5, background2.x, background2.y, paint);
            }

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            for (Coin coin : coins)
                canvas.drawBitmap(coin.getCoin(), coin.x, coin.y, paint);
            for (Bunker bunker : bunkers)
                canvas.drawBitmap(bunker.getBunker(), bunker.x,bunker.y,paint);

            paint.setTextSize(50);
            canvas.drawText("Score : " + score, screenX / 20, 100, paint);

            canvas.drawText("Coins : " + coinscore , screenX /20, 200, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                savecoinScore();
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets)
                try{
                    canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
                }catch (ConcurrentModificationException e){

                }

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }
    private void savecoinScore() {
        coinscore+=prefs.getInt("Coins",0);
        SharedPreferences.Editor editorcoin = prefs.edit();
        editorcoin.putInt("Coins", coinscore);
        editorcoin.apply();
    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                flight.isGoingUp = true;
                if (!prefs.getBoolean("isMute", false))
                    soundPool.play(sound, 1, 1, 0, 0, 1);
                flight.toShoot++;

                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                flight.toShoot=0;
                break;
        }

        return true;
    }
    public void newBullet() {

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);


    }
}
