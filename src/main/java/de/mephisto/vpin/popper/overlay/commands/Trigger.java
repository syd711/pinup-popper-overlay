package de.mephisto.vpin.popper.overlay.commands;

public class Trigger {

  public static final String TABLE_START = "table is started";
  public static final String TABLE_START_VALUE = "tableStart";
  public static final String TABLE_EXIT = "table is quit";
  public static final String TABLE_EXIT_VALUE = "tableExit";
  public static final String SYSTEM_START = "system is started";
  public static final String SYSTEM_START_VALUE = "systemStart";

  public static final String KEY_PRESSED = "key is pressed";
  public static final String KEY_PRESSED_VALUE = "keyPressed";

  private final String value;
  private final String label;

  public Trigger(String value, String label) {
    this.value = value;
    this.label = label;
  }

  public String getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return this.getLabel();
  }
}
