package de.mephisto.vpin.popper.overlay.settings;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class SettingsTab extends JPanel {

  private final SettingsTabActionListener actionListener;

  public SettingsTab(GameRepository repository) {
    actionListener = new SettingsTabActionListener(repository);

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
  }

}
