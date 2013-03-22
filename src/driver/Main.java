package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import sound.SoundController;
import states.GameState;
import states.MenuState;

public class Main extends SimpleApplication {

    private MenuState menuState;
    private GameState gameState;
    private SoundController soundController;

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
        menuState = new MenuState();
        gameState = new GameState();
        soundController = new SoundController(assetManager, rootNode);
        soundController.initAudio();
        soundController.playSound(SoundController.soundEvent.MUSIC_THEME);
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
