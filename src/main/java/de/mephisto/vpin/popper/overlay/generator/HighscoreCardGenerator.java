package de.mephisto.vpin.popper.overlay.generator;

import de.mephisto.vpin.PopperScreen;
import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;

public class HighscoreCardGenerator extends GraphicsGenerator {
  private final static Logger LOG = LoggerFactory.getLogger(HighscoreCardGenerator.class);

  public BufferedImage generate(GameInfo game, PopperScreen screen) throws Exception {
    try {
      File sourceFile = new File("./resources", Config.getCardGeneratorConfig().get("card.background"));
      BufferedImage backgroundImage = super.loadBackground(sourceFile);
      HighscoreCardGraphics.drawHighscores(backgroundImage, game);

      File target = game.getPopperMediaFile(screen);
      if(sourceFile.getName().endsWith(".png")) {
        super.writePNG(backgroundImage, target);
      }
      else {
        super.writePNG(backgroundImage, target);
      }
      return backgroundImage;
    } catch (Exception e) {
      LOG.error("Failed to generate card: " + e.getMessage(), e);
      throw e;
    }
  }

  public static void main(String[] args) throws Exception {
    GameRepository repository = GameRepository.create();
    try {
      GameInfo gameInfo = repository.getGameByRom("STLE");
      new HighscoreCardGenerator().generate(gameInfo, PopperScreen.Other2);
    } catch (Exception e) {
      e.printStackTrace();
    }
    repository.shutdown();
  }
}
