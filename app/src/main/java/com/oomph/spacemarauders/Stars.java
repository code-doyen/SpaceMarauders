package com.oomph.spacemarauders;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by david on 9/19/2017.
 */

public class Stars {
    private float x;
    private float y;
    private int speed;
    private int color;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    //Gravity Value to add gravity effect on the ship
    private final float GRAVITY = 9.8f;
    private Random generator = new Random();

    public Stars(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        speed = generator.nextInt(20);

        //generating a random coordinate
        //but keeping the coordinate inside the screen size
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);

        switch(generator.nextInt(5)) {
            case 0:
                color = Color.WHITE;
                break;
            case 1:
                color = Color.BLUE;
                break;
            case 2:
                color = Color.MAGENTA;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.GREEN;
                break;
        }
    }

    public void update(long fps) {
        //animating the star horizontally left side
        //by decreasing x coordinate with player speed
        //x -= playerSpeed;
        //x -= speed;
        //if the star reached the left edge of the screen
//        if (x < 0) {
//            //again starting the star from right edge
//            //this will give a infinite scrolling background effect
//            x = maxX;
//            Random generator = new Random();
//            y = generator.nextInt(maxY);
//            speed = generator.nextInt(15);
//        }

        //animating the star horizontally left side
        //by decreasing y coordinate with player speed
        y+= 0.5f*(speed * GRAVITY)/fps;
        //if the star reached the left edge of the screen
        if (y > maxY) {
            //again starting the star from right edge
            //this will give a infinite scrolling background effect
            y = maxY;
            y = generator.nextInt(maxY)+ 0.5f*(generator.nextInt(15) * GRAVITY)/fps;
        }
    }

    public float getStarWidth() {
        //Making the star width random so that
        //it will give a real look
        float minX = 1.0f;
        float maxX = 4.0f;
        return new Random().nextFloat() * (maxX - minX) + minX;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public int getColor(){return color;}
}
