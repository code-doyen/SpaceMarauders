package com.oomph.spacemarauders;

import android.graphics.Rect;

/**
 * Created by david on 5/27/2017.
 */

public class DefenceBrick {
    private Rect rect;
    private boolean isVisible;
    public DefenceBrick(int row, int column, int shelterNumber, int screenX, int screenY){
        int width = screenX / 50;
        int height = screenY / 15;

        isVisible = true;
        //sometimes a bullet slips through this padding
        //setting padding to zero if this annoys you
        int brickPadding =1;

        //the number of shelters
        int shelterPadding = screenX / 9;
        int startHeight = screenY - ( screenY / 3);

        rect = new Rect(column * width + brickPadding + (shelterPadding *shelterNumber)+
                shelterPadding +shelterPadding * shelterNumber,
                row*height + brickPadding + startHeight,
                column * width + width -brickPadding + (shelterPadding * shelterNumber) +
                shelterPadding + shelterPadding * shelterNumber,
                row * height + height - brickPadding + startHeight);
    }
    public Rect getRect(){
        return this.rect;
    }
    public void setInvisible(){
        isVisible = false;
    }
    public boolean getInvisibility(){
        return isVisible;
    }
}
