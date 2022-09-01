package de.mephisto.vpin.popper.overlay;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Checks the native key event, e.g.
 * NATIVE_KEY_PRESSED,keyCode=46,keyText=C,keyChar=Undefiniert,modifiers=Strg,keyLocation=KEY_LOCATION_STANDARD,rawCode=67
 * for Ctrl+C
 */
public class KeyChecker {

  private int modifier;
  private String letter = null;

  public KeyChecker(String hotkey) {
    if (hotkey.contains("+")) {
      this.modifier = Integer.parseInt(hotkey.split("\\+")[0]);
      this.letter = hotkey.split("\\+")[1];
    }
    else {
      this.letter = hotkey;
    }
  }

  public boolean matches(NativeKeyEvent event) {
    String keyText = NativeKeyEvent.getKeyText(event.getKeyCode());
    String modifiers = NativeKeyEvent.getKeyText(event.getModifiers());
    System.out.println("Modifier: " + event.getModifiers());
    System.out.println(String.valueOf(keyText).equalsIgnoreCase(this.letter));
    if(keyText.equalsIgnoreCase(this.letter) && this.modifier == event.getModifiers()) {
      return true;
    }

    return false;
  }
}
