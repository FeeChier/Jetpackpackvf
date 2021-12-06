package com.as.Jetpakpakvf;

import static com.as.Jetpakpakvf.GameView.screenRatioX;
import static com.as.Jetpakpakvf.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Coin {

    public int speed = 20;
    public boolean wasCollected = true;
    int x = 0, y, width, height, CoinCounter = 1;
    Bitmap coin;

    Coin (Resources res) {

        coin = BitmapFactory.decodeResource(res, R.drawable.dogecoin);
        width = coin.getWidth();
        height = coin.getHeight();

        width /= 12;
        height /= 12;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        coin = Bitmap.createScaledBitmap(coin, width, height, false);

        y = -height;
    }

    Bitmap getCoin () {
        return coin;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}

