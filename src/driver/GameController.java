/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

/**
 *
 * @author James
 */
public interface GameController {

    /**
     *
     * @param nextScreen
     */
    void startGame(String nextScreen);
    /**
     *
     * @param toggle
     */
    void toggleDebugShapes(boolean toggle);
    /**
     *
     */
    void pause();
    /**
     *
     */
    void endGame();
    /**
     *
     */
    void quitGame();
}
