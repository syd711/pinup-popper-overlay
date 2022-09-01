package de.mephisto.vpin.popper.overlay.util;

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
  private final static String GENERATOR_CONFIG_FILENAME = "./resources/generator.properties";
  private final static String OVERLAY_CONFIG_FILENAME = "./resources/overlay.properties";

  private static Configuration generatorConfig;
  private static Configuration overlayConfig;

  public static Configuration getGeneratorConfig() {
    if(generatorConfig == null) {
      generatorConfig = Config.create(new File(GENERATOR_CONFIG_FILENAME));
    }
    return generatorConfig;
  }

  public static Configuration getOverlayConfig() {
    if(overlayConfig == null) {
      overlayConfig = Config.create(new File(OVERLAY_CONFIG_FILENAME));
    }
    return overlayConfig;
  }

  public static Configuration create(File file) {
    try {
      FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
          new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
              .configure(new Parameters().properties()
                  .setThrowExceptionOnMissing(true)
                  .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                  .setIncludesAllowed(false));

      FileBasedConfiguration config = builder.getConfiguration();
      FileHandler fileHandler = new FileHandler(config);
      InputStream resourceAsStream = new FileInputStream(file);
      fileHandler.load(resourceAsStream);
      resourceAsStream.close();

      return config;
    } catch (Throwable e) {
      LOG.error("Error loading " + file.getAbsolutePath() + ": " + e.getMessage(), e);
    }
    return null;
  }
}
