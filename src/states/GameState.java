package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import items.ItemController;
import player.PlayerController;

/**
 *
 * @author MIKUiqnw0
 * @since 21/03/2013
 * @version 0.00.01
 */
public class GameState extends AbstractAppState {

    private Node rootNode;
    private InputManager inputManager;
    private AssetManager assetManager;
    private PlayerController playerControl;
    private ItemController itemControl;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        rootNode = ((SimpleApplication) app).getRootNode();
        inputManager = ((SimpleApplication) app).getInputManager();
        assetManager = ((SimpleApplication) app).getAssetManager();
        
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        try {
            initializePlayer();
        } catch (Exception e) {
            
        }
        
    }
    
    public void initializePlayer() {      
        playerControl = new PlayerController(inputManager);
        playerControl.setupKeys();
        //model loading
        
    }
    
    
}
