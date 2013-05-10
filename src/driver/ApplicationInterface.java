package driver;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *
 * @author MIKUiqnw0
 * @since 22/03/2013
 * @version 0.00.01
 */
public interface ApplicationInterface {
    public Node getRootNode();
    public AssetManager getAssetManager();
    public InputManager getInputManager();
    public AppStateManager getStateManager();
    public Camera getCamera();
}
