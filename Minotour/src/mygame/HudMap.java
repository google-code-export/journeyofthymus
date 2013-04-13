/*
 * To change @author Cesar Frederick Tan this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ImageRenderer;
//import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;

/**
 *@author Cesar Frederick Tan
 * date written: April 12, 2013
 */
public class HudMap {
    private static int curRow, curCol;
    private static Nifty hudNifty;
    private static NiftyImage playerPos;
   
    public HudMap(Nifty passNifty)
    {
        hudNifty = passNifty;
        curRow = 0;
        curCol = 0;
        playerPos = hudNifty.getRenderEngine().createImage("Interface/playerLoc.png", false);
        //hudNifty.getCurrentScreen().findElementByName("txtLife").getRenderer(TextRenderer.class).setText(Integer.toString(playerLife));
    }
    
    private void updateLifeBar()
    {
        if (playerLife > CRITICAL_LEVEL)
            hudNifty.getCurrentScreen().findElementByName("lifeBar").getRenderer(ImageRenderer.class).setImage(okeyLevel);
        else
            hudNifty.getCurrentScreen().findElementByName("lifeBar").getRenderer(ImageRenderer.class).setImage(criticalLevel);
       
        int intLife = playerLife * MAX_LIFEBAR_WIDTH / MAX_HEALTH;
        //hudNifty.getCurrentScreen().findElementByName("txtLife").getRenderer(TextRenderer.class).setText(Integer.toString(intLife));
        hudNifty.getCurrentScreen().findElementByName("lifeBar").setWidth(intLife);
    }

    public void plasmaCaptured()
    {
        String eName;
        if (takenPlasma < TOTAL_PLASMA)
        {
            takenPlasma += 1;
            eName = "pc"+ Integer.toString(takenPlasma);
            hudNifty.getCurrentScreen().findElementByName(eName).getRenderer(ImageRenderer.class).setImage(plasmaOnImage);
        }
    }
    
    public boolean canTakeMoreHealthPack()
    {
        return (takenHpack < TOTAL_HPACK);
    }
    
    public boolean playerIsDead()
    {
        return (playerLife <= 0);
    }
    
    public boolean plasmaComplete()
    {
        return (takenPlasma >= TOTAL_PLASMA);
    }
    
    public void healthPackCaptured()
    {
        String eName;
        if (takenHpack < TOTAL_HPACK)
        {
            takenHpack += 1;
            eName = "hp" + Integer.toString(takenHpack);
            hudNifty.getCurrentScreen().findElementByName(eName).getRenderer(ImageRenderer.class).setImage(hpackOnImage);
        }
    }

    public int looseLife(int percent)
    {
        playerLife -= percent;
        if (playerLife < 0)
            playerLife = 0;  // this may trigger end Game.
        updateLifeBar();
        return playerLife;
    }
    
    public void consumeHealthPack()
    {
        String eName;
        if (takenHpack > 0 && playerLife < MAX_HEALTH)
        {
            eName = "hp" + Integer.toString(takenHpack);
            hudNifty.getCurrentScreen().findElementByName(eName).getRenderer(ImageRenderer.class).setImage(hpackOffImage);
            takenHpack -= 1;            
            playerLife += HPACKGAIN;
            if (playerLife > MAX_HEALTH) 
                playerLife = MAX_HEALTH;
            updateLifeBar();
        }
    }
}
