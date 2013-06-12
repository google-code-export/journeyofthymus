package states;

import ai.BansheeController;
import ai.Pathfinder;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
    private TerrainBuilder terrainBuilder;
    private CameraNode camNode;
    private Node playerNode, lightNode, mapNode, itemNode, waypointNode;
    BitmapText text;

    public GameState(ApplicationInterface app) {
        this.app = app;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initializeHUD();        
        initializeNodes();
        initializeMap();
        initializeCamera();
        initializePlayer();
        initializeAI();
        initializeItems();
        initializeLighting();
        initializePostProcessing();
    }

    @Override
    public void update(float tpf) {
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initializeHUD() {
        BitmapFont guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText debug = new BitmapText(guiFont, false);
        debug.setName("debugline");
        debug.setSize(guiFont.getCharSet().getRenderedSize());
        debug.setLocalTranslation(300, debug.getLineHeight(), 0);
        app.getGuiNode().attachChild(debug);
        text = (BitmapText) this.app.getGuiNode().getChild("debugline");
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initializeNodes() {
        camNode = new CameraNode("Camera Node", app.getCamera());
        playerNode = new Node("Player");
        lightNode = new Node("Light Node");
        mapNode = new Node("Map Node");
        itemNode = new Node("Item Node");
        waypointNode = new Node("Waypoint Node");

        lightNode.attachChild(playerNode);  
        lightNode.attachChild(camNode);
        lightNode.attachChild(itemNode);
        lightNode.attachChild(waypointNode);
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
        app.getInputManager().deleteMapping("ChaseCamZoomIn");
        app.getInputManager().deleteMapping("ChaseCamZoomOut");
        camNode.addControl(chaseCam);
    }

    private void initializePlayer() {
        PlayerController playerControl = new PlayerController(app, camNode);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(playerControl);
        playerNode.addControl(playerControl);
        playerControl.setPhysicsLocation(terrainBuilder.getSpawnPoint());

        TorchController torchControl = new TorchController(playerControl, lightNode, app);
        Geometry torch = new Geometry("Torch", new Box(.05f, .3f, .05f));
        Material torchMat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        torchMat.setBoolean("UseMaterialColors",true);
        torchMat.setColor("Diffuse", ColorRGBA.White);
        torchMat.setColor("Ambient", ColorRGBA.White);
        torchMat.setColor("Specular", ColorRGBA.White); 
        torch.setMaterial(torchMat);
        torch.addControl(torchControl);
        camNode.attachChild(torch);
        torch.rotate(0.4f, 0.7f, 0f);
        torch.setLocalTranslation(-.35f, -.2f, 1.29f);
    }

    private void initializeAI() {
        Pathfinder waypointGen = new Pathfinder(app);
        waypointGen.buildGraph();
        Node bansheeNode = (Node) app.getAssetManager().loadModel("Models/Oto/Oto.mesh.xml");
        lightNode.attachChild(bansheeNode);
        BansheeController bansheeControl = new BansheeController(playerNode.getControl(PlayerController.class), bansheeNode);
        bansheeNode.addControl(bansheeControl);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(bansheeControl);               
        bansheeNode.setLocalScale(0.3f);
        bansheeNode.move(0,1,0);
        bansheeControl.setPhysicsLocation(new Vector3f(12.112f, 0.3f, 7.989f));
    }
/*    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void testRagdolls() {
                Node shoulders = createLimb(0.1f, 0.2f, new Vector3f(0.00f, 1.5f, 0), true);
        Node uArmL = createLimb(0.1f, 0.2f, new Vector3f(-0.75f, 0.8f, 0), false);
        Node uArmR = createLimb(0.1f, 0.2f, new Vector3f(0.75f, 0.8f, 0), false);
        Node lArmL = createLimb(0.1f, 0.2f, new Vector3f(-0.75f, -0.2f, 0), false);
        Node lArmR = createLimb(0.1f, 0.2f, new Vector3f(0.75f, -0.2f, 0), false);
        Node body = createLimb(0.1f, 0.2f, new Vector3f(0.00f, 0.5f, 0), false);
        Node hips = createLimb(0.1f, 0.2f, new Vector3f(0.00f, -0.5f, 0), true);
        Node uLegL = createLimb(0.1f, 0.2f, new Vector3f(-0.25f, -1.2f, 0), false);
        Node uLegR = createLimb(0.1f, 0.2f, new Vector3f(0.25f, -1.2f, 0), false);
        Node lLegL = createLimb(0.1f, 0.2f, new Vector3f(-0.25f, -2.2f, 0), false);
        Node lLegR = createLimb(0.1f, 0.2f, new Vector3f(0.25f, -2.2f, 0), false);
        
        join(body,  shoulders, new Vector3f( 0.00f,  1.4f, 0));
        join(body,       hips, new Vector3f( 0.00f, -0.5f, 0));
        join(uArmL, shoulders, new Vector3f(-0.75f,  1.4f, 0));
        join(uArmR, shoulders, new Vector3f( 0.75f,  1.4f, 0));
        join(uArmL,     lArmL, new Vector3f(-0.75f,  0.4f, 0));
        join(uArmR,     lArmR, new Vector3f( 0.75f,  0.4f, 0));
        join(uLegL,      hips, new Vector3f(-0.25f, -0.5f, 0));
        join(uLegR,      hips, new Vector3f( 0.25f, -0.5f, 0));
        join(uLegL,     lLegL, new Vector3f(-0.25f, -1.7f, 0));
        join(uLegR,     lLegR, new Vector3f( 0.25f, -1.7f, 0));
        Node ragDoll = new Node("ragdoll");
        ragDoll.attachChild(shoulders);
        ragDoll.attachChild(body);
        ragDoll.attachChild(hips);
        ragDoll.attachChild(uArmL);
        ragDoll.attachChild(uArmR);
        ragDoll.attachChild(lArmL);
        ragDoll.attachChild(lArmR);
        ragDoll.attachChild(uLegL);
        ragDoll.attachChild(uLegR);
        ragDoll.attachChild(lLegL);
        ragDoll.attachChild(lLegR);
        app.getRootNode().attachChild(ragDoll);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().addAll(ragDoll);    
    }

    private Node createLimb(float width, float height, Vector3f location, boolean rotate) {
        int axis = rotate ? PhysicsSpace.AXIS_X : PhysicsSpace.AXIS_Y;
        CapsuleCollisionShape shape = new CapsuleCollisionShape(width, height, axis);
        Node node = new Node("Limb");
        RigidBodyControl rigidBodyControl = new RigidBodyControl(shape, 1);
        node.setLocalTranslation(location);
        node.addControl(rigidBodyControl);
        return node;
    }
   
    private PhysicsJoint join(Node A, Node B, Vector3f connectionPoint) {
        Vector3f pivotA = A.worldToLocal(connectionPoint, new Vector3f());
        Vector3f pivotB = B.worldToLocal(connectionPoint, new Vector3f());
        ConeJoint joint = new ConeJoint(A.getControl(RigidBodyControl.class),
                                        B.getControl(RigidBodyControl.class),
                                        pivotA, pivotB);
        joint.setLimit(1f, 1f, 0);
        return joint;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
*/
    private void initializeItems() {
        Spatial healthpot = app.getAssetManager().loadModel("Models/health_flask/health_flask.obj");
        healthpot.setLocalScale(0.15f);
        healthpot.setLocalTranslation(0, -.27f, 0);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Models/health_flask/health.jpg"));
        healthpot.setMaterial(mat);

        //////// OIL ////////
        //CompoundCollisionShape compound = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(0.25f, 0.27f, 0.25f));
        //compound.addChildShape(new BoxCollisionShape(new Vector3f(0.25f, 0.27f, 0.25f)), new Vector3f(0, 0.27f, 0));
        ItemController itemControl = new ItemController(box, 0.3f, app, ItemType.OIL);
        itemNode.addControl(itemControl);
        itemNode.attachChild(healthpot);
        healthpot.setShadowMode(ShadowMode.Cast);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(itemControl);
        itemControl.setPhysicsLocation(new Vector3f(8.015633f, 1.15746358f, 59.78405f));
        //////// OIL ////////
    }

    private void initializeLighting() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(1f));
        lightNode.addLight(ambient);
    }

    private void initializePostProcessing() {
        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        app.getViewPort().addProcessor(fpp);

        //SSAOFilter ssaoFilter = new SSAOFilter(5.1f, 1.2f, 0.2f, 0.1f);
        //fpp.addFilter(ssaoFilter);

        FXAAFilter fxaaFilter = new FXAAFilter();
        fxaaFilter.setReduceMul(0.0f);
        fxaaFilter.setSubPixelShift(0.0f);
        fpp.addFilter(fxaaFilter);
    }
}
