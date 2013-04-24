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
public class Map {
    private static float mapWidth, mapHeight, mapX, mapY;
    private static Picture mapImage;
    
    public Map(Picture map_image, float Width, float Height, float x, float y)
    {
        mapImage = map_image;
        mapWidth = Width;
        mapHeight = Height;
        mapX = x;
        mapY = y;
        mapImage.setWidth(mapWidth);    
        mapImage.setHeight(mapHeight);
        mapImage.setPosition(mapX, mapY);
    }
    
    public float getX()
    {
        return mapX;
    }
    
    public float getY()
    {
        return mapY;
    }
    
    public float getWidth()
    {
        return mapWidth;
    }
    
    public float getHeight()
    {
        return mapHeight;
    }
}
