package com.as.Jetpakpakvf;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.as.Jetpakpakvf.GameView.screenRatioX;
import static com.as.Jetpakpakvf.GameView.screenRatioY;

public class Tour {

    public int speed = 20;
    public boolean wasShot = true;
    int x = 0, y, width, height, bunkerCounter = 1;
    Bitmap tour, deadbunker;

    Tour(Resources res) {

        tour = BitmapFactory.decodeResource(res, R.drawable.obstacleeffel);

        width = tour.getWidth();
        height = tour.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        tour = Bitmap.createScaledBitmap(tour, width, height, false);

        y = -height;
        deadbunker = BitmapFactory.decodeResource(res, R.drawable.obstacleeffel);
        deadbunker = Bitmap.createScaledBitmap(deadbunker, width, height, false);
    }

    Bitmap getTour () {
        return tour;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}

