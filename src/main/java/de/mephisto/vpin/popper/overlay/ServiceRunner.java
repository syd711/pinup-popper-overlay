package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.GameInfo;
import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.highscores.HighscoreChangeListener;
import de.mephisto.vpin.highscores.HighscoreChangedEvent;
import de.mephisto.vpin.popper.TableStatusChangeListener;
import de.mephisto.vpin.popper.TableStatusChangedEvent;
import de.mephisto.vpin.popper.overlay.generator.HighscoreCardGenerator;
import de.mephisto.vpin.popper.overlay.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRunner implements HighscoreChangeListener, TableStatusChangeListener {
  private final static Logger LOG = LoggerFactory.getLogger(ServiceRunner.class);
  private final VPinService service;

  public ServiceRunner() {
    service = VPinService.create();
    LOG.info("VPinService started.");

    service.addTableStatusChangeListener(this);

    OverlayWindowFX.launch(OverlayWindowFX.class);
    LOG.info("Overlay window listener started.");

    String targetScreen = Config.getCardGeneratorConfig().get("popper.screen");
    if(!StringUtils.isEmpty(targetScreen)) {
      LOG.info("Added VPin service listener for highscore changes.");
      service.addHighscoreChangeListener(this);
    }
    else {
      LOG.info("Skipped starting highscore card generator, no PinUP popper target screen configured.");
    }
  }

  @Override
  public void highscoreChanged(HighscoreChangedEvent highscoreChangedEvent) {
    try {
      GameInfo gameInfo = highscoreChangedEvent.getGameInfo();
      HighscoreCardGenerator.generateCard(gameInfo);
    } catch (Exception e) {
      LOG.error("Failed to generate highscore card for " + highscoreChangedEvent.getGameInfo() + ": " + e.getMessage(), e);
    }
  }

  @Override
  public void tableLaunched(TableStatusChangedEvent tableStatusChangedEvent) {

  }

  @Override
  public void tableExited(TableStatusChangedEvent tableStatusChangedEvent) {

  }
}
