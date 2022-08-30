package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.commons.GameRepository;
import de.mephisto.vpin.highscores.HighsoreResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OverlayGenerator {
  private final static Logger LOG = LoggerFactory.getLogger(OverlayGenerator.class);

  private final GameRepository gameRepository;

  public static void main(String[] args) {
    new OverlayGenerator().generate();
  }

  OverlayGenerator() {
    gameRepository = GameRepository.create();
  }

  public void generate() {
    try {
      long start = System.currentTimeMillis();
      BufferedImage backgroundImage = ImageIO.read(new File("./resources", "background.jpg"));

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice gd = ge.getDefaultScreenDevice();
      GraphicsConfiguration gc = gd.getDefaultConfiguration();

      BufferedImage rotated = create(backgroundImage, Math.PI / 2, gc);
      OverlayGraphics.drawGames(rotated, gameRepository, new HighsoreResolver(new File("./")));

      BufferedImage rotatedTwice = create(rotated, -Math.PI / 2, gc);
      writeImage(rotatedTwice);
      long duration = System.currentTimeMillis() - start;
      LOG.info("Generation took " + duration + "ms");
    } catch (Exception e) {
      LOG.error("Failed to generate overlay: " + e.getMessage(),e );
    }
  }

  private void writeImage(BufferedImage rotated1) throws IOException {
    File outputfile = new File("./resources", "generated.png");
    ImageIO.write(rotated1, "png", outputfile);
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
