package de.mephisto.vpin.popper.overlay.cardsettings;

import de.mephisto.vpin.PopperScreen;
import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.generator.HighscoreCardGenerator;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.popper.overlay.util.WidgetFactory;
import de.mephisto.vpin.util.PropertiesStore;
import de.mephisto.vpin.util.SystemInfo;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CardSettingsTab extends JPanel {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CardSettingsTab.class);

  private GameRepository repository;

  private final ConfigWindow configWindow;

  private final CardSettingsTabActionListener actionListener;
  private final JButton generateAllButton;
  final JLabel iconLabel;
  final JButton generateButton;

  public CardSettingsTab(ConfigWindow configWindow, GameRepository repository) {
    this.configWindow = configWindow;
    actionListener = new CardSettingsTabActionListener(this, repository);
    this.repository = repository;
    PropertiesStore store = Config.getCardGeneratorConfig();

    setBackground(ConfigWindow.DEFAULT_BG_COLOR);

    setLayout(new BorderLayout());

    JPanel settingsPanel = new JPanel();
    settingsPanel.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    settingsPanel.setLayout(new MigLayout("gap rel 8 insets 10", "left", "top"));
    this.add(settingsPanel, BorderLayout.WEST);


    List<String> values = Arrays.stream(PopperScreen.values()).sequential().map(e -> e.toString()).collect(Collectors.toList());
    JComboBox screenCombo = WidgetFactory.createCombobox(settingsPanel, values, "PinUP Popper Screen:", store, "popper.screen");
    String warning = "Selecting a PinUP Popper screen will enable highscore card generation.<br/>Existing media will be overwritten for this screen!";
    String msg = "<html><body>" + warning + "</body></html>";
    JLabel warnLabel = WidgetFactory.createLabel(settingsPanel, msg, Color.RED);

    JLabel separator = new JLabel("");
    separator.setPreferredSize(new Dimension(1, 30));
    settingsPanel.add(separator, "wrap");

    WidgetFactory.createTableSelector(repository, settingsPanel, "Sample Table:", store, "card.sampleTable");


    /******************************** Generator Fields ****************************************************************/
    WidgetFactory.createFileChooser(settingsPanel, "Background Image:", "Select File", store, "card.background", "background4k.jpg");
    WidgetFactory.createTextField(settingsPanel, "Card Title:", store, "card.title.text", "Highscore");
    WidgetFactory.createFontSelector(settingsPanel, "Title Font:", store, "card.title.font");
    WidgetFactory.createFontSelector(settingsPanel, "Table Name Font:", store, "card.table.font");
    WidgetFactory.createFontSelector(settingsPanel, "Score Font:", store, "card.score.font");
    WidgetFactory.createColorChooser(settingsPanel, "Font Color:", store, "card.font.color");
    WidgetFactory.createSpinner(settingsPanel, "Padding Top:", store, "card.title.y.offset", 80);
    WidgetFactory.createSpinner(settingsPanel, "Padding Left:", store, "card.highscores.row.padding.left", 60);
    WidgetFactory.createSlider(settingsPanel, "Brighten Image:", store, "card.alphacomposite.white");
    WidgetFactory.createSlider(settingsPanel, "Darken Image:", store, "card.alphacomposite.black");
    WidgetFactory.createSlider(settingsPanel, "Border Size:", store, "card.border.width");


    settingsPanel.add(new JLabel(""));
    generateButton = new JButton("Generate Sample Card");
    generateButton.setEnabled(false);
    generateButton.setActionCommand("generateCard");
    generateButton.addActionListener(this.actionListener);

    JButton showActiveCardButton = new JButton("Show Active Card Image");
    showActiveCardButton.setActionCommand("showCard");
    showActiveCardButton.addActionListener(this.actionListener);

    settingsPanel.add(generateButton, "span 3");
    settingsPanel.add(showActiveCardButton);
    settingsPanel.add(new JLabel(""), "wrap");


    settingsPanel.add(new JLabel(""));
    generateAllButton = new JButton("Generate All Cards (!)");
    generateAllButton.setEnabled(false);
    generateAllButton.setActionCommand("generateAllCards");
    generateAllButton.addActionListener(this.actionListener);
    settingsPanel.add(generateAllButton, "span 3");
    settingsPanel.add(new JLabel(""), "wrap");


    String selection = (String) screenCombo.getSelectedItem();
    generateButton.setEnabled(!StringUtils.isEmpty(selection));
    generateAllButton.setEnabled(!StringUtils.isEmpty(selection));
    showActiveCardButton.setEnabled(!StringUtils.isEmpty(selection));
    warnLabel.setVisible(!StringUtils.isEmpty(selection));
    screenCombo.addActionListener(e -> {
      String s = (String) screenCombo.getSelectedItem();
      generateButton.setEnabled(!StringUtils.isEmpty(s));
      generateAllButton.setEnabled(!StringUtils.isEmpty(s));
      showActiveCardButton.setEnabled(!StringUtils.isEmpty(s));
      warnLabel.setVisible(!StringUtils.isEmpty(s));
    });

    /******************************** Preview *************************************************************************/


    JPanel previewPanel = new JPanel();
    previewPanel.setBackground(Color.BLACK);
    TitledBorder b = BorderFactory.createTitledBorder("Sample Preview");
    b.setTitleColor(Color.WHITE);
    previewPanel.setBorder(b);
    add(previewPanel, BorderLayout.CENTER);
    previewPanel.setLayout(new MigLayout("gap rel 8 insets 10", "left"));
    iconLabel = new JLabel(getPreviewImage());
    iconLabel.setBackground(Color.BLACK);
    previewPanel.add(iconLabel);
  }

  private ImageIcon getPreviewImage() {
    try {
      GameInfo gameInfo = this.getSampleGame();
      if (gameInfo != null && getScreen() != null) {
        File file = new File(SystemInfo.RESOURCES, Config.getCardGeneratorConfig().get("card.background"));
        File sampleFile = HighscoreCardGenerator.SAMPLE_FILE;
        if (sampleFile.exists()) {
          file = sampleFile;
        }
        BufferedImage image = ImageIO.read(file);
        int percentage = 48;
        Image newimg = image.getScaledInstance(image.getWidth() * percentage / 100, image.getHeight() * percentage / 100, Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newimg);  // transform it back
      }
    } catch (Exception e) {
      LOG.error("Error loading card preview: " + e.getMessage(), e);
    }
    return null;
  }

  private PopperScreen getScreen() {
    String screen = Config.getCardGeneratorConfig().getString("popper.screen");
    if (!StringUtils.isEmpty(screen)) {
      return PopperScreen.valueOf(screen);
    }
    return null;
  }

  GameInfo getSampleGame() {
    int gameId = Config.getCardGeneratorConfig().getInt("card.sampleTable");
    if (gameId > 0) {
      return repository.getGameInfo(gameId);
    }

    List<GameInfo> gameInfos = repository.getGameInfos();
    for (GameInfo gameInfo : gameInfos) {
      if (gameInfo.getHighscore() != null) {
        return gameInfo;
      }
    }
    return null;
  }

  public void generateSampleCard() {
    try {
      iconLabel.setVisible(false);
      generateButton.setEnabled(false);
      HighscoreCardGenerator.generateCard(getSampleGame(), getScreen(), HighscoreCardGenerator.SAMPLE_FILE);
      iconLabel.setIcon(getPreviewImage());
      generateButton.setEnabled(true);
      iconLabel.setVisible(true);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this.configWindow, "Error generating overlay: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void generateAllCards() {
    try {
      int warning = JOptionPane.showConfirmDialog(this.configWindow, "This will overwrite all existing media for screen '" + getScreen() + "'.\nContinue?", "Warning", JOptionPane.YES_NO_OPTION);
      if (warning == JOptionPane.OK_OPTION) {
        generateButton.setEnabled(false);
        List<GameInfo> gameInfos = repository.getGameInfos();
        for (GameInfo gameInfo : gameInfos) {
          if (gameInfo.getHighscore() != null) {
            HighscoreCardGenerator.generateCard(gameInfo, getScreen());
          }
        }
        generateButton.setEnabled(true);
      }

    } catch (Exception e) {
      JOptionPane.showMessageDialog(this.configWindow, "Error generating overlay: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void showGeneratedCard() {
    try {
      File file = HighscoreCardGenerator.SAMPLE_FILE;
      if (file.exists()) {
        Desktop.getDesktop().open(file);
      }
    } catch (IOException ex) {
      LOG.error("Failed to open card file: " + ex.getMessage(), ex);
    }
  }
}
