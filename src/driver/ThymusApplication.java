package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import states.GameState;
import states.MenuState;

/*
 * Entry class to start the game.
 *  
 */
public class ThymusApplication extends SimpleApplication implements ApplicationInterface, GameController {

    private MenuState menuState;
    private GameState gameState;
    private BulletAppState bulletAppState;
    private ScheduledThreadPoolExecutor executor;
    private Nifty nifty;
    private boolean debugShapesOn = false;
    
    private static Logger log = Logger.getLogger("ThymusApplication");

    /*
     * Entry driver during run time
     * @params args
     */
    public static void main(String[] args) {
        log.log(Level.ALL, "Starting application");
        ThymusApplication app = new ThymusApplication();
        AppSettings newSettings = new AppSettings(true);

        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        DisplayMode[] modes = device.getDisplayModes();
//        int i=0; // note: there are usually several, let's pick the first
        newSettings.setResolution(device.getDisplayMode().getWidth(), 768);
        newSettings.setFrequency(60);
        newSettings.setDepthBits(24);
//        newSettings.setFullscreen(device.isFullScreenSupported());
        newSettings.setVSync(true);
        newSettings.setTitle("Journey of Thymus alpha");
        app.setSettings(newSettings);
        app.setShowSettings(false);

        app.start();

        Display.setLocation(0, 0);
    }

    /*
     * Initializes and displays the menu state and nifty elements
     */
    public void initializeGUI() {
        log.log(Level.ALL, "Initialising GUI");
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        menuState = new MenuState(this);
        nifty.fromXml("Interfaces/menu.xml", "start", menuState);
        stateManager.attach(menuState);
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);
    }

    /*
     * Inherited method from SimpleApplication, triggers after app.start()
     */
    @Override
    public void simpleInitApp() {
        initializeGUI();
    }

    /*
     * Inherited method from SimpleApplication, main update loop, unused.
     */
    @Override
    public void simpleUpdate(float tpf) {
    }

    /*
     * Inherited method from SimpleApplication, allows for custom render methods, unused.
     */
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /*
     * Overriden destroy method, used to properly manage the thread executor
     */
    @Override
    public void destroy() {
        super.destroy();
        executor.shutdown();
    }

    /*
     * Returns camera
     */
    @Override
    public Camera getCamera() {
        return cam;
    }

    @Override
    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        inputManager.setCursorVisible(false);
        setPauseOnLostFocus(false);
        executor = new ScheduledThreadPoolExecutor(4);
        bulletAppState = new BulletAppState();
        gameState = new GameState(this);

        stateManager.attach(bulletAppState);
        stateManager.attach(gameState);
        stateManager.detach(menuState);
        flyCam.setEnabled(false);
        if(debugShapesOn) {
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        }
    }

    @Override
    public ScheduledThreadPoolExecutor getExecutor() {
        return executor;
    }

    @Override
    public void pause() {
    }

    @Override
    public void endGame() {
    }

    @Override
    public void quitGame() {
        stop();
    }

    @Override
    public void toggleDebugShapes(boolean toggle) {
        debugShapesOn = toggle;
    }
}
