package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication {

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
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
