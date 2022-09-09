package de.mephisto.vpin.popper.overlay.overlaysettings;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.generator.GraphicsGenerator;
import de.mephisto.vpin.popper.overlay.generator.OverlayGenerator;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.popper.overlay.util.Keys;
import de.mephisto.vpin.popper.overlay.util.WidgetFactory;
import de.mephisto.vpin.util.PropertiesStore;
import de.mephisto.vpin.util.SystemInfo;
import net.miginfocom.swing.MigLayout;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class OverlaySettingsTab extends JPanel {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OverlaySettingsTab.class);

  private final ConfigWindow configWindow;
  private final OverlaySettingsTabActionListener actionListener;
  private GameRepository repository;

  final JComboBox modifierCombo;
  final JComboBox keyCombo;

  final JLabel iconLabel;
  final JButton generateButton;

  public OverlaySettingsTab(ConfigWindow configWindow, GameRepository repository) {
    this.configWindow = configWindow;
    actionListener = new OverlaySettingsTabActionListener(this, repository);
    this.repository = repository;
    PropertiesStore store = Config.getOverlayGeneratorConfig();

    setBackground(Color.WHITE);

    setLayout(new BorderLayout());

    JPanel settingsPanel = new JPanel();
    settingsPanel.setBackground(Color.WHITE);
    settingsPanel.setLayout(new MigLayout("gap rel 8 insets 10", "left", "top"));
    this.add(settingsPanel, BorderLayout.WEST);


    Vector<String> modifierNames = new Vector<>(Keys.getModifierNames());
    modifierNames.insertElementAt(null, 0);
    modifierCombo = new JComboBox(new DefaultComboBoxModel(modifierNames));
    modifierCombo.setActionCommand("modifierCombo");
    modifierCombo.addActionListener(this.actionListener);

    keyCombo = new JComboBox(new DefaultComboBoxModel(new Vector(Keys.getKeyNames())));
    keyCombo.setActionCommand("keyCombo");
    keyCombo.addActionListener(this.actionListener);


    String hotkey = Config.getOverlayConfig().get("overlay.hotkey");
    if (hotkey != null) {
      if (hotkey.contains("+")) {
        String[] split = hotkey.split("\\+");
        String key = split[1];
        modifierCombo.setSelectedItem(Keys.getModifierName(Integer.parseInt(split[0])));
        keyCombo.setSelectedItem(key.toUpperCase(Locale.ROOT));
      }
      else {
        modifierCombo.setSelectedIndex(0);
        keyCombo.setSelectedItem(hotkey.toUpperCase(Locale.ROOT));
      }
    }

    settingsPanel.add(new JLabel("Overlay Shortcut:"));
    settingsPanel.add(modifierCombo);
    settingsPanel.add(new JLabel("+"));
    settingsPanel.add(keyCombo, "wrap");


    JLabel separator = new JLabel("");
    separator.setPreferredSize(new Dimension(1, 30));
    settingsPanel.add(separator, "wrap");

    WidgetFactory.createTableSelector(repository, settingsPanel, "Challenged Table:", store, "overlay.challengedTable");

    /******************************** Generator Fields ****************************************************************/
    WidgetFactory.createFileChooser(settingsPanel, "Background Image:", "Select File", store, "overlay.background", "background4k.jpg");
    WidgetFactory.createTextField(settingsPanel, "Challenge Title:", store, "overlay.title.text", "Table of the Month");
    WidgetFactory.createTextField(settingsPanel, "Highscores Title:", store, "overlay.highscores.text", "Latest Highscores");
    WidgetFactory.createFontSelector(settingsPanel, "Title Font:", store, "overlay.title.font");
    WidgetFactory.createFontSelector(settingsPanel, "Table Name Font:", store, "overlay.table.font");
    WidgetFactory.createFontSelector(settingsPanel, "Score Font:", store, "overlay.score.font");
    WidgetFactory.createColorChooser(settingsPanel, "Font Color:", store, "overlay.font.color");
    WidgetFactory.createSpinner(settingsPanel, "Padding Top:", store, "overlay.title.y.offset", 80);
    WidgetFactory.createSpinner(settingsPanel, "Padding Left:", store, "overlay.highscores.row.padding.left", 60);
    WidgetFactory.createSlider(settingsPanel, "Brighten Image:", store, "overlay.alphacomposite.white");
    WidgetFactory.createSlider(settingsPanel, "Darken Image:", store, "overlay.alphacomposite.black");


    settingsPanel.add(new JLabel(""));
    generateButton = new JButton("Generate Overlay");
    generateButton.setActionCommand("generateOverlay");
    generateButton.addActionListener(this.actionListener);

    JButton showOverlayButton = new JButton("Show Overlay Image");
    showOverlayButton.setActionCommand("showOverlay");
    showOverlayButton.addActionListener(this.actionListener);

    settingsPanel.add(generateButton, "span 3");
    settingsPanel.add(showOverlayButton);
    settingsPanel.add(new JLabel(""), "wrap");


    /******************************** Preview *************************************************************************/


    JPanel previewPanel = new JPanel();
    previewPanel.setBackground(Color.BLACK);
    TitledBorder b = BorderFactory.createTitledBorder("Overlay Preview");
    b.setTitleColor(Color.WHITE);
    previewPanel.setBorder(b);
    add(previewPanel, BorderLayout.CENTER);
    previewPanel.setLayout(new MigLayout("gap rel 8 insets 10", "left"));
    iconLabel = new JLabel(getPreviewImage());
    previewPanel.add(iconLabel, "wrap");
  }

  private ImageIcon getPreviewImage() {
    try {
      File file = OverlayGenerator.GENERATED_OVERLAY_FILE;
      if (!OverlayGenerator.GENERATED_OVERLAY_FILE.exists()) {
        file = new File(SystemInfo.RESOURCES, Config.getOverlayGeneratorConfig().get("overlay.background"));
      }
      BufferedImage backgroundImage = ImageIO.read(file);
      BufferedImage image = GraphicsGenerator.rotateRight(backgroundImage);
      int percentage = 900 * 100 / image.getHeight();
      Image newimg = image.getScaledInstance(image.getWidth() * percentage / 100, image.getHeight() * percentage / 100, Image.SCALE_SMOOTH); // scale it the smooth way
      return new ImageIcon(newimg);  // transform it back

    } catch (Exception e) {
      LOG.error("Erorr loading overlay preview: " + e.getMessage(), e);
    }
    return null;
  }

  public void generateOverlay() {
    try {
      iconLabel.setVisible(false);
      generateButton.setEnabled(false);
      OverlayGenerator.generateOverlay(repository);
      iconLabel.setIcon(getPreviewImage());
      generateButton.setEnabled(true);
      iconLabel.setVisible(true);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this.configWindow, "Error generating overlay: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
