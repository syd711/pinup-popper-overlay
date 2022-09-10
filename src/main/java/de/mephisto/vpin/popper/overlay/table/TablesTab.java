package de.mephisto.vpin.popper.overlay.table;

import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.popper.overlay.ConfigWindow;

import javax.swing.*;
import java.awt.*;

public class TablesTab extends JPanel {

  private TablesTabActionListener actionListener;

  GamesTable gamesTable;
  ConfigWindow configWindow;
  JButton highscoreButton;
  JButton scanButton;


  public TablesTab(ConfigWindow configWindow, VPinService repository) {
    super(new BorderLayout());
    this.configWindow = configWindow;

    setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    this.actionListener = new TablesTabActionListener(repository, this);
    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    JPanel toolBar = new JPanel();
    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    toolBar.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    toolBar.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 4));
    this.addButtons(toolBar);
    this.add(toolBar, BorderLayout.NORTH);

    gamesTable = new GamesTable(this, repository, new GameTableModel(repository), new GameTableColumnModel());
    JScrollPane sp = new JScrollPane(gamesTable);
    this.add(sp, BorderLayout.CENTER);
  }

  private void addButtons(JPanel toolBar) {
    JButton refreshButton = makeNavigationButton("refresh.png", "tableRefresh", "Refresh List", "Refresh all tables");
    toolBar.add(refreshButton);
    scanButton = makeNavigationButton("reload.png", "tableRescan", "Rescan Table", "Rescan tables");
    scanButton.setEnabled(false);
    toolBar.add(scanButton);
    highscoreButton = makeNavigationButton("highscores.png", "tableHighscore", "Show Highscore", "Show Table Highscore");
    highscoreButton.setEnabled(false);
    toolBar.add(highscoreButton);
  }

  public GamesTable getGamesTable() {
    return gamesTable;
  }

  private JButton makeNavigationButton(String imageName,
                                       String actionCommand,
                                       String toolTipText,
                                       String altText) {
    //Create and initialize the button.
    JButton button = new JButton(toolTipText);
    button.setActionCommand(actionCommand);
    button.setToolTipText(toolTipText);
    button.addActionListener(actionListener);
//    button.setIcon(new ImageIcon(ResourceLoader.getResource(imageName)));

    return button;
  }
}
