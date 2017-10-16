package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by david on 5/27/2017.
 */

public class Bullet {
    Rect detectCollision;
    private Bitmap bitmap;
    //how long and height bullet will be
    private int height;
    private int length;
    //which way is it shooting
    public final int UP= 0;
    public final int DOWN = 1;
    //going nowhere
    int heading = -1;

    private int x;
    private int y;

    float speed;


    private boolean isActive;

    public Bullet(Context context, int screenX, int screenY){
        //initialize  a blank Rect
        detectCollision = new Rect();
        //scales the bullet
        length = screenX/60;
        height = screenY/60;

        //initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership);
        //stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)length, (int)height,false);
        //how fast is the spaceship is  moving pixels per sec
        speed = 3;
    }
    //getter method to make the rectangle define our ships available in the space invader view
    public Bitmap getBitmap(){
        return bitmap;
    }
    public Rect getDetectCollision(){
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
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean shoot(int startX, int startY, int direction){
        if(!isActive){
            x = startX;
            y = startY-150;
            heading = direction;
            isActive = true;
            return true;
        }
        //bullet already active
        return false;
    }
    public void update(){
        if(heading == UP){
            y -= speed;
        }else{
            y += speed;
        }
        //Update Rect
        detectCollision.left = x;
        detectCollision.right = x + length;
        detectCollision.top = y;
        detectCollision.bottom = y + height;
    }
}
