package states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
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
    private CameraNode camNode;
    private Node playerNode;

    public GameState(ApplicationInterface app) {
        this.app = app;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initializeMap();
        initializeNodes();
        initializeCamera();
        initializePlayer();
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        camNode.setLocalTranslation(playerControl.getPhysicsLocation());
    }
    
    private void initializeNodes() {
        camNode = new CameraNode("Camera Node", app.getCamera());
        playerNode = new Node("Player");

        app.getRootNode().attachChild(camNode);
        app.getRootNode().attachChild(playerNode);
    }
    
    public void initializeCamera() {      
        ChaseCamera chaseCam = new ChaseCamera(app.getCamera(), playerNode, app.getInputManager());
        camNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);      
        chaseCam.setDefaultDistance(0.001f);
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setSmoothMotion(false);
        chaseCam.setDragToRotate(false);
        chaseCam.setRotationSensitivity(2f);
        chaseCam.setMinVerticalRotation(25 * FastMath.DEG_TO_RAD);
        chaseCam.setMaxVerticalRotation(FastMath.DEG_TO_RAD * 75);
        camNode.addControl(chaseCam);
    }
    
    public void initializeMap() {
        terrainBuilder = new TerrainBuilder(app);
        terrainBuilder.buildMap();
    }
    
    public void initializePlayer() {        
        playerControl = new PlayerController(app);
        playerControl.setGravity(0.001f);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(playerControl);
        playerNode.addControl(playerControl);   
        playerControl.setPhysicsLocation(terrainBuilder.getSpawnPoint());
    }
    
    public void initializeItems() {
    }
    
}
