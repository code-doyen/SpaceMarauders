package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by david on 9/18/2017.
 */

public class Player {
    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates
    private float x;
    private float y;

    //how long and high our ship will be
    private int width;
    private int height;

    //motion speed of the character
    private int speed = 0;

    //variables to set the ship's movement direction
    public final int LEFT = -1;
    public final int NEUTRAL = 0;
    public final int RIGHT = 1;
    private int direction;


    //Gravity Value to add gravity effect on the ship
    private final float GRAVITY = 9.8f;

    //Controlling X and Y coordinate so that ship won't go outside the screen
//    private int maxY;
//    private int minY;
    private int maxX;
    private int minX;


    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = -100;
    private final int MAX_SPEED = 100;

    //creating a rect object
    private RectF detectCollision;

    //constructor
    public Player(Context context, int screenX, int screenY) {
        //scales the ship
        width = screenX/20;
        height = screenY/20;

        // Start ship in roughly the screen centre
        x = screenX / 2;
        y = screenY - 120;


        //Getting bitmap from drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mship1);
        //stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height,false);

        //calculating maxY
//        maxY = screenY - bitmap.getHeight();

        //calculating maxX
        maxX = screenX - bitmap.getWidth();

        //top edge's x and y point is 0 so min x and y will always be zero
        //minY = 0;
        minX = 0;

        //initializing rect object
        detectCollision = new RectF(x, y, x+ bitmap.getWidth(), y + bitmap.getHeight());

    }

    //setting boosting true
    public void setMovement(int d) {
        direction = d;
    }

    //Method to update coordinate of character
    public void update(long fps){
        //determine the direction to move ship
        if (direction == RIGHT) {
            //speeding up the ship
            speed += 5;
        } else if(direction == LEFT){
            //slowing down if not boosting
            speed -= 5;
        }
        else {
            if(speed < 0)
                speed += 5;
            else if(speed >0)
                speed -= 5;
            else
                speed = 0;
        }
        //controlling the top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        //if the speed is less than min speed
        //controlling it so that it won't stop completely
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        //moving the ship down
        //y -= speed + GRAVITY;

        //moving the ship across
        // vf = vi + at
        Log.i(getClass().getName(), "Speed: "+String.valueOf(speed));
        x += 0.5f*(speed * GRAVITY)/fps;

        //but controlling it also so that it won't go off the screen
//        if (y < minY) {
//            y = minY;
//        }
//        if (y > maxY) {
//            y = maxY;
//        }
        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }

        //adding top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    /*
    * These are getters you can generate it automatically
    * right click on editor -> generate -> getters
    * */

    //one more getter for getting the rect object
    public RectF getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getLength(){
        return width;
    }

}
