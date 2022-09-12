package de.mephisto.vpin.popper.overlay.commands;

import de.mephisto.vpin.popper.overlay.ConfigWindow;

import javax.swing.*;
import java.awt.*;

public class RuleDialog extends JDialog {

  public RuleDialog(ConfigWindow configWindow) {
    super(configWindow);

    this.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    this.setLayout(new BorderLayout());
    this.setModal(true);
    this.setSize(450, 380);
    this.setTitle("DOF Rule");
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    setLocation(x, y);

    this.setVisible(true);
  }
}
