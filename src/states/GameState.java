package states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.PssmShadowRenderer;
import driver.ApplicationInterface;
import generators.TerrainBuilder;
import items.ItemController;
import items.ItemType;
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
    private Node playerNode, lightNode, mapNode;
    private PointLight pointl;

    public GameState(ApplicationInterface app) {
        this.app = app;
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initializeNodes();
        initializeMap();    
        initializeCamera();
        initializePlayer();
        initializeItems();
        initializeLighting();
        initializeShadows();
        testLight();
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        playerNode.setLocalTranslation(app.getCamera().getLocation());
        pointl.setPosition(playerNode.getLocalTranslation());
    }
    
    private void initializeNodes() {
        camNode = new CameraNode("Camera Node", app.getCamera());
        playerNode = new Node("Player");
        lightNode = new Node("Light Node");
        mapNode = new Node("Map Node");

        //app.getRootNode().setShadowMode(ShadowMode.Off);

        app.getRootNode().attachChild(camNode);
        app.getRootNode().attachChild(playerNode);
        app.getRootNode().attachChild(lightNode);
    }
    
    private void initializeMap() {
        terrainBuilder = new TerrainBuilder(app, mapNode);
        terrainBuilder.buildMap();
    }
    
    private void initializeCamera() {      
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
    
    private void initializePlayer() {        
        playerControl = new PlayerController(app);
        playerControl.setGravity(0.001f);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(playerControl);
        playerNode.addControl(playerControl);
        playerControl.setPhysicsLocation(terrainBuilder.getSpawnPoint());
        playerControl.enableSprint();
    }
    
    private void initializeItems() {
        Geometry boxgeom = new Geometry("Item", new Box(0.2f, 0.2f, 0.2f));
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors",true);
        ColorRGBA color = ColorRGBA.randomColor();
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color);
        boxgeom.setMaterial(mat);
        
        CollisionShape box = new BoxCollisionShape(new Vector3f(0.2f, 0.2f, 0.2f));
        ItemController itemControl = new ItemController(box, 0.3f, app.getStateManager().getState(BulletAppState.class).getPhysicsSpace(), ItemType.TINDER);
        boxgeom.addControl(itemControl);
        lightNode.attachChild(boxgeom);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(itemControl);
        itemControl.setPhysicsLocation(new Vector3f(8.015633f, 1.15746358f, 59.78405f));
        
    }
    
    private void initializeLighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.3f, -.3f, -.2f).normalizeLocal());
        app.getRootNode().addLight(sun);
    }
    
    private void initializeShadows() {
        PssmShadowRenderer shadow = new PssmShadowRenderer(app.getAssetManager(), 2056, 5);
        shadow.setDirection(new Vector3f(-.3f, -.3f, -.2f).normalizeLocal());
        shadow.setFilterMode(PssmShadowRenderer.FilterMode.PCF8);
        shadow.setShadowIntensity(0.4f);
        app.getViewPort().addProcessor(shadow);
    }
    
    private void testLight() {
        pointl = new PointLight();
        pointl.setColor(ColorRGBA.White);
        pointl.setRadius(7f);
        lightNode.addLight(pointl);
    }
}
