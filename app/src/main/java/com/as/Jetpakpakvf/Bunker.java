package com.as.Jetpakpakvf;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.as.Jetpakpakvf.GameView.screenRatioX;
import static com.as.Jetpakpakvf.GameView.screenRatioY;

public class Bunker {

    public int speed = 20;
    public boolean wasShot = true;
    int x = 0, y, width, height, bunkerCounter = 1;
    Bitmap bunker, deadbunker;

    Bunker (Resources res) {

        bunker = BitmapFactory.decodeResource(res, R.drawable.bunker);

        width = bunker.getWidth();
        height = bunker.getHeight();

        width /= 12;
        height /= 12;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bunker = Bitmap.createScaledBitmap(bunker, width, height, false);

        y = -height;
        deadbunker = BitmapFactory.decodeResource(res, R.drawable.bunker);
        deadbunker = Bitmap.createScaledBitmap(deadbunker, width, height, false);
    }

    Bitmap getBunker () {


        return bunker;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
    Bitmap getDeadbunker () {
        return deadbunker;
    }
}

