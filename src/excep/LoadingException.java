/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package excep;

/**
 *
 * @author mdm110-08
 */
public class LoadingException extends Exception {

    /**
     * Creates a new instance of
     * <code>LoadingException</code> without detail message.
     */
    public LoadingException() {
    }

    /**
     * Constructs an instance of
     * <code>LoadingException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public LoadingException(String msg) {
        super(msg);
    }
}
