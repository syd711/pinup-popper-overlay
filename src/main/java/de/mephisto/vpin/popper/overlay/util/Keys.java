package de.mephisto.vpin.popper.overlay.util;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Keys {

  public static int[] KEY_CODES = {KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4, KeyEvent.VK_F5,
      KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9, KeyEvent.VK_F10, KeyEvent.VK_F11, KeyEvent.VK_F12,
      KeyEvent.VK_A,
      KeyEvent.VK_B,
      KeyEvent.VK_C,
      KeyEvent.VK_D,
      KeyEvent.VK_E,
      KeyEvent.VK_F,
      KeyEvent.VK_G,
      KeyEvent.VK_H,
      KeyEvent.VK_I,
      KeyEvent.VK_J,
      KeyEvent.VK_K,
      KeyEvent.VK_L,
      KeyEvent.VK_M,
      KeyEvent.VK_N,
      KeyEvent.VK_O,
      KeyEvent.VK_P,
      KeyEvent.VK_Q,
      KeyEvent.VK_R,
      KeyEvent.VK_S,
      KeyEvent.VK_T,
      KeyEvent.VK_U,
      KeyEvent.VK_V,
      KeyEvent.VK_W,
      KeyEvent.VK_X,
      KeyEvent.VK_Y,
      KeyEvent.VK_Z,
      KeyEvent.VK_0,
      KeyEvent.VK_1,
      KeyEvent.VK_2,
      KeyEvent.VK_3,
      KeyEvent.VK_4,
      KeyEvent.VK_5,
      KeyEvent.VK_6,
      KeyEvent.VK_7,
      KeyEvent.VK_8,
      KeyEvent.VK_9,
      KeyEvent.VK_PLUS,
      KeyEvent.VK_MINUS,
      KeyEvent.VK_HOME,
      KeyEvent.VK_END,
      KeyEvent.VK_DELETE,
      KeyEvent.VK_INSERT,
      KeyEvent.VK_PAGE_DOWN,
      KeyEvent.VK_PAGE_UP,
  };

  public static List<String> getKeyNames() {
    List<String> result = new ArrayList<>();
    for (int keyCode : KEY_CODES) {
      result.add(getKeyDisplayName(keyCode));
    }
    return result;
  }

  private static String getKeyDisplayName(int code) {
    return KeyEvent.getKeyText(code);
  }
}
