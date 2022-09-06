package de.mephisto.vpin.popper.overlay.generator;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;

public class OverlayGenerator extends GraphicsGenerator {
  private final static Logger LOG = LoggerFactory.getLogger(OverlayGenerator.class);

  public final static File GENERATED_OVERLAY_FILE = new File("./resources", "overlay.jpg");

  private final GameRepository gameRepository;

  public static void main(String[] args) throws Exception {
    generateOverlay();
  }

  public static void generateOverlay() throws Exception {
    new OverlayGenerator(GameRepository.create()).generate();
  }

  OverlayGenerator(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public BufferedImage generate() throws Exception {
    try {
      BufferedImage backgroundImage = super.loadBackground(new File("./resources", Config.getOverlayGeneratorConfig().getString("overlay.background")));
      BufferedImage rotated = super.rotateRight(backgroundImage);

      int selection = Config.getOverlayConfig().getInt("overlay.challengedTable");
      GameInfo gameOfTheMonth = null;
      if (selection > 0) {
        gameOfTheMonth = gameRepository.getGameInfo(selection);
      }
      OverlayGraphics.drawGames(rotated, gameRepository, gameOfTheMonth);

      BufferedImage rotatedTwice = super.rotateLeft(rotated);
      super.writeImage(rotatedTwice, GENERATED_OVERLAY_FILE);
      return rotatedTwice;
    } catch (Exception e) {
      LOG.error("Failed to generate overlay: " + e.getMessage(), e);
      throw e;
    } finally {
      gameRepository.shutdown();
    }
  }
}
