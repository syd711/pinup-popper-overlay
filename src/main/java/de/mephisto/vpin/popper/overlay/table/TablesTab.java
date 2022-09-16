package de.mephisto.vpin.popper.overlay.table;

import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.util.WidgetFactory;

import javax.swing.*;
import java.awt.*;

public class TablesTab extends JPanel {

  private TablesTabActionListener actionListener;

  GamesTable gamesTable;
  ConfigWindow configWindow;
  JButton highscoreButton;
  JButton scanButton;


  public TablesTab(ConfigWindow configWindow, VPinService service) {
    super(new BorderLayout());
    this.configWindow = configWindow;

    setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    this.actionListener = new TablesTabActionListener(service, this);
    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    JPanel toolBar = new JPanel();
    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    toolBar.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    toolBar.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 4));
    this.addButtons(toolBar);
    this.add(toolBar, BorderLayout.NORTH);

    gamesTable = new GamesTable(this, service, new GameTableModel(service), new GameTableColumnModel());
    JScrollPane sp = new JScrollPane(gamesTable);
    this.add(sp, BorderLayout.CENTER);
  }

  private void addButtons(JPanel toolBar) {
    WidgetFactory.createButton(toolBar, "tableRefresh", "Refresh List", this.actionListener);
    scanButton = WidgetFactory.createButton(toolBar, "tableRescan", "Rescan Table", this.actionListener);
    scanButton.setEnabled(false);
    toolBar.add(scanButton);
    highscoreButton = WidgetFactory.createButton(toolBar, "tableHighscore", "Show Highscore", this.actionListener);
    highscoreButton.setEnabled(false);
    toolBar.add(highscoreButton);
  }

  public GamesTable getGamesTable() {
    return gamesTable;
  }
}
