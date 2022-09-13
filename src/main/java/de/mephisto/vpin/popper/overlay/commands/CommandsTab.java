package de.mephisto.vpin.popper.overlay.commands;

import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.dof.DOFCommand;
import de.mephisto.vpin.dof.Trigger;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.table.GameTableColumnModel;
import de.mephisto.vpin.popper.overlay.table.GameTableModel;
import de.mephisto.vpin.popper.overlay.table.GamesTable;
import de.mephisto.vpin.popper.overlay.table.TablesTabActionListener;
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
  private final CommandTable commandTable;
  private final CommandTableModel commandTableModel;
  private VPinService service;

  JButton editButton;


  public CommandsTab(ConfigWindow configWindow, VPinService service) {
    this.configWindow = configWindow;
    this.service = service;

    this.setLayout(new BorderLayout());
    setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    JPanel toolBar = new JPanel();
    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    toolBar.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    toolBar.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 4));
    this.addButtons(toolBar);
    this.add(toolBar, BorderLayout.NORTH);

    commandTableModel = new CommandTableModel(service);
    commandTable = new CommandTable(this, service, commandTableModel, new CommandTableColumnModel());
    JScrollPane sp = new JScrollPane(commandTable);
    this.add(sp, BorderLayout.CENTER);
  }

  private void addButtons(JPanel toolBar) {
    WidgetFactory.createButton(toolBar, "createRule", "Create Rule", this);
    editButton = WidgetFactory.createButton(toolBar, "editRule", "Edit Rule", this);
    editButton.setEnabled(false);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if(cmd.equals("createRule")) {
      DOFCommand dofCommand = new DOFCommand(1, 1, 1, 0, 0, Trigger.TableStart, null, false, "");
      new RuleDialog(configWindow, service, dofCommand);
      service.getDOFCommands().add(dofCommand);
      commandTableModel.fireTableDataChanged();
    }
    else if(cmd.equals("editRule")) {
      DOFCommand selection = commandTable.getSelection();
      new RuleDialog(configWindow, service, selection);
      commandTableModel.fireTableDataChanged();
    }
  }
}
