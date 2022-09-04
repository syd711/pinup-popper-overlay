package de.mephisto.vpin.popper.overlay.settings;

import de.mephisto.vpin.games.GameRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsTabActionListener implements ActionListener {
  private GameRepository repository;

  public SettingsTabActionListener(GameRepository repository) {
    this.repository = repository;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if (cmd.equals("tableOfTheMonthSelector")) {
      Object selectedItem = ((JComboBox) e.getSource()).getSelectedItem();
      System.out.println(selectedItem);
    }
  }
}
