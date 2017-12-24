package com.oomph.spacemarauders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 *  Created by david on 5/27/2017.
 */

public class DefenceBrick {
    private RectF detectCollision;
    private Bitmap bitmap;
    private boolean isVisible;
    //x and y coordinates
    private float x;
    private float y;
    private int width;
    private int height;
    public DefenceBrick(Context context, int row, int column, int shelterNumber, int screenX, int screenY){
        width = screenX / 50;
        height = screenY / 25;
        isVisible = true;
        //sometimes a bullet slips through this padding
        //setting padding to zero if this annoys you
        int brickPadding =5;

        //the number of shelters
        float shelterPadding = screenX / 11.0f;
        float startHeight = screenY - ( screenY / 3);
        x = column * (width + brickPadding) + (shelterPadding *shelterNumber)+shelterPadding +(shelterPadding * shelterNumber);
        y = row * (height + brickPadding) + startHeight;
        //initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shelter);
        //stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height,false);
        //detectCollision = new RectF(x, y, bitmap.getWidth(), bitmap.getHeight());
        detectCollision = new RectF(
                column * (width + brickPadding) + (shelterPadding *shelterNumber)+shelterPadding +(shelterPadding * shelterNumber),
                row * (height + brickPadding) - brickPadding + startHeight,
                column * (width + brickPadding) + (shelterPadding *shelterNumber)+shelterPadding +(shelterPadding * shelterNumber)+width,
                row * (height + brickPadding) - brickPadding + startHeight + height);
    }
    public RectF getDetectCollision(){
        return detectCollision;
    }
    public void setInvisible(){
        isVisible = false;
    }
    public boolean isVisible(){
        return isVisible;
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
}
