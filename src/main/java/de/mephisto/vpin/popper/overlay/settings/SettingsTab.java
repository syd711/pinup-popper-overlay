package de.mephisto.vpin.popper.overlay.settings;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.util.Keys;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;

public class SettingsTab extends JPanel {


  private ConfigWindow configWindow;
  private final SettingsTabActionListener actionListener;

  public SettingsTab(ConfigWindow configWindow, GameRepository repository) {
    this.configWindow = configWindow;
    actionListener = new SettingsTabActionListener(repository);
    setBackground(Color.WHITE);



    setLayout(new MigLayout("gap rel 8 insets 10", "left"));

    List<GameInfo> gameInfos = repository.getGameInfos();
    Vector<GameInfo> data = new Vector<>(gameInfos);
    data.insertElementAt(null, 0);
    JComboBox tableSelection = new JComboBox(data);
    tableSelection.setActionCommand("tableOfTheMonthSelector");
    tableSelection.addActionListener(this.actionListener);

    add(new JLabel("Table of the Month:"));
    add(tableSelection, "span 2");
    add(new JLabel(""), "wrap");

    data.insertElementAt(null, 0);
    JComboBox modifierCombo = new JComboBox(new DefaultComboBoxModel(new String[]{"Left-CTRL", "Right-CTRL", "ALT", "Left-SHIFT", "Right-SHIFT"}));
    modifierCombo.setActionCommand("modifierCombo");
    modifierCombo.addActionListener(this.actionListener);

    JComboBox keyCombo = new JComboBox(new DefaultComboBoxModel(new Vector(Keys.getKeyNames())));
    keyCombo.setActionCommand("keyCombo");
    keyCombo.addActionListener(this.actionListener);

    add(new JLabel("Overlay Shortcut:"));
    add(modifierCombo);
    add(keyCombo, "wrap");
  }
}
