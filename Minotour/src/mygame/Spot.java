/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.ui.Picture;

/**
 *
 * @author Cesar Frederick Tan
 */
public class Spot {
    private Map mapSpot;
    private Picture picSpot;
    private float pHeight, pWidth, minX, maxX, minY, maxY, curX, curY, curPX, curPY;
    
    public Spot(Picture SpotImage, Map map)
    {
        mapSpot = map;
        picSpot = SpotImage;
        pWidth = 10;
        pHeight = 10;
        curX = 0;
        curY = 0;
        minX = mapSpot.getX();
        maxX = mapSpot.getX() + mapSpot.getWidth() - pWidth;
        minY = mapSpot.getY() + mapSpot.getHeight() - pHeight;
        maxY = mapSpot.getY();
        curPX = minX;
        curPY = minY;
        picSpot.setWidth(pWidth);
        picSpot.setHeight(pHeight);
    }

    public void setSpot(float onX, float onY)
    {
        setSpotX(onX);
        setSpotY(onY);
        DisplaySpot();
    }
    
    public void setSpotX(float atX)
    {
        float pX = minX + atX;
        if (pX <= maxX && pX >= minX)
        {
            curX = atX;
            curPX = pX;
        }
    }
    
    public void setSpotY(float atY)
    {
        float pY = minY - atY;
        if (pY >= maxY && pY <= minY)
        {
            curY = atY;
            curPY = pY;
        }
    }
    
    public void DisplaySpot()
    {
        picSpot.setPosition(curPX, curPY);
    }
    
    public float getMinX()
    {
        return minX;
    }
    
    public float getMaxX()
    {
        return maxX;
    }
    
    public void GoRightBy(float Steps)
    {
        this.setSpot(curX + Steps, curY);
    }
    
    public void GoLeftBy(float Steps)
    {
        this.setSpot(curX - Steps, curY);
    }
    
    public void GoUpBy(float Steps)
    {
        this.setSpot(curX, curY - Steps);
    }

    public void GoDownBy(float Steps)
    {
        this.setSpot(curX, curY + Steps);
    }
    
    public float getCurX()
    {
        return curX;
    }
    
    public float getCurY()
    {
        return curY;
    }
}
