package driver;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import generators.TerrainBuilder;
import generators.Tile;
import sound.SoundController;
import states.GameState;
import states.MenuState;

public class Main extends SimpleApplication {

    private MenuState menuState;
    private GameState gameState;
    private SoundController soundController;
    private TerrainBuilder terrainBuilder;
    private Tile[][] map;

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
        
// Used for testing MapFileReader and TerrainBuilder
//        terrainBuilder = new TerrainBuilder(assetManager, rootNode);
//        map = terrainBuilder.getMap("E:/TAFE/Programming/JMonkey/journeyofthymus/assets/MapFiles/Labyrinth1.txt");
//        for (int i = 0; i < 8; i++) {
//            System.out.println(map[0][i].code + map[1][i].code + map[2][i].code + map[3][i].code + map[4][i].code + map[5][i].code + map[6][i].code + map[7][i].code);
//        }
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
