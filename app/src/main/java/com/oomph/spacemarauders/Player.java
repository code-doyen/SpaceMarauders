package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by david on 9/18/2017.
 */

public class Player {
    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //how long and high our ship will be
    private int length;
    private int height;

    //motion speed of the character
    private int speed = 0;

    //boolean variable to track the ship is boosting or not
    private boolean boosting;

    //Gravity Value to add gravity effect on the ship
    private final int GRAVITY = -10;

    //Controlling X and Y coordinate so that ship won't go outside the screen
    private int maxY;
    private int minY;
    private int maxX;
    private int minX;


    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    //creating a rect object
    private Rect detectCollision;

    //constructor
    public Player(Context context, int screenX, int screenY) {
        //scales the ship
        length = screenX/20;
        height = screenY/20;

        // Start ship in roughly the screen centre
        x = screenX / 2;
        y = screenY - 120;
        speed = 1;

        //Getting bitmap from drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mship1);
        //stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)length, (int)height,false);

        //calculating maxY
        maxY = screenY - bitmap.getHeight();

        //calculating maxX
        maxX = screenX - bitmap.getWidth();

        //top edge's x and y point is 0 so min x and y will always be zero
        minY = 0;
        minX = 0;

        //setting the boosting value to false initially
        boosting = false;

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    //setting boosting true
    public void setBoosting() {
        boosting = true;
    }

    //setting boosting false
    public void stopBoosting() {
        boosting = false;
    }

    //Method to update coordinate of character
    public void update(){
        //updating x coordinate
        //x++;
        //if the ship is boosting
        if (boosting) {
            //speeding up the ship
            speed += 1;
        } else {
            //slowing down if not boosting
            speed -= 1;
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
        x -= speed + GRAVITY;

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
    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength(){
        return length;
    }

    public int getSpeed() {
        return speed;
    }
}
