package com.as.Jetpakpakvf;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x = 0, y = 0;
    Bitmap background, background1, background2, background3,background4,background5,background6;

    Background (int screenX, int screenY, Resources res) {

        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        background1 = BitmapFactory.decodeResource(res, R.drawable.background1);
        background1 = Bitmap.createScaledBitmap(background1, screenX, screenY, false);
        background2 = BitmapFactory.decodeResource(res, R.drawable.background2);
        background2 = Bitmap.createScaledBitmap(background2, screenX, screenY, false);
        background3 = BitmapFactory.decodeResource(res, R.drawable.background3);
        background3 = Bitmap.createScaledBitmap(background3, screenX, screenY, false);
        background4 = BitmapFactory.decodeResource(res, R.drawable.background4);
        background4 = Bitmap.createScaledBitmap(background4, screenX, screenY, false);
        background5 = BitmapFactory.decodeResource(res, R.drawable.background5);
        background5 = Bitmap.createScaledBitmap(background5, screenX, screenY, false);
        background6 = BitmapFactory.decodeResource(res, R.drawable.background6);
        background6 = Bitmap.createScaledBitmap(background6, screenX, screenY, false);

    }

}
