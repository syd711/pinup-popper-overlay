package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.games.HighscoreChangedEvent;
import de.mephisto.vpin.games.RepositoryListener;
import de.mephisto.vpin.popper.overlay.cardsettings.CardSettingsTab;
import de.mephisto.vpin.popper.overlay.overlaysettings.OverlaySettingsTab;
import de.mephisto.vpin.popper.overlay.overview.OverviewTab;
import de.mephisto.vpin.popper.overlay.resources.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ConfigWindow extends JFrame implements RepositoryListener {
  private final static Logger LOG = LoggerFactory.getLogger(ConfigWindow.class);

  private static ConfigWindow instance;
  private final GameRepository gameRepository;

  public static ConfigWindow getInstance() {
    return instance;
  }

  public ConfigWindow() throws Exception {
    ConfigWindow.instance = this;

    setUIFont (new javax.swing.plaf.FontUIResource("Tahoma",Font.PLAIN,14));
    UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName());

    gameRepository = GameRepository.create();
    gameRepository.addListener(this);

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Highscore Overlay Settings", null, new OverlaySettingsTab(this, gameRepository), "Table Challenge, Key-Bindings, etc.");
    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    tabbedPane.addTab("Highscore Cards Settings", null, new CardSettingsTab(this, gameRepository), "Highscore Generation Settings");
    tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

    tabbedPane.addTab("Tables", null, new OverviewTab(this, gameRepository),"");
    tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);


    tabbedPane.setBackground(Color.WHITE);
    tabbedPane.setBackgroundAt(0, Color.WHITE);
    tabbedPane.setBackgroundAt(1, Color.WHITE);
    tabbedPane.setBackgroundAt(2, Color.WHITE);
    add(tabbedPane);

    setSize(1336,990);
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    setLocation(x, y);
    setResizable(false);

    // setting the title of Frame
    setTitle("PinUP Popper Overlay");
    setIconImage(ResourceLoader.getResource("logo.png"));

    setVisible(true);


    Action escapeAction = new AbstractAction() {
      private static final long serialVersionUID = 5572504000935312338L;
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    };

    this.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("F2"),
        "pressed");
    this.getRootPane().getActionMap().put("pressed", escapeAction);

  }

  public static void setUIFont (javax.swing.plaf.FontUIResource f){
    java.util.Enumeration keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get (key);
      if (value instanceof javax.swing.plaf.FontUIResource)
        UIManager.put (key, f);
    }
  }

  public static void main(String[] args) throws Exception {
    new ConfigWindow();
  }

  @Override
  public void gameScanned(GameInfo gameInfo) {

  }

  @Override
  public void highscoreChanged(HighscoreChangedEvent highscoreChangedEvent) {
    LOG.info("Highscore changed for " + highscoreChangedEvent.getGameInfo());
  }
}
