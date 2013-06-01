package items;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapText;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import driver.ApplicationInterface;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import player.PlayerController;

/**
 *
 * @author MIKUiqnw0
 */
public class TorchController extends AbstractControl {

    private float oilTimer, tinderTimer, initialLevel,
            targetLevel, lightTimer, particleTimer,
            timeToUpdate;
    private boolean statusOff;
    private static final float TINDER_TIMER_MAX = 480,
            OIL_TIMER_MAX = 600,
            LIFE_LOW = 180;
    private AssetManager assetManager;
    private PlayerController player;
    private PointLight light;
    private ParticleEmitter fire;
    private Node lightNode, guiNode;
    private Camera cam;
    private float g_tpf;
    private ScheduledThreadPoolExecutor executor;
    private Future future;

    public TorchController(PlayerController player, Node lightNode, ApplicationInterface app) {
        oilTimer = OIL_TIMER_MAX;
        tinderTimer = TINDER_TIMER_MAX;
        this.player = player;
        this.lightNode = lightNode;
        assetManager = app.getAssetManager();
        guiNode = app.getGuiNode();
        cam = app.getCamera();
        executor = app.getExecutor();
        createLight();
        createParticles();
    }

    public float getOilTimer() {
        return oilTimer;
    }

    public float getTinderTimer() {
        return tinderTimer;
    }

    public static float getMaxOilTime() {
        return OIL_TIMER_MAX;
    }

    public static float getMaxTinderTime() {
        return TINDER_TIMER_MAX;
    }

    public void setOilTimer(float modifier) {
        oilTimer += (oilTimer + modifier > OIL_TIMER_MAX) ? OIL_TIMER_MAX - oilTimer : modifier;
    }

    public void setTinderTimer(float modifier) {
        tinderTimer += (tinderTimer + modifier > TINDER_TIMER_MAX) ? TINDER_TIMER_MAX - tinderTimer : modifier;
    }

    public void useOil() {
        if (player.isPositiveInventory(ItemType.OIL)) {
            if (oilTimer < OIL_TIMER_MAX) {
                setOilTimer(Oil.getExtensionTime());
                player.setOilCount(-1);
            }
        }
    }

    public void useTinder() {
        if (player.isPositiveInventory(ItemType.TINDER)) {
            if (tinderTimer < TINDER_TIMER_MAX) {
                setTinderTimer(Tinder.getExtensionTime());
                player.setTinderCount(-1);
            }
        }
    }

    private void createLight() {
        light = new PointLight();
        light.setColor(ColorRGBA.Orange.mult(0.9f));
        targetLevel = highFlicker();
        light.setRadius(targetLevel);
        lightNode.addLight(light);
    }

    private void createParticles() {
        fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 15);
        // temp textures
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.6f, 0));
        fire.setStartSize(0.3f);
        fire.setEndSize(0.01f);
        fire.setGravity(0f, 0f, 0f);
        fire.setLowLife(0.5f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        Node camNode = (Node) lightNode.getChild("Camera Node");
        camNode.attachChild(fire);
        fire.setLocalTranslation(-.29f, .12f, 1.29f);

    }

    private float highFlicker() {
        return ((float) Math.random() * 2) + 11;
    }

    private float lowFlicker() {
        return ((float) Math.random() * 2) + 7;
    }

    @Override
    protected void controlUpdate(float tpf) {
        // Position light at the camera's location
        light.setPosition(cam.getLocation());
        // Update the torch's fuel timers
        if (oilTimer > 0) {
            oilTimer -= tpf;
        }
        if (tinderTimer > 0) {
            tinderTimer -= tpf;
        }
        g_tpf = tpf;

        if (future == null) {
            future = executor.submit(updateLight);
        } else if (future != null) {
            if (future.isDone()) {
                future = null;
            } else if (future.isCancelled()) {
                future = null;
            }
        }
    }
    
    private Callable updateLight = new Callable() {
        @Override
        public Object call() throws Exception {
            // Adjust torch lighting based on fuel properties
            if (oilTimer <= 0 || tinderTimer <= 0) {
                if (light.getRadius() > 0.001f) {
                    if (targetLevel != 0.001f) {
                        statusOff = true;
                        targetLevel = 0.001f;
                        initialLevel = light.getRadius();
                        lightTimer = 10.0f;
                        timeToUpdate = lightTimer;
                    }

                    if (particleTimer <= 0 && lightTimer >= 0) {
                            fire.setParticlesPerSec(lightTimer);
                            particleTimer = 1;
                    }

                    light.setRadius(FastMath.interpolateLinear(lightTimer / timeToUpdate, targetLevel, initialLevel));
                    lightTimer -= g_tpf;
                    particleTimer -= g_tpf;

                    BitmapText text = (BitmapText) guiNode.getChild("debugline");
                    text.setText("Current Radius: " + light.getRadius() + " | Timer: " + lightTimer + " | True 0.001: " + (light.getRadius() == 0.001f));
                } else if (fire.getParticlesPerSec() != 0) {
                    fire.setParticlesPerSec(0);
                } else if (fire.getNumVisibleParticles() == 0) {
                    fire.setEnabled(false);
                }
            } else {
                if (lightTimer <= 0 || statusOff) {
                    statusOff = false;
                    lightTimer = 0.15f;
                    initialLevel = light.getRadius();
                    timeToUpdate = lightTimer;
                    if (oilTimer < LIFE_LOW || tinderTimer < LIFE_LOW) {
                        targetLevel = lowFlicker();
                    } else {
                        targetLevel = highFlicker();
                    }
                    if (!fire.isEnabled()) {
                        fire.setEnabled(true);
                    }
                    fire.setParticlesPerSec(targetLevel);
                }

                lightTimer -= g_tpf;
                light.setRadius(FastMath.interpolateLinear(lightTimer / timeToUpdate, targetLevel, initialLevel));
            }
            return null;
        }
    };

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }
}
