package de.mephisto.vpin.popper.overlay.commands;

import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.util.WidgetFactory;
import net.miginfocom.swing.MigLayout;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommandsTab extends JPanel implements ActionListener {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CommandsTab.class);

  private final ConfigWindow configWindow;
  private VPinService service;


  public CommandsTab(ConfigWindow configWindow, VPinService service) {
    this.configWindow = configWindow;
    this.service = service;

    setBackground(ConfigWindow.DEFAULT_BG_COLOR);

    setLayout(new BorderLayout());

    JPanel toolBar = new JPanel();
    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    toolBar.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    toolBar.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 4));
    this.addButtons(toolBar);
    this.add(toolBar, BorderLayout.NORTH);

    JPanel settingsPanel = new JPanel();
    settingsPanel.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    settingsPanel.setLayout(new MigLayout("gap rel 8 insets 10", "left", "top"));
    this.add(settingsPanel, BorderLayout.WEST);


  }

  private void addButtons(JPanel toolBar) {
    WidgetFactory.createButton(toolBar, "createRule", "Create Rule", this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if(cmd.equals("createRule")) {
      new RuleDialog(configWindow, service);
    }
  }
}
