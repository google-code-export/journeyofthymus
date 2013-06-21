package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
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

/**
 * Journey of Thymus' entry class
 *
 * @author MIKUiqnw0, James
 * @version 0.03.48
 * @since 8/3/13
 * 
 */
public class ThymusApplication extends SimpleApplication implements ApplicationInterface, GameController {

    private MenuState menuState;
    private GameState gameState;
    private BulletAppState bulletAppState;
    private ScheduledThreadPoolExecutor executor;
    private Nifty nifty;  // Manages GUI elements
    private boolean debugShapesOn = false;
    private static final Logger logger = Logger.getLogger("ThymusApplication"); // logging interface for debugging

    /**
     * Entry driver during run time
     * @param args Retrieves input strings from commandline arguments when starting the application
     */
    public static void main(String[] args) {
        logger.log(Level.ALL, "Starting application");
        ThymusApplication app = new ThymusApplication();
        AppSettings newSettings = new AppSettings(true); // AppSettings used to customize loading options

        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); // Retrieves current system graphical settings, used for window width
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

        Display.setLocation(0, 0); // Sets window start position to the top left of the screen
    }

    /**
     * Initializes the menu state and displays the initial GUI using nifty elements.
     * @since 24/5/13
     */
    public void initializeGUI() {
        logger.log(Level.ALL, "Initialising GUI");
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

    /**
     * Overriden method from SimpleApplication, triggers after app.start().
     * @since 8/3/13
     */
    @Override
    public void simpleInitApp() {
        initializeGUI();
    }

    /**
     * Overriden destroy method, used to properly handle the thread executor.
     * @since 7/6/13
     */
    @Override
    public void destroy() {
        super.destroy();
        if(executor != null) {
            executor.shutdown();
        }
    }

    /**
     * Called after the player clicks on "Start Game" at the menu. Swaps states from
     * menu state to game state.
     * @param nextScreen Name of the screen that nifty uses to look up the next display.
     * @since 24/5/13
     */
    @Override
    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        inputManager.setCursorVisible(false);
        setPauseOnLostFocus(false);
        executor = new ScheduledThreadPoolExecutor(4); // Thread pool for multi-threading operations
        bulletAppState = new BulletAppState(); // Physics space for collidable objects
        gameState = new GameState(this);

        stateManager.attach(bulletAppState);
        stateManager.attach(gameState);
        stateManager.detach(menuState);
        flyCam.setEnabled(false);
        if(debugShapesOn) {
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        }
    }

    /**
     * Overriden method from ApplicationInterface, used to get the thread executor
     * from within the game application.
     * @return Instance of the ScheduledThreadPoolExecutor object
     * @since 17/5/13
     */
    @Override
    public ScheduledThreadPoolExecutor getExecutor() {
        return executor;
    }

    //Implementations from GameController

    /**
     * Pause function, triggered when pause can be applied (Interacting with a menu / keyboard key)
     * @since 24/5/13
     */
    @Override
    public void pause() {
    }

    /**
     * Score statistics function, triggered on overall objective completion. Also cleans up objects.
     * @since 24/5/13
     */
    @Override
    public void endGame() {
    }

    /**
     * Cleanup function, triggered when pressing exit on the menu.
     * @since 24/5/13
     */
    @Override
    public void quitGame() {
        stop();
    }

    /**
     * Enables / Disables collision debugging in the physics space.
     * @param toggle value to affect debug visibility (true / false)
     * @since 24/5/13
     */
    @Override
    public void toggleDebugShapes(boolean toggle) {
        debugShapesOn = toggle;
    }
}
