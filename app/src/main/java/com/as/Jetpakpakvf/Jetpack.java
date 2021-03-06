package com.as.Jetpakpakvf;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.as.Jetpakpakvf.GameView.screenRatioX;
import static com.as.Jetpakpakvf.GameView.screenRatioY;

public class Jetpack {

    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap flight1, flight2, shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    private GameView gameView;

    Jetpack(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.jetpack);//récupère les images
        flight2 = BitmapFactory.decodeResource(res, R.drawable.jetpack);

        width = flight1.getWidth();//récupère la largeur et la hauteur de l'image
        height = flight1.getHeight();

        width /= 15;//réduction de la taille de l'image
        height /= 15;

        width = (int) (width * screenRatioX);//proportionne l'image à la taille de l'écran
        height = (int) (height * screenRatioY);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.jetpack);//récupère les images des balles
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.jetpack);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.jetpack);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.jetpack);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.jetpack);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);//création de l'image
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);


        dead = BitmapFactory.decodeResource(res, R.drawable.jetpack);//image de décès
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }

    Bitmap getFlight () {
//changements de images a chaque coup
        if (toShoot != 0) {

            if (shootCounter == 1) {
                shootCounter++;
                return shoot1;
            }

            if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            }

            if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            }

            if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            }

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();

            return shoot5;
        }

        if (wingCounter == 0) {
            wingCounter++;
            return flight1;
        }
        wingCounter--;

        return flight2;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {
        return dead;
    }//retourne l'image de décès'

}
