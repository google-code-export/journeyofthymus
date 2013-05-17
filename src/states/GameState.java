package states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.input.ChaseCamera;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import driver.ApplicationInterface;
import generators.TerrainBuilder;
import items.ItemController;
import items.ItemType;
import items.TorchController;
import player.PlayerController;

    
/**
 *
 * @author MIKUiqnw0
 * @since 21/03/2013
 * @version 0.00.01
 */
public class GameState extends AbstractAppState {

    private ApplicationInterface app;
    private PhysicsSpace physicsSpace;
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
        physicsSpace = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
        initializeNodes();
        initializeMap();    
        initializeCamera();
        initializePlayer();
        initializeItems();
        initializeLighting();
        initializePostProcessing();
    }
    
    @Override
    public void update(float tpf) {
    }
    
    private void initializeNodes() {
        camNode = new CameraNode("Camera Node", app.getCamera());
        playerNode = new Node("Player");
        lightNode = new Node("Light Node");
        mapNode = new Node("Map Node");

        app.getRootNode().setShadowMode(ShadowMode.Off);
        lightNode.setShadowMode(ShadowMode.Off);
        mapNode.setShadowMode(ShadowMode.Off);

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
        PlayerController playerControl = new PlayerController(app, camNode);
        playerControl.setGravity(0.001f);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(playerControl);
        playerNode.addControl(playerControl);
        playerControl.setPhysicsLocation(terrainBuilder.getSpawnPoint());
        playerControl.enableSprint();
                       
        TorchController torchControl = new TorchController(playerControl, lightNode, app.getCamera());
        Geometry torch = new Geometry("Torch", new Box(.05f, .3f, .05f));
        Material torchMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        torchMat.setColor("Color", ColorRGBA.White);
        torch.setMaterial(torchMat);
        torch.addControl(torchControl);
        camNode.attachChild(torch);
        torch.rotate(0.4f, 0.7f, 0f);
        torch.setLocalTranslation(-.35f, -.2f, 1.29f);
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
        ItemController itemControl = new ItemController(box, 0.3f, app.getStateManager().getState(BulletAppState.class).getPhysicsSpace(), ItemType.OIL);
        boxgeom.addControl(itemControl);
        boxgeom.setShadowMode(ShadowMode.Off);
        lightNode.attachChild(boxgeom);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(itemControl);
        itemControl.setPhysicsLocation(new Vector3f(8.015633f, 1.15746358f, 59.78405f));

    }
    
    private void initializeLighting() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.07f));
        lightNode.addLight(ambient);
    }
    
    private void initializePostProcessing() {
         FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
         app.getViewPort().addProcessor(fpp);
         
        SSAOFilter ssaoFilter = new SSAOFilter(5.1f, 1.2f, 0.2f, 0.1f);
        fpp.addFilter(ssaoFilter);
        
        FXAAFilter fxaaFilter = new FXAAFilter();
        fxaaFilter.setReduceMul(0.0f);
        fxaaFilter.setSubPixelShift(0.0f);
        fpp.addFilter(fxaaFilter);
    }

}
