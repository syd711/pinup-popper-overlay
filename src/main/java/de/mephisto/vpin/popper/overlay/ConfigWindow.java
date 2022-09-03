package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.components.GameTableColumnModel;
import de.mephisto.vpin.popper.overlay.components.GameTableModel;
import de.mephisto.vpin.popper.overlay.components.GamesTable;
import de.mephisto.vpin.popper.overlay.components.OverviewTab;
import de.mephisto.vpin.popper.overlay.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ConfigWindow extends JFrame {

  public ConfigWindow() throws Exception {
    setUIFont (new javax.swing.plaf.FontUIResource("Tahoma",Font.PLAIN,14));
    UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName());

    GameRepository gameRepository = GameRepository.create();

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    JTabbedPane tabbedPane = new JTabbedPane();
    ImageIcon icon = createImageIcon("logo.png");
    tabbedPane.addTab("Overview", icon, new OverviewTab(gameRepository),"");
    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);



    JComponent panel2 = makeTextPanel("Panel #2");
    tabbedPane.addTab("Overlay Settings", icon, panel2,
        "Does twice as much nothing");
    tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

    JComponent panel3 = makeTextPanel("Panel #3");
    tabbedPane.addTab("Tab 3", icon, panel3,
        "Still does nothing");
    tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

    add(tabbedPane);

    // frame size 300 width and 300 height
    setSize(1024,800);
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    setLocation(x, y);

    // setting the title of Frame
    setTitle("VPin Overlay");
    setIconImage(Toolkit.getDefaultToolkit().getImage(ResourceLoader.getResource("logo.png")));



    // now frame will be visible, by default it is not visible
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

  private JComponent makeTextPanel(String s) {
    return new JPanel();
  }

  private ImageIcon createImageIcon(String s) {
    return new ImageIcon(ResourceLoader.getResource(s));
  }

  public static void main(String[] args) throws Exception {
    new ConfigWindow();
  }
}
