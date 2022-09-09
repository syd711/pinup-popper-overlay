package de.mephisto.vpin.popper.overlay.util;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.util.PropertiesStore;
import de.mephisto.vpin.util.SystemInfo;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class WidgetFactory {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(WidgetFactory.class);

  public static void createTableSelector(GameRepository repository, JPanel parent, String title, PropertiesStore store, String property) {
    List<GameInfo> gameInfos = repository.getActiveGameInfos();
    Vector<GameInfo> data = new Vector<>(gameInfos);
    data.insertElementAt(null, 0);
    final JComboBox tableSelection = new JComboBox(data);
    tableSelection.addActionListener(e -> {
      GameInfo selectedItem = (GameInfo) tableSelection.getSelectedItem();
      String value = "";
      if (selectedItem != null) {
        value = String.valueOf(selectedItem.getId());
      }
      store.set(property, value);
    });
    int selection = store.getInt(property);
    if (selection > 0) {
      GameInfo gameInfo = repository.getGameInfo(selection);
      tableSelection.setSelectedItem(gameInfo);
    }

    parent.add(new JLabel(title));
    parent.add(tableSelection, "span 3");
    parent.add(new JLabel(""), "width 30:200:200");
    parent.add(new JLabel(""), "wrap");
  }

  public static void createTextField(JPanel parent, String title, PropertiesStore store, String property, String defaultValue) {
    parent.add(new JLabel(title));
    String value = store.getString(property, defaultValue);
    final JTextField field = new JTextField(value);
    field.setMinimumSize(new Dimension(300, 26));
    parent.add(field, "span 4");
    parent.add(new JLabel(""), "wrap");
    field.addActionListener(e -> {
      String text = field.getText();
      store.set(property, text);
    });
  }

  public static void createSpinner(JPanel parent, String title, PropertiesStore store, String property, int defaultValue) {
    parent.add(new JLabel(title));
    int value = store.getInt(property, defaultValue);
    final JSpinner field = new JSpinner();
    field.setValue(value);
    field.setMinimumSize(new Dimension(50, 26));
    parent.add(field, "span 3");
    parent.add(new JLabel(""), "wrap");
    field.addChangeListener(e -> {
      int fieldValue = (int) field.getValue();
      store.set(property, fieldValue);
    });
  }

  public static void createFileChooser(JPanel parent, String label, String buttonLabel, PropertiesStore store, String property, String defaultValue) {
    parent.add(new JLabel(label));
    String value = store.getString(property, defaultValue);
    parent.add(new JLabel(value), "span 2");
    JButton openButton = new JButton(buttonLabel);
    parent.add(openButton, "span 1");
    parent.add(new JLabel(""), "wrap");
    openButton.addActionListener(e -> {
      final JFileChooser field = new JFileChooser();
      field.setCurrentDirectory(new File("./"));
      field.setFileFilter(new FileFilter() {
        @Override
        public boolean accept(File f) {
          return f.isDirectory() || f.getName().endsWith("png") || f.getName().endsWith(".jpg") || f.getName().endsWith(".jpeg");
        }

        @Override
        public String getDescription() {
          return "Pictures";
        }
      });
      int returnCode = field.showOpenDialog(parent);
      if (returnCode == JFileChooser.APPROVE_OPTION) {
        File selectedFile = field.getSelectedFile();
        String name = selectedFile.getName();
        File target = new File(SystemInfo.RESOURCES, name);
        Path normalizedSource = Paths.get(selectedFile.getAbsolutePath()).normalize();
        Path normalizedTarget = Paths.get(target.getAbsolutePath()).normalize();
        if (!normalizedSource.toString().equals(normalizedTarget.toString())) {
          if (target.exists()) {
            target.delete();
          }
          try {
            FileUtils.copyFile(selectedFile, target);
          } catch (IOException ex) {
            LOG.error("Error selecting file: " + ex.getMessage(), ex);
          }
        }

        store.set(property, field.getSelectedFile().getName());
      }
    });
  }

  public static void createFontSelector(JPanel parent, String label, PropertiesStore store, String property) {
    parent.add(new JLabel(label));
    String name = store.getString(property + ".name");
    int size = store.getInt(property + ".size");
    final JLabel titleFontLabel = new JLabel(name + " / " + size);
    parent.add(titleFontLabel,"span 3");
    JButton fontChooserButton = new JButton("Choose Font");
    fontChooserButton.addActionListener(e -> {
      JFontChooser fontChooser = new JFontChooser();
      fontChooser.setSelectedFontSize(store.getInt(property + ".size", 48));
      fontChooser.setSelectedFontFamily(store.getString(property + ".name", "Arial"));
      fontChooser.setSelectedFontStyle(store.getInt(property + ".font.style", 0));
      int result = fontChooser.showDialog(parent);
      if (result == JFontChooser.OK_OPTION) {
        Font font = fontChooser.getSelectedFont();
        store.set(property + ".size", font.getSize());
        store.set(property + ".name", font.getFamily());
        store.set(property + ".style", font.getStyle());
        titleFontLabel.setText(font.getFamily() + " / " + font.getSize());
        LOG.info("Selected Title Font : " + font);
      }
    });
    parent.add(fontChooserButton);
    parent.add(new JLabel(""), "wrap");
  }

  public static void createSlider(JPanel parent, String title, PropertiesStore store, String property) {
    parent.add(new JLabel(title));
    int value = store.getInt(property);
    JSlider slider = new JSlider(0, 100, value);
    slider.setBackground(Color.WHITE);
    slider.setMajorTickSpacing(50);
    slider.setMinorTickSpacing(1);
    slider.setPaintLabels(true);
    parent.add(slider, "span 3");
    parent.add(new JLabel(""), "wrap");
    slider.addChangeListener(e -> {
      int s = slider.getValue();
      store.set(property, s);
    });
  }

  public static void createColorChooser(JPanel parent, String label, PropertiesStore store, String property) {
    String value = store.getString(property, "#FFFFFF");

    parent.add(new JLabel(label));
    final JColorChooser fontColorChooser = new JColorChooser();
    fontColorChooser.setLocale(Locale.ENGLISH);
    fontColorChooser.setColor(Color.decode(value));
    fontColorChooser.getSelectionModel().addChangeListener(e -> {
      Color color = fontColorChooser.getColor();
      String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
      store.set(property, hex);
    });
    fontColorChooser.setBackground(Color.WHITE);
    parent.add(fontColorChooser, "span 4");
    parent.add(new JLabel(""), "wrap");
  }
}
