package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by david on 10/15/2017.
 */

public class Explosion {
    //Bitmap to get character from image
    private Bitmap bitmap;
    private Bitmap bitmapsFrame[];
    private final int NUMFRAMES = 9;
    private final int NUMFRAMES_Y = 3;
    private final int NUMFRAMES_X = 3;
    private int currentFrame;

    //coordinates
    private float x;
    private float y;

    //how long and high our explosion will be
    private int width;
    private int height;

    private boolean isDestroyed;
    //constructor
    public Explosion(Context context, int screenX, int screenY, int spriteName) {
        //scales the ship
        width = screenX/3;
        height = screenY/3;

        isDestroyed = false;

        bitmap = BitmapFactory.decodeResource(context.getResources(), spriteName);

        //initialize bitmap array
        bitmapsFrame = new Bitmap[NUMFRAMES];

        //stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height,true);

        // cut bitmaps from  bmp to array of bitmaps
        currentFrame = 0;
        for (int i = 0; i < NUMFRAMES_Y; i++) {
            for (int j = 0; j < NUMFRAMES_X; j++) {
                bitmapsFrame[currentFrame] = Bitmap.createBitmap(bitmap,
                        (bitmap.getWidth()/NUMFRAMES_X)*j, (bitmap.getHeight()/NUMFRAMES_Y)*i,
                        bitmap.getWidth()/NUMFRAMES_X, bitmap.getHeight()/NUMFRAMES_Y);

                if (++currentFrame >= NUMFRAMES) {
                    currentFrame = 0;
                    break;
                }
            }
        }
    }

    public float getX() {return x-bitmapsFrame[currentFrame].getWidth()/2; }

    public float getY() {
        return y-bitmapsFrame[currentFrame].getHeight()/2;
    }

    void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDestroying() {
        return isDestroyed;
    }
    public void setDestroying() {
        isDestroyed = !isDestroyed ;
    }

    public Bitmap getBitmap(){
        if (++currentFrame >= NUMFRAMES){
            isDestroyed = false;
            currentFrame = 0;
        }
        Log.i(getClass().getName(), "Frame: "+String.valueOf(currentFrame));
        return bitmapsFrame[currentFrame];
    }

}
