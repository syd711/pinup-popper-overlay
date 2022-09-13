package de.mephisto.vpin.popper.overlay.commands;

import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.dof.DOFCommand;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CommandTableModel extends AbstractTableModel {

  private final List<DOFCommand> commandList;

  public CommandTableModel(VPinService repository) {
    commandList = repository.getDOFCommands();
  }

  @Override
  public int getRowCount() {
    return commandList.size();
  }

  @Override
  public int getColumnCount() {
    return 9;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    DOFCommand command = commandList.get(rowIndex);
    if (columnIndex == 0) {
      return command.getId();
    }
    if (columnIndex == 1) {
      return command.getDescription();
    }
    if (columnIndex == 2) {
      return command.getUnit();
    }
    if (columnIndex == 3) {
      return command.getPortNumber();
    }
    if (columnIndex == 4) {
      return command.getValue();
    }
    if (columnIndex == 5) {
      return command.getDurationMs();
    }
    if (columnIndex == 6) {
      return command.getTrigger();
    }
    if (columnIndex == 7) {
      return command.getKeyBinding();
    }
    if (columnIndex == 8) {
      return command.isToggle();
    }
    return "";
  }
}
