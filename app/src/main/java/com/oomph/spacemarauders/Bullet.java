package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 *  Created by david on 5/27/2017.
 */

public class Bullet {
    private RectF detectCollision;
    private Bitmap bitmap;
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

    public Bullet(Context context, int screenX, int screenY){
        //initialize  a blank Rect
        detectCollision = new RectF();
        //scales the bullet
        width = screenX/60;
        height = screenY/60;

        //initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership);
        //stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height,false);
        //how fast is the spaceship is  moving pixels per sec
        speed = 50;
    }
    //getter method to make the rectangle define our ships available in the space invader view
    public Bitmap getBitmap(){
        return bitmap;
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
            return y + height;
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
    public int getLength(){
        return width;
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
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();
    }
}
