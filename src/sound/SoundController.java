package sound;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author James
 */
public class SoundController extends AbstractControl {

    private AssetManager assetManager;
    private Node rootNode;
    private AudioNode musicAmbient,
                        musicTheme;
    public enum soundEvent{MUSIC_AMBIENT,
                            MUSIC_THEME};
    
    
    public SoundController(AssetManager assetManager, Node rootNode)
    {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void playSound(soundEvent event)
    {
        switch(event)
        {
            case MUSIC_AMBIENT:
                musicAmbient.play();
                break;
            case MUSIC_THEME:
                musicTheme.play();
                break;
            default:
                break;
        }
    }
    
    public void stopSound(soundEvent event)
    {
        switch(event)
        {
            case MUSIC_AMBIENT:
                musicAmbient.stop();
                break;
            case MUSIC_THEME:
                musicTheme.stop();
                break;
            default:
                break;
        }
    }
    
    public void initAudio()
    {
        //Looping ambient music
        musicAmbient = new AudioNode(assetManager, "Sounds/ThymusLabrynth.ogg", false);
        musicAmbient.setLooping(true);
        musicAmbient.setVolume(1);
        rootNode.attachChild(musicAmbient);
        
        //Theme music used for menu
        musicTheme = new AudioNode(assetManager, "Sounds/ThymusTheme.ogg", false);
        musicTheme.setLooping(true);
        musicTheme.setVolume(1);
        rootNode.attachChild(musicTheme);
    }
}
