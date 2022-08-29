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
  private final static String CONFIG_FILENAME = "./resources/config.properties";

  private static Configuration configuration;

  public static Configuration getConfiguration() {
    if(configuration != null) {
      return configuration;
    }

    try {
      FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
          new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
              .configure(new Parameters().properties()
                  .setThrowExceptionOnMissing(true)
                  .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                  .setIncludesAllowed(false));

      FileBasedConfiguration config = builder.getConfiguration();
      FileHandler fileHandler = new FileHandler(config);
      InputStream resourceAsStream = new FileInputStream(CONFIG_FILENAME);
      fileHandler.load(resourceAsStream);
      resourceAsStream.close();

      configuration = config;
    } catch (Throwable e) {
      LOG.error("Error loading " + CONFIG_FILENAME + ": " + e.getMessage(), e);
    }
    return configuration;
  }

  private static File getConfigFile(String dir, String config) {
    return new File(dir + config);
  }
}
