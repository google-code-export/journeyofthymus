package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.lwjgl.opengl.Display;
import states.GameState;
import states.MenuState;

public class ThymusApplication extends SimpleApplication implements ApplicationInterface, GameController {

    private MenuState menuState;
    private GameState gameState;
    private BulletAppState bulletAppState;
    private ScheduledThreadPoolExecutor executor;
    private Nifty nifty;

    public static void main(String[] args) {
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

    public void initializeGUI() {
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
    
    @Override
    public void simpleInitApp() {
        initializeGUI();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void destroy() {
        super.destroy();
        executor.shutdown();
    }
    
    
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
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        guiNode.detachAllChildren();
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
}
