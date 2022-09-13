package de.mephisto.vpin.popper.overlay.commands;

import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.dof.Unit;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.popper.overlay.util.Keys;
import de.mephisto.vpin.popper.overlay.util.WidgetFactory;
import de.mephisto.vpin.util.PropertiesStore;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

public class RuleDialog extends JDialog {

  private static final java.util.List<String> OUTPUTS = new ArrayList<>();
  private static final java.util.List<String> VALUES = new ArrayList<>();
  private static final java.util.List<Trigger> TRIGGERS = new ArrayList<>();

  static {
    for (int i = 1; i < 129; i++) {
      OUTPUTS.add(String.valueOf(i));
    }
    for (int i = 0; i < 256; i++) {
      VALUES.add(String.valueOf(i));
    }

    TRIGGERS.add(new Trigger(Trigger.SYSTEM_START_VALUE, Trigger.SYSTEM_START));
    TRIGGERS.add(new Trigger(Trigger.TABLE_START_VALUE, Trigger.TABLE_START));
    TRIGGERS.add(new Trigger(Trigger.TABLE_EXIT_VALUE, Trigger.TABLE_EXIT));
    TRIGGERS.add(new Trigger(Trigger.KEY_PRESSED_VALUE, Trigger.KEY_PRESSED));
  }

  private final VPinService service;
  private final JPanel keySelectionPanel;
  private final JSpinner timeSpinner;
  private final JCheckBox toggleBtnCheckbox;
  private final JComboBox triggerCombo;

  public RuleDialog(ConfigWindow configWindow, VPinService service) {
    super(configWindow);
    this.service = service;

    this.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    this.setLayout(new BorderLayout());
    this.setModal(true);
    this.setSize(370, 330);
    this.setTitle("DOF Rule");
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    setLocation(x, y);


    JPanel rootPanel = new JPanel();
    rootPanel.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    rootPanel.setLayout(new MigLayout("gap rel 8 insets 10", "left", "center"));
    this.add(rootPanel, BorderLayout.CENTER);


    String key = "rule.1";
    PropertiesStore store = Config.getCommandConfig();

    addBoardCombo(rootPanel, key + ".board", store);

    WidgetFactory.createCombobox(rootPanel, OUTPUTS, "set output number", store, key + ".output");

    WidgetFactory.createCombobox(rootPanel, VALUES, "to value", store, key + ".value");

    timeSpinner = WidgetFactory.createSpinner(rootPanel, "for", "ms", store, key + ".duration", 0);

    triggerCombo = addTriggerCombo(rootPanel, key + ".trigger", store);
    triggerCombo.addActionListener(e -> this.updateViewState());

    toggleBtnCheckbox = WidgetFactory.createCheckbox(rootPanel, "Toggle Button", store, key + ".toggle");
    toggleBtnCheckbox.addActionListener(e -> this.updateViewState());

    keySelectionPanel = addKeySelection(rootPanel, key + ".key", store);

    this.updateViewState();

    JToolBar tb = new JToolBar();
    tb.setLayout(new FlowLayout(FlowLayout.RIGHT));
    tb.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 8));
    tb.setFloatable(false);
    JButton save = new JButton("Save");
    save.setMinimumSize(new Dimension(60, 30));
    save.addActionListener(e -> setVisible(false));
    tb.add(save);
    JButton close = new JButton("Cancel");
    close.setMinimumSize(new Dimension(60, 30));
    close.addActionListener(e -> setVisible(false));
    tb.add(close);


    this.add(tb, BorderLayout.SOUTH);

    this.setVisible(true);
  }

  private void updateViewState() {
    Trigger selectedItem = (Trigger) triggerCombo.getSelectedItem();
    boolean keyTrigger = selectedItem.getValue().equals(Trigger.KEY_PRESSED_VALUE);

    keySelectionPanel.setVisible(keyTrigger);
    toggleBtnCheckbox.setVisible(keyTrigger);
    timeSpinner.setEnabled(!keyTrigger);
  }

  private JComboBox addTriggerCombo(JPanel rootPanel, String key, PropertiesStore store) {
    String property = key + ".trigger";
    Vector<Trigger> data = new Vector<>(TRIGGERS);
    final JComboBox triggerTypeSelector = new JComboBox(data);
    triggerTypeSelector.addActionListener(e -> {
      Trigger selectedItem = (Trigger) triggerTypeSelector.getSelectedItem();
      String value = "";
      if (selectedItem != null) {
        value = String.valueOf(selectedItem.getValue());
      }
      store.set(property, value);
      updateViewState();
    });
    String selection = store.getString(property);
    if (!StringUtils.isEmpty(selection)) {
      triggerTypeSelector.setSelectedItem(selection);
    }
    rootPanel.add(new JLabel("when"));
    rootPanel.add(triggerTypeSelector, "span 3");
    rootPanel.add(new JLabel(""));
    rootPanel.add(new JLabel(""), "wrap");
    return triggerTypeSelector;
  }

  private JPanel addKeySelection(JPanel rootPanel, String pKey, PropertiesStore store) {
    JPanel panel = new JPanel();
    panel.setBackground(ConfigWindow.DEFAULT_BG_COLOR);
    Vector<String> modifierNames = new Vector<>(Keys.getModifierNames());
    modifierNames.insertElementAt(null, 0);
    JComboBox modifierCombo = new JComboBox(new DefaultComboBoxModel(modifierNames));
    modifierCombo.setActionCommand("modifierCombo");
    modifierCombo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });

    JComboBox keyCombo = new JComboBox(new DefaultComboBoxModel(new Vector(Keys.getKeyNames())));
    keyCombo.setActionCommand("keyCombo");
    keyCombo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });


    String hotkey = Config.getOverlayGeneratorConfig().get("overlay.hotkey");
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

    panel.add(modifierCombo);
    panel.add(new JLabel("+"));
    panel.add(keyCombo);

    rootPanel.add(new JLabel(""));
    rootPanel.add(panel, "span 3");
    rootPanel.add(new JLabel(""));
    rootPanel.add(new JLabel(""), "wrap");

    return panel;
  }

  private void addBoardCombo(JPanel rootPanel, String key, PropertiesStore store) {
    String property = key + ".trigger";
    Vector<Unit> data = new Vector<>(service.getUnits());
    final JComboBox boardSelector = new JComboBox(data);
    boardSelector.addActionListener(e -> {
      Unit selectedItem = (Unit) boardSelector.getSelectedItem();
      String value = "";
      if (selectedItem != null) {
        value = String.valueOf(selectedItem.getId());
      }
      store.set(property, value);
    });
    String selection = store.getString(property);
    if (!StringUtils.isEmpty(selection)) {
      boardSelector.setSelectedItem(selection);
    }
    rootPanel.add(new JLabel("From board"));
    rootPanel.add(boardSelector, "span 3");
    rootPanel.add(new JLabel(""));
    rootPanel.add(new JLabel(""), "wrap");
  }
}
