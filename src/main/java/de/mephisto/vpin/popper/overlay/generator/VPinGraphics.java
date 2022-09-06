package de.mephisto.vpin.popper.overlay.generator;

import de.mephisto.vpin.popper.overlay.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VPinGraphics {
  private final static Logger LOG = LoggerFactory.getLogger(VPinGraphics.class);

  /**
   * Enables the anti aliasing for fonts
   */
  static void setRendingHints(BufferedImage image) {
    Graphics g = image.getGraphics();
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  }

  static void registerFonts(java.util.List<Font> fonts) throws Exception {
    try {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      for (Font font : fonts) {
        ge.registerFont(font);
      }
    } catch (Exception e) {
      LOG.error("Failed to register fonts: " + e.getMessage(), e);
      throw e;
    }
  }

  static void setDefaultColor(BufferedImage image, String fontColor) {
    Graphics g = image.getGraphics();
    g.setColor(Color.decode(fontColor));
  }

  static void applyAlphaComposites(BufferedImage image, float alphaWhite, float alphaBlack) {
    Graphics g = image.getGraphics();
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();

    if (alphaWhite > 0) {
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.setColor(Color.WHITE);
      Rectangle rect = new Rectangle(0, 0, imageWidth, imageHeight);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaWhite));
      g2d.fill(rect);
      g2d.dispose();
    }

    if (alphaBlack > 0) {
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.setColor(Color.BLACK);
      Rectangle rect = new Rectangle(0, 0, imageWidth, imageHeight);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaBlack));
      g2d.fill(rect);
      g2d.dispose();
    }
  }
}
