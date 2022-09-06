package de.mephisto.vpin.popper.overlay.settings;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.generator.OverlayGenerator;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.popper.overlay.util.Keys;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SettingsTabActionListener implements ActionListener {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SettingsTabActionListener.class);

  private final SettingsTab settingsTab;
  private final GameRepository repository;

  public SettingsTabActionListener(SettingsTab settingsTab, GameRepository repository) {
    this.settingsTab = settingsTab;
    this.repository = repository;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if (cmd.equals("tableOfTheMonthSelector")) {
      GameInfo selectedItem = (GameInfo) ((JComboBox) e.getSource()).getSelectedItem();
      String value = "";
      if (selectedItem != null) {
        value = String.valueOf(selectedItem.getId());
      }
      Config.getOverlayConfig().set("overlay.challengedTable", value);
    }
    else if (cmd.equals("modifierCombo")) {
      this.saveOverlayKeyBinding();
    }
    else if (cmd.equals("keyCombo")) {
      this.saveOverlayKeyBinding();
    }
    else if (cmd.equals("generateOverlay")) {
      this.settingsTab.generateOverlay();
    }
    else if (cmd.equals("showOverlay")) {
      try {
        File file = OverlayGenerator.GENERATED_OVERLAY_FILE;
        if(!OverlayGenerator.GENERATED_OVERLAY_FILE.exists()) {
          file = new File("./resources/", Config.getOverlayGeneratorConfig().get("overlay.background"));
        }
        Desktop.getDesktop().open(file);
      } catch (IOException ex) {
        LOG.error("Failed to open overlay file: " + ex.getMessage(), ex);
      }
    }
  }

  private void saveOverlayKeyBinding() {
    String key = (String) settingsTab.getKeyCombo().getSelectedItem();
    String modifier = (String) settingsTab.getModifierCombo().getSelectedItem();

    if (key.length() == 1) {
      key = key.toLowerCase();
    }

    if (modifier != null) {
      int modifierNum = Keys.getModifier(modifier);
      key = modifierNum + "+" + key;
    }

    Config.getOverlayConfig().set("overlay.hotkey", key);
  }
}
