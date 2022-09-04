package de.mephisto.vpin.popper.overlay.overview;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GameTableModel extends AbstractTableModel {

  private final List<GameInfo> gameInfos;

  public GameTableModel(GameRepository repository) {
    gameInfos = repository.getGameInfos();
  }

  @Override
  public int getRowCount() {
    return gameInfos.size();
  }

  @Override
  public int getColumnCount() {
    return 3;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    GameInfo gameInfo = gameInfos.get(rowIndex);
    if(columnIndex == 0) {
      return gameInfo.getGameDisplayName();
    }
    if(columnIndex == 1) {
      return gameInfo.getRom();
    }
    if(columnIndex == 2) {
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
