package de.mephisto.vpin.popper.overlay.util;

import de.mephisto.vpin.util.PropertiesStore;
import de.mephisto.vpin.util.SystemInfo;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.io.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Utility for accessing the different config files.
 */
public class Config {
  private final static Logger LOG = LoggerFactory.getLogger(Config.class);
  private final static String GENERATOR_CONFIG_4K_FILENAME = "4k-generator.properties";
  private final static String GENERATOR_CONFIG_2K_FILENAME = "2k-generator.properties";
  private final static String OVERLAY_CONFIG_FILENAME = "overlay.properties";
  private final static String CARd_CONFIG_FILENAME = "card-generator.properties";

  private static PropertiesStore generatorConfig;
  private static PropertiesStore overlayConfig;
  private static PropertiesStore cardConfig;

  public static PropertiesStore getCardGeneratorConfig() {
    if(cardConfig == null) {
      cardConfig = PropertiesStore.create(CARd_CONFIG_FILENAME);
    }
    return cardConfig;
  }

  public static PropertiesStore getOverlayGeneratorConfig() {
    if(generatorConfig == null) {
      if(SystemInfo.getInstance().getScreenSize().width > 3000) {
        generatorConfig = PropertiesStore.create(GENERATOR_CONFIG_4K_FILENAME);
      }
      else {
        generatorConfig = PropertiesStore.create(GENERATOR_CONFIG_2K_FILENAME);
      }
    }
    return generatorConfig;
  }

  public static PropertiesStore getOverlayConfig() {
    if(overlayConfig == null) {
      overlayConfig = PropertiesStore.create(OVERLAY_CONFIG_FILENAME);
    }
    return overlayConfig;
  }
}
