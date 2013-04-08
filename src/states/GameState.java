package states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import driver.ApplicationInterface;
import excep.LoadingException;
import generators.TerrainBuilder;
import items.ItemController;
import player.PlayerController;

/**
 *
 * @author MIKUiqnw0
 * @since 21/03/2013
 * @version 0.00.01
 */
public class GameState extends AbstractAppState {

    private PlayerController playerControl;
    private ItemController itemControl;
    private ApplicationInterface app;
    private TerrainBuilder terrainBuilder;

    public GameState(ApplicationInterface app) {
        this.app = app;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        try {
            initializePlayer();
        } catch (LoadingException e) {
            
        }
        initializeMap();
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);  
    }
    
    public void initializeMap() {
        terrainBuilder = new TerrainBuilder(app.getAssetManager(), app.getRootNode());
        terrainBuilder.buildMap();
    }
    
    public void initializePlayer() throws LoadingException {
        Node playerNode = new Node("Player");
        app.getRootNode().attachChild(playerNode);
        playerControl = new PlayerController(app);
        playerControl.setupKeys();
    }
    
    public void initializeItems() {
    }
    
}
