package com.oomph.spacemarauders;

/**
 * Created by david on 9/19/2017.
 **/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

public class Enemy {
    private Random generator = new Random();
    //bitmap for the enemy
    //we have already pasted the bitmap in the drawable folder
    private Bitmap bitmap;
    private Bitmap bitmap2;

    //how long and high our invaders will be
    private int width;
    private int height;

    //x and y coordinates
    private float x;
    private float y;

    //enemy speed
    private float speed;

    //Gravity Value to add gravity effect on the ship
    private final float GRAVITY = 9.8f;

    private float newHeight;
    //min and max coordinates to keep the enemy inside the screen
    private int maxX;
    private int minX;

    private int maxY;
    private int minY;


    private final int LEFT = 1;
    private final int RIGHT = 2;

    //is the ship moving and in which direction
    private int shipMoving = RIGHT;

    //has enemy been destroyed
    private boolean isVisible;

    //creating a rect object
    private RectF detectCollision;

    public Enemy(Context context, int row, int column, int screenX, int screenY) {


        //getting bitmap from drawable resource
        width = screenX/20;
        height = screenY/20;
        isVisible = true;

        int padding =20;
        x = column * (width+padding);
        y = row * (height + padding) +51;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roundysh);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.aliensh);

        //stretch the first bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, width, height, false);

        //how fast is the invader in pixels per second
        speed = 10;
        //initializing min and max coordinates
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //generating a random coordinate to add enemy
       // Random generator = new Random();
        //speed = generator.nextInt(6) + 10;
       // x = screenX;
        //y = generator.nextInt(maxY) - bitmap.getHeight();
        newHeight = y;
        //initializing rect object
        detectCollision = new RectF(x, y, x+ width, y+height);
    }

    public void update(long fps) {
        //decreasing x coordinate so that enemy will move right to left
        //x -= playerSpeed;
        //x -= speed;

        //if the enemy reaches the left edge
        if(shipMoving == LEFT){
            if(y < newHeight){
                y += 0.5f * (speed * GRAVITY) / fps;
            }else {
                x -= 0.5f * (speed * GRAVITY) / fps;
                newHeight = y;
            }
        }
        if(shipMoving == RIGHT){
            if(y < newHeight){
                y += 0.5f * (speed * GRAVITY) / fps;
            }else {
                x += 0.5f * (speed * GRAVITY) / fps;
                newHeight = y;
            }
        }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }
    public void dropDownAndReverse(long fps){
        Log.i(getClass().getName(), " AlienX colliding: "+x);
        if(shipMoving ==LEFT){
            shipMoving = RIGHT;
            x += 0.5f * (speed * GRAVITY) / fps;
        }
        else {
            shipMoving = LEFT;
            x -= 0.5f * (speed * GRAVITY) / fps;
        }
        if(y == newHeight) {
            newHeight += height;
            //y += height;
            speed *= 1.18f;
        }
    }



    //one more getter for getting the rect object
    public RectF getDetectCollision() {
        return detectCollision;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getBitmap2(){
        return bitmap2;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public void setInvisible(){
        isVisible = false;
    }
    public Boolean isVisible(){
        return isVisible;
    }

    public float getLength(){
        return width;
    }
    public int getHeight() {
        return height;
    }

    public boolean takeAim(float playerShipX, float playerShipLength) {
        // if near the player
        if ((playerShipX + playerShipLength > x
                && playerShipX + playerShipLength < x + width)
                || (playerShipX > x && playerShipX < x + width)) {
            // A 1 in 500 chance to shoot

            if (generator.nextInt(150) == 0) {
                return true;
            }
        }
        // if firing randomly (not near the player) a 1 in 5000 chance)
        return generator.nextInt(2000) == 0;
    }
}