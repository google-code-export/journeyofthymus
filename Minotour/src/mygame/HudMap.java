package mygame;

import de.lessvoid.nifty.Nifty;

/**
 *@author Cesar Frederick Tan
 * Date written: April 12, 2013
 * Last Update:  April 20, 2013
 */

public class HudMap 
{
    private static final int 
            MAX_HEIGHT = 140, 
            MAX_WIDTH = 200, 
            MIN_HEIGHT = 0, 
            MIN_WIDTH = 0;
    private static int 
            curRow, 
            curCol;
    private static Nifty 
            mapNifty;
   
    public HudMap(Nifty nifty)
    {
        mapNifty = nifty;
        curRow = 0;
        curCol = 0;
     }
    
    private void PutInColumn(int NewColumn)
    {
        if (NewColumn <= MAX_WIDTH && NewColumn >= MIN_WIDTH )
        {
            curCol = NewColumn;
            mapNifty.getCurrentScreen().findElementByName("ColPos").setWidth(curCol);
        }
    }
    
    private void PutInRow(int NewRow)
    {
        if (NewRow <= MAX_HEIGHT && NewRow >= MIN_HEIGHT)
        {
            curRow = NewRow;
            mapNifty.getCurrentScreen().findElementByName("RowPos").setHeight(curRow);
        }
    }
    
    public void MovePlayerOn(int NewRow, int NewColumn)
    {
        PutInRow(NewRow);
        PutInColumn(NewColumn);
    }
    
    public void GoUpBy(int Steps)
    {
        PutInRow(curRow + Steps);
    }
 
    public void GoDownBy(int Steps)
    {
        PutInRow(curRow - Steps);
    }
    
    public void GoLeftBy(int Steps)
    {
        PutInColumn(curCol - Steps);
    }
    
    public void GoRightBy(int Steps)
    {
        PutInColumn(curCol + Steps);
    }
    
    public int GetRow()
    {
        return curRow;
    }
    
    public int GetColumn()
    {
        return curCol;
    }
}