package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by david on 10/15/2017.
 */

public class Explosion {
    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates
    private float x;
    private float y;

    //how long and high our explosion will be
    private int length;
    private int height;

    private boolean visible;
    //constructor
    public Explosion(Context context, int screenX, int screenY) {
        //scales the ship
        length = screenX/20;
        height = screenY/20;

        //Getting bitmap from drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);
        //stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, length, height,false);

        visible = false;
    }
    void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public float getX() {return x; }

    public float getY() {
        return y;
    }

    boolean isVisible(){ return visible; }

    void setVisible(){ visible = !visible; }

    public Bitmap getBitmap(){
        return bitmap;
    }
}
