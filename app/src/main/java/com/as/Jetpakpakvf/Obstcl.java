package com.as.Jetpakpakvf;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.as.Jetpakpakvf.GameView.screenRatioX;
import static com.as.Jetpakpakvf.GameView.screenRatioY;

public class Obstcl {

    public int speed = 50;
    public boolean wasShot = true;
    int x = 0, y, width, height, birdCounter = 1;
    Bitmap bird1, bird2, bird3, bird4;

    Obstcl(Resources res) {

        bird1 = BitmapFactory.decodeResource(res, R.drawable.pigeon);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.pigeon);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.pigeon);
        bird4 = BitmapFactory.decodeResource(res, R.drawable.pigeon);

        width = bird1.getWidth();
        height = bird1.getHeight();

        width /= 12;
        height /= 12;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false);

        y = -height;
    }

    Bitmap getBird () {

        if (birdCounter == 1) {
            birdCounter++;
            return bird1;
        }

        if (birdCounter == 2) {
            birdCounter++;
            return bird2;
        }

        if (birdCounter == 3) {
            birdCounter++;
            return bird3;
        }

        birdCounter = 1;

        return bird4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
