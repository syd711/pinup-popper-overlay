package de.mephisto.vpin.popper.overlay.util;

import de.mephisto.vpin.util.PropertiesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for accessing the different config files.
 */
public class Config {
  private final static String GENERATOR_CONFIG_FILENAME = "overlay-generator.properties";
  private final static String CARd_CONFIG_FILENAME = "card-generator.properties";

  private static PropertiesStore generatorConfig;
  private static PropertiesStore cardConfig;

  public static PropertiesStore getCardGeneratorConfig() {
    if (cardConfig == null) {
      cardConfig = PropertiesStore.create(CARd_CONFIG_FILENAME);
    }
    return cardConfig;
  }

  public static PropertiesStore getOverlayGeneratorConfig() {
    if (generatorConfig == null) {
      generatorConfig = PropertiesStore.create(GENERATOR_CONFIG_FILENAME);
    }
    return generatorConfig;
  }
}
