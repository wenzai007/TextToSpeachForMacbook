package main;

/**
 * Created by Knight on 15/10/5.
 */

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalKeyListenerExample implements NativeKeyListener {
    public  static TrySpeech  ts =null;

    public void nativeKeyPressed(NativeKeyEvent e) {
        //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
       // System.out.println("aaaa"+e.getKeyCode());  不要去记录按键了  太多了。。。
        ts.press(e);


        /*if (e.getKeyCode() == NativeKeyEvent.VC_C) {
            GlobalScreen.unregisterNativeHook();
        }*/
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
       // System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        ts.release(e);
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
       // System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    public static void process() {
        try {
            GlobalScreen.registerNativeHook();
            // Get the logger for "org.jnativehook" and set the level to off.
// Change the level for all handlers attached to the default logger.
            Handler[] handlers = Logger.getLogger("").getHandlers();
            for (int i = 0; i < handlers.length; i++) {
                handlers[i].setLevel(Level.OFF);
            }
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample());
    }

    public static void main(String[] args){
        ts =new TrySpeech();
        ts.startIt();
        process();
    }
}
