package de.mephisto.vpin.popper.overlay.util;

import de.mephisto.vpin.util.PropertiesStore;
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
  private final static String GENERATOR_CONFIG_FILENAME = "4k-generator.properties";
  private final static String OVERLAY_CONFIG_FILENAME = "overlay.properties";

  private static PropertiesStore generatorConfig;
  private static PropertiesStore overlayConfig;

  public static PropertiesStore getGeneratorConfig() {
    if(generatorConfig == null) {
      generatorConfig = PropertiesStore.create(GENERATOR_CONFIG_FILENAME);
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
