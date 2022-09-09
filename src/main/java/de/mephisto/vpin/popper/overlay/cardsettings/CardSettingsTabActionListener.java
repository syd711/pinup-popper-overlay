package de.mephisto.vpin.popper.overlay.cardsettings;

import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.generator.OverlayGenerator;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.util.SystemInfo;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class CardSettingsTabActionListener implements ActionListener {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CardSettingsTabActionListener.class);

  private final CardSettingsTab cardSettingsTab;

  public CardSettingsTabActionListener(CardSettingsTab cardSettingsTab, GameRepository repository) {
    this.cardSettingsTab = cardSettingsTab;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if (cmd.equals("generateCard")) {
      this.cardSettingsTab.generateOverlay();
    }
    else if (cmd.equals("showCard")) {
      this.cardSettingsTab.showGeneratedCard();
    }
  }
}
