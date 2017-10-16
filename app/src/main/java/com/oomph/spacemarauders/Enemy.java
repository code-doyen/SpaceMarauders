package com.oomph.spacemarauders;

/**
 * Created by david on 9/19/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Enemy {
    Random generator = new Random();
    //bitmap for the enemy
    //we have already pasted the bitmap in the drawable folder
    private Bitmap bitmap;
    private Bitmap bitmap2;

    //how long and high our invaders will be
    private int length;
    private int height;

    //x and y coordinates
    private int x;
    private int y;

    //enemy speed
    private float speed;

    //min and max coordinates to keep the enemy inside the screen
    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    //This will hold the pixels per second speed that the invader will move
    //private float shipSpeed;

    public final int LEFT = 1;
    public final int RIGHT = 2;

    //is the ship moving and in which direction
    private int shipMoving = RIGHT;

    //has enemy been destroyed
    boolean isVisible;

    //creating a rect object
    private Rect detectCollision;

    public Enemy(Context context, int row, int column, int screenX, int screenY) {


        //getting bitmap from drawable resource
        length = screenX/20;
        height = screenY/20;
        isVisible = true;

        int padding =screenX /25;
        x = column * (length+padding);
        y = row * (length + padding/4);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roundysh);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.aliensh);

        //stretch the first bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, length, height, false);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, length, height, false);

        //how fast is the invader in pixels per second
        speed = 2.5f;
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

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        //decreasing x coordinate so that enemy will move right to left
        //x -= playerSpeed;
        //x -= speed;

        //if the enemy reaches the left edge
        if(shipMoving == LEFT){
            x -= speed;
        }
        if(shipMoving == RIGHT){
            x += speed;
        }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }
    public void dropDownAndReverse(){
        if(shipMoving ==LEFT){
            shipMoving = RIGHT;
        }
        else
            shipMoving = LEFT;
        y += height;
        speed *= 1.18f;
    }



    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getBitmap2(){
        return bitmap2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //public int getSpeed() {
    //    return speed;
    //}
    public void setInvisible(){
        isVisible = false;
    }
    public Boolean getVisibility(){
        return isVisible;
    }

    public int getlength(){
        return length;
    }

    public boolean takeAim(float playerShipX, float playerSHipLength) {
        int randomNum = -1;
        // if near the player
        if ((playerShipX + playerSHipLength > x
                && playerShipX + playerSHipLength < x + length)
                || (playerShipX > x && playerShipX < x + length)) {
            // A 1 in 500 chance to shoot
            randomNum = generator.nextInt(150);
            if (randomNum == 0) {
                return true;
            }
        }
        // if firing randomly (not near the player) a 1 in 5000 chance)
        randomNum = generator.nextInt(2000);
        if (randomNum == 0) {
            return true;
        }
        return false;
    }
}