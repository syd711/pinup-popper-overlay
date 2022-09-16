package de.mephisto.vpin.popper.overlay.table;

import de.mephisto.vpin.GameInfo;
import de.mephisto.vpin.VPinService;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GameTableModel extends AbstractTableModel {

  private final List<GameInfo> gameInfos;

  public GameTableModel(VPinService service) {
    gameInfos = service.getGameInfos();
  }

  @Override
  public int getRowCount() {
    return gameInfos.size();
  }

  @Override
  public int getColumnCount() {
    return 6;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    GameInfo gameInfo = gameInfos.get(rowIndex);
    if(columnIndex == 0) {
      return gameInfo.getId();
    }
    if(columnIndex == 1) {
      return gameInfo.getGameDisplayName();
    }
    if(columnIndex == 2) {
      return gameInfo.getEmulatorName();
    }
    if(columnIndex == 3) {
      return gameInfo.getRom();
    }
    if(columnIndex == 4) {
      return gameInfo.getNumberPlays();
    }
    if(columnIndex == 5) {
      if(StringUtils.isEmpty(gameInfo.getRom())) {
        return "No rom information found for table.";
      }
      else if(!gameInfo.hasHighscore()) {
        return "No highscore files found.";
      }
      return "";
    }

    return "";
  }
}
