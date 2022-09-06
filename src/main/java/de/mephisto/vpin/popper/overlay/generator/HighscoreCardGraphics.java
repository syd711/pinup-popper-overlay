package de.mephisto.vpin.popper.overlay.generator;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.highscores.Highscore;
import de.mephisto.vpin.highscores.Score;
import de.mephisto.vpin.popper.overlay.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighscoreCardGraphics extends VPinGraphics {
  private final static Logger LOG = LoggerFactory.getLogger(HighscoreCardGraphics.class);

  private final static int ROW_HEIGHT = Config.getOverlayGeneratorConfig().getInt("overlay.highscores.row.height");
  private final static int ROW_SEPARATOR = Config.getOverlayGeneratorConfig().getInt("overlay.highscores.row.separator");
  private final static int ROW_PADDING_LEFT = Config.getOverlayGeneratorConfig().getInt("overlay.highscores.row.padding.left");

  private final static String HIGHSCORE_TEXT = Config.getOverlayGeneratorConfig().getString("overlay.highscores.text");
  private final static String TITLE_TEXT = Config.getOverlayGeneratorConfig().getString("overlay.title.text");

  private final static String HIGHSCORE_FONT_FILE = Config.getOverlayGeneratorConfig().getString("overlay.highscore.font.file");
  private final static String HIGHSCORE_FONT_NAME = Config.getOverlayGeneratorConfig().getString("overlay.highscore.font.name");
  private final static int HIGHSCORE_TABLE_FONT_SIZE = Config.getOverlayGeneratorConfig().getInt("overlay.highscore.table.font.size");
  private final static int HIGHSCORE_OWNER_FONT_SIZE = Config.getOverlayGeneratorConfig().getInt("overlay.highscore.owner.font.size");

  private final static String TITLE_FONT_FILE = Config.getOverlayGeneratorConfig().getString("overlay.title.font.file");
  private final static String TITLE_FONT_NAME = Config.getOverlayGeneratorConfig().getString("overlay.title.font.name");
  private final static int TITLE_Y_OFFSET = Config.getOverlayGeneratorConfig().getInt("overlay.title.y.offset");
  private final static int TITLE_FONT_SIZE = Config.getOverlayGeneratorConfig().getInt("overlay.title.font.size");


  public static final String RESOURCES = "./resources/";

  public static void drawHighscores(BufferedImage image, GameInfo game) throws Exception {
    Font scoreFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(RESOURCES + HIGHSCORE_FONT_FILE));
    Font textFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(RESOURCES + TITLE_FONT_FILE));

    registerFonts(Arrays.asList(scoreFont, textFont));
    setRendingHints(image);
    setDefaultColor(image, Config.getOverlayGeneratorConfig().getString("card.font.color"));

    float alphaWhite = Config.getOverlayGeneratorConfig().getFloat("card.alphacomposite.white");
    float alphaBlack = Config.getOverlayGeneratorConfig().getFloat("card.alphacomposite.black");
    applyAlphaComposites(image, alphaWhite, alphaBlack);
    renderTableChallenge(image, game);
  }

  /**
   * The upper section, usually with the three topscores.
   */
  private static void renderTableChallenge(BufferedImage image, GameInfo challengedGame) throws Exception {
    int highscoreListYOffset = TITLE_Y_OFFSET + TITLE_FONT_SIZE;
    Highscore highscore = challengedGame.getHighscore(true);
    int returnOffset = highscoreListYOffset;
    if (highscore != null) {
      Graphics g = image.getGraphics();
      int imageWidth = image.getWidth();

      g.setFont(new Font(TITLE_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE));

      String title = TITLE_TEXT;
      int titleWidth = g.getFontMetrics().stringWidth(title);
      int titleY = ROW_SEPARATOR + TITLE_FONT_SIZE + TITLE_Y_OFFSET;
      g.drawString(title, imageWidth / 2 - titleWidth / 2, titleY);

      g.setFont(new Font(HIGHSCORE_FONT_NAME, Font.BOLD, HIGHSCORE_TABLE_FONT_SIZE));
      String challengedTable = challengedGame.getGameDisplayName();
      int width = g.getFontMetrics().stringWidth(challengedTable);
      int tableNameY = titleY + ROW_SEPARATOR + TITLE_FONT_SIZE;
      g.drawString(challengedTable, imageWidth / 2 - width / 2, tableNameY);

      g.setFont(new Font(HIGHSCORE_FONT_NAME, Font.PLAIN, HIGHSCORE_TABLE_FONT_SIZE));

      int count = 0;
      int scoreWidth = 0;

      List<String> scores = new ArrayList<>();
      for (Score score : highscore.getScores()) {
        String scoreString = score.getPosition() + ". " + score.getUserInitials() + " " + score.getScore();
        scores.add(scoreString);

        int singleScoreWidth = g.getFontMetrics().stringWidth(title);
        if (scoreWidth < singleScoreWidth) {
          scoreWidth = singleScoreWidth;
        }
        count++;
        if (count == 3) {
          break;
        }
      }

      int position = 0;
      int wheelWidth = 3 * TITLE_FONT_SIZE + 3 * ROW_SEPARATOR;
      int totalScoreAndWheelWidth = scoreWidth + wheelWidth;

      for (String score : scores) {
        position++;
        int scoreY = tableNameY + position * TITLE_FONT_SIZE + ROW_SEPARATOR;
        g.drawString(score, imageWidth / 2 - totalScoreAndWheelWidth / 2 + wheelWidth + ROW_SEPARATOR, scoreY);
      }

      File wheelIconFile = challengedGame.getWheelIconFile();
      int wheelY = tableNameY + ROW_SEPARATOR;
      returnOffset = wheelY * 2 + HIGHSCORE_TABLE_FONT_SIZE * 2;
      if (wheelIconFile.exists()) {
        BufferedImage wheelImage = ImageIO.read(wheelIconFile);
        g.drawImage(wheelImage, imageWidth / 2 - totalScoreAndWheelWidth / 2, wheelY, wheelWidth, wheelWidth, null);
      }
    }
  }
}
