package de.mephisto.vpin.popper.overlay.overlaysettings;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.generator.OverlayGenerator;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.popper.overlay.util.JFontChooser;
import de.mephisto.vpin.popper.overlay.util.Keys;
import de.mephisto.vpin.util.SystemInfo;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class OverlaySettingsTabActionListener implements ActionListener {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OverlaySettingsTabActionListener.class);

  private final OverlaySettingsTab overlaySettingsTab;
  private final GameRepository repository;

  public OverlaySettingsTabActionListener(OverlaySettingsTab overlaySettingsTab, GameRepository repository) {
    this.overlaySettingsTab = overlaySettingsTab;
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
      this.overlaySettingsTab.generateOverlay();
    }
    else if (cmd.equals("showOverlay")) {
      try {
        File file = OverlayGenerator.GENERATED_OVERLAY_FILE;
        if (!OverlayGenerator.GENERATED_OVERLAY_FILE.exists()) {
          file = new File(SystemInfo.RESOURCES, Config.getOverlayGeneratorConfig().get("overlay.background"));
        }
        Desktop.getDesktop().open(file);
      } catch (IOException ex) {
        LOG.error("Failed to open overlay file: " + ex.getMessage(), ex);
      }
    }
  }

  private void saveOverlayKeyBinding() {
    String key = (String) overlaySettingsTab.keyCombo.getSelectedItem();
    String modifier = (String) overlaySettingsTab.modifierCombo.getSelectedItem();

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
