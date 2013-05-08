package states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import com.jme3.math.Vector3f;
import driver.ApplicationInterface;
import generators.TerrainBuilder;
import player.PlayerController;

/**
 *
 * @author MIKUiqnw0
 * @since 21/03/2013
 * @version 0.00.01
 */
public class GameState extends AbstractAppState {

    private PlayerController playerControl;
    private ApplicationInterface app;
    private TerrainBuilder terrainBuilder;

    public GameState(ApplicationInterface app) {
        this.app = app;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initializeMap();
        initializePlayer();
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);  
    }
    
    public void initializeMap() {
        terrainBuilder = new TerrainBuilder(app.getAssetManager(), app.getRootNode(), app);
        terrainBuilder.buildMap();
    }
    
    public void initializePlayer() {
        Node playerNode = new Node("Player");
        app.getRootNode().attachChild(playerNode);
        playerControl = new PlayerController(app);
        playerControl.setGravity(1);
        playerControl.setFallSpeed(1);
        playerControl.setupKeys();
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(playerControl);
        playerNode.addControl(playerControl);        
        playerControl.setPhysicsLocation(terrainBuilder.getSpawnPoint());
        
    }
    
    public void initializeItems() {
    }
    
}
