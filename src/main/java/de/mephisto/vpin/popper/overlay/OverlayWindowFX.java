package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.popper.overlay.util.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.configuration2.Configuration;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OverlayWindowFX extends Application implements NativeKeyListener {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OverlayGraphics.class);

  private KeyChecker keyChecker;

  private final File file = new File("./resources", "overlay.png");

  private boolean visible = false;

  private Stage stage;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.stage = primaryStage;
    Configuration overlayConfig = Config.getOverlayConfig();
    String hotkey = overlayConfig.getString("overlay.hotkey");
    keyChecker = new KeyChecker(hotkey);

    Platform.setImplicitExit(false);

    FileInputStream inputstream = new FileInputStream(file);
    Image image = new Image(inputstream);
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);

    Group root = new Group(imageView);
    Screen screen = Screen.getPrimary();
    final Scene scene = new Scene(root, screen.getVisualBounds().getWidth(), screen.getVisualBounds().getHeight(), true, SceneAntialiasing.BALANCED);

    Rectangle2D bounds = screen.getVisualBounds();
    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());

    stage.setScene(scene);
    stage.setFullScreenExitHint("");
    stage.setFullScreen(true);
    stage.setAlwaysOnTop(true);
    stage.setHeight(screen.getVisualBounds().getWidth());
    stage.setWidth(screen.getVisualBounds().getHeight());

    GlobalScreen.registerNativeHook();
    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    logger.setLevel(Level.OFF);
    logger.setUseParentHandlers(false);
    GlobalScreen.addNativeKeyListener(this);
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    if (keyChecker.matches(nativeKeyEvent)) {
      this.visible = !visible;
      Platform.runLater(() -> {
        if (this.visible) {
          stage.show();
        }
        else {
          stage.hide();
        }
      });
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

  }
}
