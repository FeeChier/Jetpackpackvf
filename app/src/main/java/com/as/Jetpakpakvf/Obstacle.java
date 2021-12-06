package com.as.Jetpakpakvf;

import static com.as.Jetpakpakvf.GameView.screenRatioX;
import static com.as.Jetpakpakvf.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Obstacle {
    int x, y, width, height;
    Bitmap Obstacle;
    
    Obstacle (Resources res) {

        Obstacle = BitmapFactory.decodeResource(res, R.drawable.obstacle);

        width = Obstacle.getWidth();
        height = Obstacle.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        Obstacle = Bitmap.createScaledBitmap(Obstacle, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}

