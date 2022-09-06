package de.mephisto.vpin.popper.overlay.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GraphicsGenerator {
  private final static Logger LOG = LoggerFactory.getLogger(GraphicsGenerator.class);

  protected BufferedImage loadBackground(File file) throws Exception {
    return ImageIO.read(file);
  }

  public static BufferedImage rotateLeft(BufferedImage image) {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gd.getDefaultConfiguration();
    return create(image, -Math.PI / 2, gc);
  }

  public static BufferedImage rotateRight(BufferedImage image) {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gd.getDefaultConfiguration();
    return create(image, Math.PI / 2, gc);
  }

  protected BufferedImage writeImage(BufferedImage image, File file) throws IOException {
    long writeDuration = System.currentTimeMillis();
    BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(file));
    ImageIO.write(image, "JPG", imageOutputStream);
    long duration = System.currentTimeMillis() - writeDuration;
    LOG.info("Writing image took " + duration + "ms.");
    return image;
  }


  private static BufferedImage create(BufferedImage image, double angle, GraphicsConfiguration gc) {
    double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
    int w = image.getWidth(), h = image.getHeight();
    int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h
        * cos + w * sin);
    int transparency = image.getColorModel().getTransparency();
    BufferedImage result = gc.createCompatibleImage(neww, newh, transparency);
    Graphics2D g = result.createGraphics();
    g.translate((neww - w) / 2, (newh - h) / 2);
    g.rotate(angle, w / 2, h / 2);
    g.drawRenderedImage(image, null);
    return result;
  }
}
