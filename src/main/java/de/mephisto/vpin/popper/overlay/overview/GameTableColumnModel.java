package de.mephisto.vpin.popper.overlay.overview;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class GameTableColumnModel extends DefaultTableColumnModel {
  public GameTableColumnModel() {
    TableColumn firstColumn = new TableColumn(0);
    firstColumn.setHeaderValue("Table");
    addColumn(firstColumn);
    TableColumn secondColumn = new TableColumn(1);
    secondColumn.setHeaderValue("ROM");
    addColumn(secondColumn);
    TableColumn thirdColumn = new TableColumn(2);
    thirdColumn.setHeaderValue("Status");
    addColumn(thirdColumn);
  }
}
