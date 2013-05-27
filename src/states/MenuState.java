package states;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import driver.GameController;
import driver.ThymusApplication;

/**
 *
 * @author
 */
public class MenuState extends AbstractAppState implements ScreenController {

    GameController gameController;
    
    public MenuState(GameController gameController) {
        this.gameController = gameController;
    }
    
    public void start(String nextScreen) {
        gameController.startGame(nextScreen);
    }
    
    @Override
    public void stateAttached(AppStateManager stateManager) {
    }
    
    @Override
    public void stateDetached(AppStateManager stateManager) {
    }

    @Override
    public void update(float tpf) {
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        
    }

    @Override
    public void onStartScreen() {
       
    }

    @Override
    public void onEndScreen() {
        
    }
    
}
