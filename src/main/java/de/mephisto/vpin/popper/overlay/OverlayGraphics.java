package de.mephisto.vpin.popper.overlay;

import de.mephisto.vpin.commons.GameInfo;
import de.mephisto.vpin.commons.GameRepository;
import de.mephisto.vpin.highscores.Highscore;
import de.mephisto.vpin.highscores.HighsoreResolver;
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
import java.util.Collections;
import java.util.List;

public class OverlayGraphics {
  private final static int ROW_COUNT = Config.getConfiguration().getInt("ui.highscores.count");
  private final static int ROW_HEIGHT = Config.getConfiguration().getInt("ui.highscores.row.height");
  private final static int ROW_SEPARATOR = Config.getConfiguration().getInt("ui.highscores.row.separator");
  private final static int ROW_PADDING_LEFT = Config.getConfiguration().getInt("ui.highscores.row.padding.left");

  private final static String HIGHSCORE_TEXT = Config.getConfiguration().getString("ui.highscores.text");
  private final static String TITLE_TEXT = Config.getConfiguration().getString("ui.title.text");

  private final static String HIGHSCORE_FONT_FILE = Config.getConfiguration().getString("ui.highscore.font.file");
  private final static String HIGHSCORE_FONT_NAME = Config.getConfiguration().getString("ui.highscore.font.name");
  private final static int HIGHSCORE_TABLE_FONT_SIZE = Config.getConfiguration().getInt("ui.highscore.table.font.size");
  private final static int HIGHSCORE_OWNER_FONT_SIZE = Config.getConfiguration().getInt("ui.highscore.owner.font.size");

  private final static String TITLE_FONT_FILE = Config.getConfiguration().getString("ui.title.font.file");
  private final static String TITLE_FONT_NAME = Config.getConfiguration().getString("ui.title.font.name");
  private final static int TITLE_Y_OFFSET = Config.getConfiguration().getInt("ui.title.y.offset");
  private final static int TITLE_FONT_SIZE = Config.getConfiguration().getInt("ui.title.font.size");


  private final static Logger LOG = LoggerFactory.getLogger(OverlayGenerator.class);

  public static void drawGames(BufferedImage image, GameRepository gameRepository, HighsoreResolver highsoreResolver) throws Exception {
    List<GameInfo> gameInfos = gameRepository.getGameInfos();
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Font scoreFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("./resources/" + HIGHSCORE_FONT_FILE));
    ge.registerFont(scoreFont);
    Font textFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("./resources/" + TITLE_FONT_FILE));
    ge.registerFont(textFont);

    Graphics g = image.getGraphics();
    g.setColor(Color.WHITE);
    g.setFont(new Font(TITLE_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE));


    GameInfo gameOfTheMonth = gameInfos.get(1);

    renderTableOfTheMonth(highsoreResolver, imageWidth, g, gameOfTheMonth);
    renderHighscoreList(imageWidth, imageHeight, gameOfTheMonth, gameRepository, highsoreResolver, g);
  }

  /**
   * The upper section, usually with the three topscores.
   *
   * @param highsoreResolver
   * @param imageWidth
   * @param g
   * @param gameOfTheMonth
   * @throws Exception
   */
  private static void renderTableOfTheMonth(HighsoreResolver highsoreResolver, int imageWidth, Graphics g, GameInfo gameOfTheMonth) throws Exception {
    String title = TITLE_TEXT;
    int titleWidth = g.getFontMetrics().stringWidth(title);
    int titleY = ROW_SEPARATOR + TITLE_FONT_SIZE + TITLE_Y_OFFSET;
    g.drawString(title, imageWidth / 2 - titleWidth / 2, titleY);

    g.setFont(new Font(HIGHSCORE_FONT_NAME, Font.BOLD, HIGHSCORE_TABLE_FONT_SIZE));
    String tableOfTheMonth = gameOfTheMonth.getGameDisplayName();
    int width = g.getFontMetrics().stringWidth(tableOfTheMonth);
    int tableNameY = titleY + ROW_SEPARATOR + TITLE_FONT_SIZE;
    g.drawString(tableOfTheMonth, imageWidth / 2 - width / 2, tableNameY);

    g.setFont(new Font(HIGHSCORE_FONT_NAME, Font.PLAIN, HIGHSCORE_TABLE_FONT_SIZE));
    Highscore highscore = highsoreResolver.getHighscore(gameOfTheMonth);
    if (highscore != null) {
      int count = 0;
      int scoreWidth = 0;

      List<String> scores = new ArrayList<>();
      for (Score score : highscore.getScores()) {
        String scoreString = score.getPosition() + ". " + score.getUserInitials() + " " + score.getScore();
        scores.add(scoreString);

        int singleScoreWidth = g.getFontMetrics().stringWidth(title);
        if(scoreWidth < singleScoreWidth ) {
          scoreWidth = singleScoreWidth;
        }
        count++;
        if (count == 3) {
          break;
        }
      }

      int position = 0;
      int wheelWidth = 3 * TITLE_FONT_SIZE + 3* ROW_SEPARATOR;
      int totalScoreAndWheelWidth = scoreWidth + wheelWidth;

      for (String score : scores) {
        position++;
        int scoreY = tableNameY + position * TITLE_FONT_SIZE + ROW_SEPARATOR;
        g.drawString(score, imageWidth / 2 - totalScoreAndWheelWidth / 2 + wheelWidth + ROW_SEPARATOR, scoreY);
      }

      File wheelIconFile = gameOfTheMonth.getWheelIconFile();
      if (wheelIconFile.exists()) {
        BufferedImage wheelImage = ImageIO.read(wheelIconFile);
        g.drawImage(wheelImage, imageWidth / 2 - totalScoreAndWheelWidth / 2, tableNameY + ROW_SEPARATOR, wheelWidth, wheelWidth, null);
      }
    }
  }

  private static void renderHighscoreList(int imageWidth, int imageHeight, GameInfo gameOfTheMonth, GameRepository gameRepository, HighsoreResolver highsoreResolver, Graphics g) throws Exception {
    g.setFont(new Font(TITLE_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE));
    String text = HIGHSCORE_TEXT;
    int highscoreTextWidth = g.getFontMetrics().stringWidth(text);

    int highscoreTitleY = imageHeight - ((ROW_COUNT * ROW_HEIGHT) + (ROW_COUNT * ROW_SEPARATOR) + TITLE_FONT_SIZE + (TITLE_FONT_SIZE / 2));
    g.drawString(text, imageWidth / 2 - highscoreTextWidth / 2, highscoreTitleY);

    int tableIndex = 1;
    int yStart = highscoreTitleY + ROW_SEPARATOR + TITLE_FONT_SIZE / 2;

    List<GameInfo> gameInfos = gameRepository.getGameInfos();
    gameInfos.sort((o1, o2) -> (int) (o2.getLastModified() - o1.getLastModified()));

    for (GameInfo game : gameInfos) {
      Highscore highscore = highsoreResolver.getHighscore(game);
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
      g.drawString(highscore.getUserInitials() + " " + highscore.getScore(), ROW_HEIGHT + (ROW_PADDING_LEFT * 2), yStart + HIGHSCORE_OWNER_FONT_SIZE + ((ROW_HEIGHT - HIGHSCORE_OWNER_FONT_SIZE) / 2) + HIGHSCORE_OWNER_FONT_SIZE/2);

      yStart = yStart + ROW_HEIGHT + ROW_SEPARATOR;
      tableIndex++;
      if (tableIndex > ROW_COUNT) {
        break;
      }
    }
  }
}
