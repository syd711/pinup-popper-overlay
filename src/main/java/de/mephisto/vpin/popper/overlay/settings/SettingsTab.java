package de.mephisto.vpin.popper.overlay.settings;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.popper.overlay.util.Keys;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class SettingsTab extends JPanel {

  private final ConfigWindow configWindow;
  private final SettingsTabActionListener actionListener;
  private final JComboBox modifierCombo;
  private final JComboBox keyCombo;

  public SettingsTab(ConfigWindow configWindow, GameRepository repository) {
    this.configWindow = configWindow;
    actionListener = new SettingsTabActionListener(this, repository);
    setBackground(Color.WHITE);

    setLayout(new MigLayout("gap rel 8 insets 10", "left"));

    List<GameInfo> gameInfos = repository.getGameInfos();
    Vector<GameInfo> data = new Vector<>(gameInfos);
    data.insertElementAt(null, 0);
    JComboBox tableSelection = new JComboBox(data);
    tableSelection.setActionCommand("tableOfTheMonthSelector");
    tableSelection.addActionListener(this.actionListener);
    int selection = Config.getOverlayConfig().getInt("overlay.tableOfTheMonth");
    if(selection > 0) {
      GameInfo gameInfo = repository.getGameInfo(selection);
      tableSelection.setSelectedItem(gameInfo);
    }

    add(new JLabel("Table of the Month:"));
    add(tableSelection, "span 3");
    add(new JLabel(""), "wrap");

    data.insertElementAt(null, 0);
    Vector<String> modifierNames = new Vector<>(Keys.getModifierNames());
    modifierNames.insertElementAt(null, 0);
    modifierCombo = new JComboBox(new DefaultComboBoxModel(modifierNames));
    modifierCombo.setActionCommand("modifierCombo");
    modifierCombo.addActionListener(this.actionListener);

    keyCombo = new JComboBox(new DefaultComboBoxModel(new Vector(Keys.getKeyNames())));
    keyCombo.setActionCommand("keyCombo");
    keyCombo.addActionListener(this.actionListener);


    String hotkey = Config.getOverlayConfig().get("overlay.hotkey");
    if(hotkey != null) {
      if(hotkey.contains("+")) {
        String[] split = hotkey.split("\\+");
        String key = split[1];
        modifierCombo.setSelectedItem(Keys.getModifierName(Integer.parseInt(split[0])));
        keyCombo.setSelectedItem(key.toUpperCase(Locale.ROOT));
      }
      else {
        modifierCombo.setSelectedIndex(0);
        keyCombo.setSelectedItem(hotkey.toUpperCase(Locale.ROOT));
      }
    }

    add(new JLabel("Overlay Shortcut:"));
    add(modifierCombo);
    add(new JLabel("+"));
    add(keyCombo, "wrap");
  }

  public JComboBox getModifierCombo() {
    return modifierCombo;
  }

  public JComboBox getKeyCombo() {
    return keyCombo;
  }
}
