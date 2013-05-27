package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.Logger;
import states.GameState;
import states.MenuState;

public class ThymusApplication extends SimpleApplication implements ApplicationInterface, GameController {

    private MenuState menuState;
    private GameState gameState;
    private BulletAppState bulletAppState;
    private Nifty nifty;

    public static void main(String[] args) {
        ThymusApplication app = new ThymusApplication();
        AppSettings newSettings = new AppSettings(true);
        newSettings.setWidth(1024);
        newSettings.setHeight(768);
        newSettings.setFrameRate(60);
        app.setSettings(newSettings);
        app.setShowSettings(false);
        app.start();
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
    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        inputManager.setCursorVisible(false);
        bulletAppState = new BulletAppState();
        gameState = new GameState(this);
        stateManager.attach(bulletAppState);
        stateManager.attach(gameState);
        stateManager.detach(menuState);
        flyCam.setMoveSpeed(30);
        //cam.setFrustumNear(0.3f);
        //cam.setFrustumFar(10f);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
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
    public Camera getCamera() {
        return cam;
    }

    @Override
    public void quitgame() {
        stop();
    }
}
