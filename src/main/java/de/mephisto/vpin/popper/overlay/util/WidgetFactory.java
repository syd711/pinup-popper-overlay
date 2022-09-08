package de.mephisto.vpin.popper.overlay.util;

import de.mephisto.vpin.util.PropertiesStore;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class WidgetFactory {
  private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(WidgetFactory.class);

  public static void createTextField(JPanel parent, String title, PropertiesStore store, String property, String defaultValue) {
    parent.add(new JLabel(title));
    String value = store.getString(property, defaultValue);
    final JTextField field = new JTextField(value);
    field.setMinimumSize(new Dimension(300, 26));
    parent.add(field, "span 3");
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

  public static void createFontSelector(JPanel parent, String label, PropertiesStore store, String property) {
    parent.add(new JLabel(label));
    final JLabel titleFontLabel = new JLabel(store.getString(property + ".name"));
    parent.add(titleFontLabel);
    JButton fontChooserButton = new JButton("Choose Font");
    fontChooserButton.setActionCommand("titleFont");
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
        titleFontLabel.setText(font.getFamily());
        LOG.info("Selected Title Font : " + font);
      }
    });
    parent.add(fontChooserButton, "span 3");
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
    parent.add(new JLabel(label));
    final JColorChooser fontColorChooser = new JColorChooser();
    fontColorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Color color = fontColorChooser.getColor();
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        store.set(property, hex);
      }
    });
    fontColorChooser.setBackground(Color.WHITE);
    parent.add(fontColorChooser, "span 4");
    parent.add(new JLabel(""), "wrap");
  }
}
