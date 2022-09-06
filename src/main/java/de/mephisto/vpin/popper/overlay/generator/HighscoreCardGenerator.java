package de.mephisto.vpin.popper.overlay.generator;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.popper.overlay.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;

public class HighscoreCardGenerator extends GraphicsGenerator {
  private final static Logger LOG = LoggerFactory.getLogger(HighscoreCardGenerator.class);

  public BufferedImage generate(GameInfo game) throws Exception {
    try {
      BufferedImage backgroundImage = super.loadBackground(new File("./resources", Config.getCardGeneratorConfig().get("card.background")));
      HighscoreCardGraphics.drawHighscores(backgroundImage, game);
      File target = new File(game.getVpxFile().getName());
      super.writeImage(backgroundImage, target);
      return backgroundImage;
    } catch (Exception e) {
      LOG.error("Failed to generate card: " + e.getMessage(), e);
      throw e;
    }
  }
}
