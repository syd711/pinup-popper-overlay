package de.mephisto.vpin.popper.overlay.commands;

import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.dof.DOFCommand;
import de.mephisto.vpin.popper.overlay.util.Keys;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Locale;

public class CommandTableModel extends AbstractTableModel {

  private VPinService repository;

  public CommandTableModel(VPinService repository) {
    this.repository = repository;
  }

  @Override
  public int getRowCount() {
    return repository.getDOFCommands().size();
  }

  @Override
  public int getColumnCount() {
    return 9;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    List<DOFCommand> dofCommands = repository.getDOFCommands();
    DOFCommand command = dofCommands.get(rowIndex);
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
      String keyBinding = command.getKeyBinding();
      if(!StringUtils.isEmpty(keyBinding)) {
        if (keyBinding.contains("+")) {
          String[] split = keyBinding.split("\\+");
          String key = split[1];
          String modifier = Keys.getModifierName(Integer.parseInt(split[0]));
          return modifier + " + " + key;
        }
        return keyBinding;
      }
      return "";
    }
    if (columnIndex == 8) {
      return command.isToggle();
    }
    return "";
  }
}
