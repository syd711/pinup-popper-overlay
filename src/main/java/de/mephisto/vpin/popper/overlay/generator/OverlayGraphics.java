package de.mephisto.vpin.popper.overlay.generator;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.highscores.Highscore;
import de.mephisto.vpin.highscores.Score;
import de.mephisto.vpin.popper.overlay.util.Config;
import de.mephisto.vpin.util.SystemInfo;
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

public class OverlayGraphics extends VPinGraphics {
  private final static Logger LOG = LoggerFactory.getLogger(OverlayGraphics.class);

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


  public static void drawGames(BufferedImage image, GameRepository gameRepository, GameInfo gameOfTheMonth) throws Exception {
    Font scoreFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(SystemInfo.RESOURCES + HIGHSCORE_FONT_FILE));
    Font textFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(SystemInfo.RESOURCES + TITLE_FONT_FILE));

    registerFonts(Arrays.asList(scoreFont, textFont));
    setRendingHints(image);
    setDefaultColor(image, Config.getOverlayGeneratorConfig().getString("overlay.font.color"));

    float alphaWhite = Config.getOverlayGeneratorConfig().getFloat("overlay.alphacomposite.white");
    float alphaBlack = Config.getOverlayGeneratorConfig().getFloat("overlay.alphacomposite.black");
    applyAlphaComposites(image, alphaWhite, alphaBlack);

    int highscoreListYOffset = TITLE_Y_OFFSET + TITLE_FONT_SIZE;
    if (gameOfTheMonth != null) {
      highscoreListYOffset = renderTableChallenge(image, gameOfTheMonth, highscoreListYOffset);
    }
    renderHighscoreList(image, gameOfTheMonth, gameRepository, highscoreListYOffset);
  }

  /**
   * The upper section, usually with the three topscores.
   */
  private static int renderTableChallenge(BufferedImage image, GameInfo challengedGame, int highscoreListYOffset) throws Exception {
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
    return returnOffset;
  }

  private static void renderHighscoreList(BufferedImage image, GameInfo gameOfTheMonth, GameRepository gameRepository, int highscoreListYOffset) throws Exception {
    Graphics g = image.getGraphics();
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();

    g.setFont(new Font(TITLE_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE));
    String text = HIGHSCORE_TEXT;
    int highscoreTextWidth = g.getFontMetrics().stringWidth(text);

    g.drawString(text, imageWidth / 2 - highscoreTextWidth / 2, highscoreListYOffset);

    int tableIndex = 1;
    int yStart = highscoreListYOffset + ROW_SEPARATOR + TITLE_FONT_SIZE / 2;

    List<GameInfo> gameInfos = gameRepository.getGameInfos();
    gameInfos.sort((o1, o2) -> (int) (o2.getLastPlayed().getTime() - o1.getLastPlayed().getTime()));

    for (GameInfo game : gameInfos) {
      Highscore highscore = game.getHighscore(true);
      if (highscore == null) {
        LOG.info("Skipped highscore rendering of " + game.getGameDisplayName() + ", no highscore info found");
        continue;
      }

      if (gameOfTheMonth != null && gameOfTheMonth.getGameDisplayName().equals(game.getGameDisplayName())) {
        continue;
      }

      File wheelIconFile = game.getWheelIconFile();
      if (wheelIconFile.exists()) {
        BufferedImage wheelImage = ImageIO.read(wheelIconFile);
        g.drawImage(wheelImage, ROW_PADDING_LEFT, yStart, ROW_HEIGHT, ROW_HEIGHT, null);
      }

      g.setFont(new Font(HIGHSCORE_FONT_NAME, Font.PLAIN, HIGHSCORE_TABLE_FONT_SIZE));
      g.drawString(game.getGameDisplayName(), ROW_HEIGHT + (ROW_PADDING_LEFT * 2), yStart + HIGHSCORE_TABLE_FONT_SIZE);

      g.setFont(new Font(HIGHSCORE_FONT_NAME, Font.PLAIN, HIGHSCORE_OWNER_FONT_SIZE));
      g.drawString(highscore.getUserInitials() + " " + highscore.getScore(), ROW_HEIGHT + (ROW_PADDING_LEFT * 2), yStart + HIGHSCORE_OWNER_FONT_SIZE + ((ROW_HEIGHT - HIGHSCORE_OWNER_FONT_SIZE) / 2) + HIGHSCORE_OWNER_FONT_SIZE / 2);

      yStart = yStart + ROW_HEIGHT + ROW_SEPARATOR;
      tableIndex++;
      if (!isRemainingSpaceAvailable(imageHeight, yStart)) {
        break;
      }
    }
  }

  private static boolean isRemainingSpaceAvailable(int imageHeight, int positionY) {
    int remaining = imageHeight - positionY;
    return remaining > (ROW_HEIGHT + ROW_SEPARATOR + TITLE_Y_OFFSET);
  }
}
