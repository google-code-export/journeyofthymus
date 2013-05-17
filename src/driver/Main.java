package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import gui.GUI;
import states.GameState;
import states.MenuState;

public class Main extends SimpleApplication implements ApplicationInterface {

    private GUI guiController;
    private MenuState menuState;
    private GameState gameState;
    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings newSettings = new AppSettings(true);
        newSettings.setWidth(1024);
        newSettings.setHeight(768);
        newSettings.setFrameRate(60);
        app.setSettings(newSettings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //guiController = new GUI(this);
        bulletAppState = new BulletAppState();
        menuState = new MenuState();
        gameState = new GameState(this);

        stateManager.attach(bulletAppState);
        //stateManager.attach(menuState);
        stateManager.attach(gameState);
        flyCam.setMoveSpeed(30);
        //cam.setFrustumNear(0.3f);
        //cam.setFrustumFar(10f);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
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
}
