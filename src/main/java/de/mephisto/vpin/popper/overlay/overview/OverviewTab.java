package de.mephisto.vpin.popper.overlay.overview;

import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.resources.ResourceLoader;

import javax.swing.*;
import java.awt.*;

public class OverviewTab extends JPanel {

  private final GamesTable gamesTable;
  private OverviewTabActionListener actionListener;
  private JButton highscoreButton;
  private ConfigWindow configWindow;


  public OverviewTab(ConfigWindow configWindow, GameRepository repository) {
    super(new BorderLayout());
    this.configWindow = configWindow;

    setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    this.actionListener = new OverviewTabActionListener(repository, this);
    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    JToolBar toolBar = new JToolBar("Overview Toolbar");
    this.addButtons(toolBar);
    this.add(toolBar, BorderLayout.NORTH);

    gamesTable = new GamesTable(this, repository, new GameTableModel(repository), new GameTableColumnModel());
    JScrollPane sp = new JScrollPane(gamesTable);
    this.add(sp, BorderLayout.CENTER);
  }

  private void addButtons(JToolBar toolBar) {
    JButton button = makeNavigationButton("reload.png", "tableRescan", "Rescan All Tables", "Rescan all tables");
    toolBar.add(button);
    button = makeNavigationButton("refresh.png", "tableRefresh", "Refresh List", "Refresh all tables");
    toolBar.add(button);
    toolBar.addSeparator();
    highscoreButton = makeNavigationButton("highscores.png", "tableHighscore", "Show Highscore", "Show Table Highscore");
    highscoreButton.setEnabled(false);
    toolBar.add(highscoreButton);
  }

  public JButton getHighscoreButton() {
    return highscoreButton;
  }

  public GamesTable getGamesTable() {
    return gamesTable;
  }

  public ConfigWindow getConfigWindow() {
    return configWindow;
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
    button.setIcon(new ImageIcon(ResourceLoader.getResource(imageName)));

    return button;
  }
}
