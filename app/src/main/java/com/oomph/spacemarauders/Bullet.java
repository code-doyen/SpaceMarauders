package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 *  Created by david on 5/27/2017.
 */

public class Bullet {
    private RectF detectCollision;
    private Bitmap bitmap;
    private Bitmap bitmapsFrame[];
    private final int NUMFRAMES = 5;
    private final int NUMFRAMES_Y = 5;
    private final int NUMFRAMES_X = 1;
    private int currentFrame;
    //how long and height bullet will be
    private int height;
    private int width;
    //which way is it shooting
    public final int UP= 0;
    public final int DOWN = 1;
    //going nowhere
    private int heading = -1;

    private float x;
    private float y;

    private float speed;
    //Gravity Value to add gravity effect on the ship
    private final float GRAVITY = 9.8f;

    private boolean isActive;

    public Bullet(Context context, int screenX, int screenY, int spriteName){
        //initialize  a blank Rect
        detectCollision = new RectF();
        //scales the bullet
        width = screenX/60;
        height = screenY/4;

        //initialize the bitmap
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
        //how fast is the bullet is  moving pixels per sec
        speed = 50;
    }
    //getter method to make the rectangle define our ships available in the space invader view
    public Bitmap getBitmap(){
        if (++currentFrame >= NUMFRAMES)
            currentFrame = 0;
        return bitmapsFrame[currentFrame];
    }
    public RectF getDetectCollision(){
        return detectCollision;
    }
    public boolean getStatus(){
        return isActive;
    }
    public void setInactive(){
        isActive = false;
    }
    public float getImpactPointY(){
        if(heading == DOWN){
            return y + bitmapsFrame[currentFrame].getHeight();
        }else{
            return y;
        }
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public int getCenterX(){
        return bitmapsFrame[currentFrame].getWidth() / 2;
    }
    public int getCenterY(){
        return bitmapsFrame[currentFrame].getHeight() / 2;
    }

    public boolean shoot(float startX, float startY, int direction){
        if(!isActive){
            x = startX;
            y = startY;//-150;
            heading = direction;
            isActive = true;
            return true;
        }
        //bullet already active
        return false;
    }
    public void update(long fps){
        if(heading == UP){
            y -= 0.5f*(speed * GRAVITY)/fps;
        }else{
            y += 0.5f*(speed * GRAVITY)/fps;
        }
        //Update Rect
        detectCollision.left = x;
        detectCollision.right = x + bitmapsFrame[currentFrame].getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmapsFrame[currentFrame].getHeight();
    }
}
