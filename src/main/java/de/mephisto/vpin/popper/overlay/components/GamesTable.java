package de.mephisto.vpin.popper.overlay.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class GamesTable extends JTable {

  public GamesTable(GameTableModel tableModel, GameTableColumnModel columnModel) {
    super(tableModel, columnModel);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setRowHeight(20);
    setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
        return c;
      }
    });


  }
}
