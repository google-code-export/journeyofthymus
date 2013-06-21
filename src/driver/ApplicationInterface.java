package driver;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author MIKUiqnw0
 * @since 22/03/2013
 * @version 0.00.01
 */
public interface ApplicationInterface {
    /**
     *
     * @return
     */
    public Node getRootNode();
    /**
     *
     * @return
     */
    public Node getGuiNode();
    /**
     *
     * @return
     */
    public AssetManager getAssetManager();
    /**
     *
     * @return
     */
    public InputManager getInputManager();
    /**
     *
     * @return
     */
    public AppStateManager getStateManager();
    /**
     *
     * @return
     */
    public ViewPort getViewPort();
    /**
     *
     * @return
     */
    public Camera getCamera();
    /**
     *
     * @return
     */
    public ScheduledThreadPoolExecutor getExecutor();
}
