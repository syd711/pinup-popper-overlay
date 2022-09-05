package de.mephisto.vpin.popper.overlay.settings;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.popper.overlay.util.Keys;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
      if(selectedItem != null) {
        value = String.valueOf(selectedItem.getId());
      }
      Config.getOverlayConfig().set("overlay.challengedTable", value);
    }
    else if(cmd.equals("modifierCombo")) {
      this.saveOverlayKeyBinding();
    }
    else if(cmd.equals("keyCombo")) {
      this.saveOverlayKeyBinding();
    }
  }

  private void saveOverlayKeyBinding() {
    String key = (String) settingsTab.getKeyCombo().getSelectedItem();
    String modifier = (String) settingsTab.getModifierCombo().getSelectedItem();

    if(key.length() == 1) {
      key = key.toLowerCase();
    }

    if(modifier != null) {
      int modifierNum = Keys.getModifier(modifier);
      key = modifierNum + "+" + key;
    }

    Config.getOverlayConfig().set("overlay.hotkey", key);
  }
}
