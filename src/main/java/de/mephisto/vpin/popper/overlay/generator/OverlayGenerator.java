package de.mephisto.vpin.popper.overlay.generator;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.util.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;

public class OverlayGenerator extends GraphicsGenerator {
  private final static Logger LOG = LoggerFactory.getLogger(OverlayGenerator.class);

  public final static File GENERATED_OVERLAY_FILE = new File(SystemInfo.RESOURCES, "overlay.jpg");

  private final GameRepository repository;

  public static void main(String[] args) throws Exception {
    generateOverlay(GameRepository.create());
  }

  public static void generateOverlay(GameRepository repository) throws Exception {
    new OverlayGenerator(repository).generate();
  }

  OverlayGenerator(GameRepository repository) {
    this.repository = repository;
  }

  public BufferedImage generate() throws Exception {
    try {
      BufferedImage backgroundImage = super.loadBackground(new File(SystemInfo.RESOURCES, Config.getOverlayGeneratorConfig().getString("overlay.background")));
      BufferedImage rotated = rotateRight(backgroundImage);

      int selection = Config.getOverlayConfig().getInt("overlay.challengedTable");
      GameInfo gameOfTheMonth = null;
      if (selection > 0) {
        gameOfTheMonth = repository.getGameInfo(selection);
      }
      OverlayGraphics.drawGames(rotated, repository, gameOfTheMonth);

      BufferedImage rotatedTwice = rotateLeft(rotated);
      super.writeJPG(rotatedTwice, GENERATED_OVERLAY_FILE);
      return rotatedTwice;
    } catch (Exception e) {
      LOG.error("Failed to generate overlay: " + e.getMessage(), e);
      throw e;
    } finally {
      repository.shutdown();
    }
  }
}
