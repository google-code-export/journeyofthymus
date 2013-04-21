package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
 
/**
 * Example 9 - How to make walls and floors solid.
 * This collision code uses Physics and a custom Action Listener.
 * @author normen, with edits by Zathras
 */
public class Main extends SimpleApplication implements ActionListener,ScreenController 
{ 
  private Spatial sceneModel;
  private BulletAppState bulletAppState;
  private RigidBodyControl landscape;
  private CharacterControl player;
  private Vector3f walkDirection = new Vector3f();
  private boolean left = false, right = false, up = false, down = false;
  //Nifty GUI
  private HudMap myMap; 
  private Screen myScreen;
  private Nifty nifty;
 
  public static void main(String[] args) {
    Main app = new Main();
    app.setPauseOnLostFocus(false);
    app.start();
  }
  
 @Override
  public void simpleInitApp() {
    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                     inputManager,
                                                      audioRenderer,
                                                      guiViewPort);
    nifty = niftyDisplay.getNifty();
    nifty.fromXml("Interface/map.xml", "map", this);
    guiViewPort.addProcessor(niftyDisplay);
    myMap = new HudMap(nifty); 
    myMap.GoDownBy(5);
    myMap.MovePlayerOn(100, 100);
    guiNode.detachAllChildren();
    /** Set up Physics */
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
 
    // We re-use the flyby camera for rotation, while positioning is handled by physics
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    flyCam.setMoveSpeed(100);
    setUpKeys();
    setUpLight();
 
    // We load the scene from the zip file and adjust its size.
    assetManager.registerLocator("town.zip", ZipLocator.class);
    sceneModel = assetManager.loadModel("main.scene");
    sceneModel.setLocalScale(2f);
 
    // We set up collision detection for the scene by creating a
    // compound collision shape and a static RigidBodyControl with mass zero.
    CollisionShape sceneShape =
            CollisionShapeFactory.createMeshShape((Node) sceneModel);
    landscape = new RigidBodyControl(sceneShape, 0);
    sceneModel.addControl(landscape);
 
    // We set up collision detection for the player by creating
    // a capsule collision shape and a CharacterControl.
    // The CharacterControl offers extra settings for
    // size, stepheight, jumping, falling, and gravity.
    // We also put the player in its starting position.
    CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
    player = new CharacterControl(capsuleShape, 0.05f);
    player.setJumpSpeed(20);
    player.setFallSpeed(30);
    player.setGravity(30);
    player.setPhysicsLocation(new Vector3f(0, 10, 0));
 
    // We attach the scene and the player to the rootNode and the physics space,
    // to make them appear in the game world.
    rootNode.attachChild(sceneModel);
    bulletAppState.getPhysicsSpace().add(landscape);
    bulletAppState.getPhysicsSpace().add(player);
  
    

}
 
  private void setUpLight() {
    // We add light so we see the scene
    AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(1.3f));
    rootNode.addLight(al);
 
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
    rootNode.addLight(dl);
  }
 
  /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
  private void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
  }
 
  /** These are our custom actions triggered by key presses.
   * We do not walk yet, we just keep track of the direction the user pressed. */
  public void onAction(String binding, boolean value, float tpf) {
    if (binding.equals("Left")) 
    {
      left = value;
      myMap.GoLeftBy(2);
    } 
    else if (binding.equals("Right")) 
    {
      right = value;
      myMap.GoRightBy(2);
    } 
    else if (binding.equals("Up")) 
    {
      up = value;
      myMap.GoUpBy(2);
    } 
    else if (binding.equals("Down")) 
    {
      down = value;
      myMap.GoDownBy(2);
    } 
    else if (binding.equals("Jump")) 
    {
      player.jump();
    }
  }
 
  /**
   * This is the main event loop--walking happens here.
   * We check in which direction the player is walking by interpreting
   * the camera direction forward (camDir) and to the side (camLeft).
   * The setWalkDirection() command is what lets a physics-controlled player walk.
   * We also make sure here that the camera moves with player.
   */
  @Override
  public void simpleUpdate(float tpf) {
    Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
    Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
    walkDirection.set(0, 0, 0);
    if (left)  
    { 
        walkDirection.addLocal(camLeft); 
        myMap.GoLeftBy(2);
    }
    if (right) 
    { 
        walkDirection.addLocal(camLeft.negate()); 
        myMap.GoRightBy(2);
    }
    if (up)    
    { 
        walkDirection.addLocal(camDir); 
    }
    if (down)  
    { 
        walkDirection.addLocal(camDir.negate()); 
    }
    player.setWalkDirection(walkDirection);
    cam.setLocation(player.getPhysicsLocation());
  }

    public void bind(Nifty nifty, Screen screen) {
        //this.nifty = nifty;
        //myScreen = screen;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onStartScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
