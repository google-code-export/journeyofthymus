/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

/**
 *
 * @author MDM110-09
 */
public interface GameController {

    void startGame(String nextScreen);
    void toggleDebugShapes(boolean toggle);
    void pause();
    void endGame();
    void quitGame();
}
