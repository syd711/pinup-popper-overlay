package de.mephisto.vpin.popper.overlay.settings;

import com.sun.javafx.scene.input.KeyCodeMap;
import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.ConfigWindow;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class SettingsTab extends JPanel {

  private ConfigWindow configWindow;
  private final SettingsTabActionListener actionListener;

  public SettingsTab(ConfigWindow configWindow, GameRepository repository) {
    this.configWindow = configWindow;
    actionListener = new SettingsTabActionListener(repository);
    setBackground(Color.WHITE);


    GroupLayout groupLayout = new GroupLayout(this);
    List<GameInfo> gameInfos = repository.getGameInfos();
    Vector<GameInfo> data = new Vector<>(gameInfos);
    data.insertElementAt(null, 0);
    JComboBox tableSelection = new JComboBox(data);
    tableSelection.setActionCommand("tableOfTheMonthSelector");
    tableSelection.addActionListener(this.actionListener);

    groupLayout.setHorizontalGroup(
        groupLayout.createSequentialGroup()
            .addComponent(new JLabel("Table of the Month:"))
            .addComponent(tableSelection));

    groupLayout = new GroupLayout(this);
    data.insertElementAt(null, 0);
    JComboBox modifierCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "CTRL", "ALT", "SHIFT" }));
    modifierCombo.setActionCommand("modifierCombo");
    modifierCombo.addActionListener(this.actionListener);

    JComboBox keyCombo = new JComboBox(new DefaultComboBoxModel(getKeys()));
    keyCombo.setActionCommand("keyCombo");
    keyCombo.addActionListener(this.actionListener);

    groupLayout.setHorizontalGroup(
        groupLayout.createSequentialGroup()
            .addComponent(new JLabel("Overlay Shortcut:"))
            .addComponent(modifierCombo)
            .addComponent(keyCombo));


  }

  private KeyStroke[] getKeys() {
    JTextField textField = new JTextField();
    InputMap inputMap = textField.getInputMap( JComponent.WHEN_FOCUSED );
    return inputMap.allKeys();
  }


  class KeyBinding {



    @Override
    public String toString() {
      return super.toString();
    }
  }
}
