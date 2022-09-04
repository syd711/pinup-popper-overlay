package de.mephisto.vpin.popper.overlay.overview;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GamesTable extends JTable {

  private final OverviewTab overviewTab;
  private final GameRepository gameRepository;

  public GamesTable(OverviewTab overviewTab, GameRepository gameRepository, GameTableModel tableModel, GameTableColumnModel columnModel) {
    super(tableModel, columnModel);
    this.overviewTab = overviewTab;
    this.gameRepository = gameRepository;

    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setColumnSelectionAllowed(false);
    getSelectionModel().addListSelectionListener(this);
    setRowHeight(20);

    getColumnModel().getColumn(0).setPreferredWidth(160);
    getColumnModel().getColumn(1).setPreferredWidth(60);
    getColumnModel().getColumn(2).setPreferredWidth(240);

    List<GameInfo> games = gameRepository.getGameInfos();
    setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(Color.WHITE);
        if (isSelected) {
          c.setBackground(Color.LIGHT_GRAY);
        }

        GameInfo game = games.get(row);
        if (StringUtils.isEmpty(game.getRom())) {
          c.setBackground(Color.decode("#FF9999"));
        }
        else if(!game.isHighscoreSupported() || !game.hasHighscore()) {
          c.setBackground(Color.decode("#FFCC33"));
        }

        table.repaint();
        return c;
      }
    });
  }

  public void valueChanged(ListSelectionEvent e) {
    List<GameInfo> gameInfos = gameRepository.getGameInfos();
    int[] selectedRow = getSelectedRows();
    if (selectedRow.length > 0) {
      int row = selectedRow[0];
      GameInfo gameInfo = gameInfos.get(row);
      overviewTab.getHighscoreButton().setEnabled(gameInfo.hasHighscore());
    }
  }
}
