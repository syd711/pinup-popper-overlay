package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.popper.overlay.util.Config;
import org.apache.commons.configuration2.Configuration;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OverlayWindow extends JFrame implements NativeKeyListener {

  private final KeyChecker keyChecker;
  private final File file = new File("./resources", "overlay.png");

  private boolean visible = false;


  public OverlayWindow() throws Exception {
    Configuration overlayConfig = Config.getOverlayConfig();
    String hotkey = overlayConfig.getString("overlay.hotkey");
    keyChecker = new KeyChecker(hotkey);

    UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName());

    this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(0, 0);
    setUndecorated(true);
    setAlwaysOnTop(true);

    // no layout manager
    setLayout(new BorderLayout());
    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
    this.add(new JLabel(icon), BorderLayout.CENTER);

    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    logger.setLevel(Level.OFF);
    logger.setUseParentHandlers(false);

    GlobalScreen.registerNativeHook();
    GlobalScreen.addNativeKeyListener(this);
  }

  public static void main(String[] args) throws Exception {
    new OverlayWindow();
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    if (keyChecker.matches(nativeKeyEvent)) {
      this.visible = !visible;
      this.setVisible(this.visible);
      EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
          toFront();
        }
      });
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

  }
}
