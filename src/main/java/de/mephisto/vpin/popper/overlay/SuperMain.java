package de.mephisto.vpin.popper.overlay;

public class SuperMain {
  public static void main(String[] args) throws Exception {
    if(args != null && args.length > 0 && args[0].contains("config")) {
      ConfigWindow.main(args);
    }
    else {
      OverlayWindowFX.launch(OverlayWindowFX.class);
    }
  }
}
