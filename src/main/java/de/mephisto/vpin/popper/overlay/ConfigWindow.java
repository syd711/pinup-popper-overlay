package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.overview.OverviewTab;
import de.mephisto.vpin.popper.overlay.resources.ResourceLoader;
import de.mephisto.vpin.popper.overlay.settings.SettingsTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ConfigWindow extends JFrame {

  private static ConfigWindow instance;

  public static ConfigWindow getInstance() {
    return instance;
  }

  public ConfigWindow() throws Exception {
    ConfigWindow.instance = this;

    setUIFont (new javax.swing.plaf.FontUIResource("Tahoma",Font.PLAIN,14));
    UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName());

    GameRepository gameRepository = GameRepository.create();

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Overview", null, new OverviewTab(this, gameRepository),"");
    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    tabbedPane.addTab("Settings", null, new SettingsTab(this, gameRepository), "Table Challenge, Key-Bindings, etc.");
    tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

    tabbedPane.setBackground(Color.WHITE);
    tabbedPane.setBackgroundAt(0, Color.WHITE);
    tabbedPane.setBackgroundAt(1, Color.WHITE);
    add(tabbedPane);


    // frame size 300 width and 300 height
    setSize(1024,800);
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

  private JComponent makeTextPanel() {
    return new JPanel();
  }

  private ImageIcon createImageIcon(String s) {
    return new ImageIcon(ResourceLoader.getResource(s));
  }

  public static void main(String[] args) throws Exception {
    new ConfigWindow();
  }
}
