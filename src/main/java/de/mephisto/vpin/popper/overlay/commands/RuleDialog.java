package de.mephisto.vpin.popper.overlay.commands;

import de.mephisto.vpin.popper.overlay.ConfigWindow;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class RuleDialog extends JDialog {

  public RuleDialog(ConfigWindow configWindow) {
    super(configWindow);

    this.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    this.setLayout(new BorderLayout());
    this.setModal(true);
    this.setSize(650, 380);
    this.setTitle("DOF Rule");
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    setLocation(x, y);


    JPanel rootPanel = new JPanel();
    rootPanel.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    rootPanel.setLayout(new MigLayout("gap rel 8 insets 10", "left", "top"));
    this.add(rootPanel, BorderLayout.CENTER);


    rootPanel.add(new JLabel("Set output #"));
    rootPanel.add(new JLabel("b"));
    rootPanel.add(new JLabel("c"), "wrap");

    rootPanel.add(new JLabel("from board"));
    rootPanel.add(new JLabel(""));
    rootPanel.add(new JLabel("f"), "wrap");

    rootPanel.add(new JLabel("to value"));
    rootPanel.add(new JLabel(""));
    rootPanel.add(new JLabel("f"), "wrap");

    rootPanel.add(new JLabel("when"));
    rootPanel.add(new JLabel(""));
    rootPanel.add(new JLabel("f"), "wrap");

    rootPanel.add(new JLabel("for"));
    rootPanel.add(new JLabel(""));
    rootPanel.add(new JLabel("f"), "wrap");

    this.setVisible(true);
  }
}
