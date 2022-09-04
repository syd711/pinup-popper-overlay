package de.mephisto.vpin.popper.overlay.overview;

import de.mephisto.vpin.games.GameInfo;
import de.mephisto.vpin.highscores.Highscore;
import de.mephisto.vpin.highscores.Score;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HighscoreDialog extends JDialog implements ActionListener {

  public HighscoreDialog(Frame owner, GameInfo gameInfo, String title) {
    super(owner);
    setTitle(title);
    setModal(true);
    setLocation(owner.getX() + 50, owner.getY() + 50);
    setSize(390, 400);
    this.setLayout(new BorderLayout());


    Font font = new Font("Tahoma", Font.BOLD, 16);

    JPanel top = new JPanel();
    top.setLayout(new MigLayout("gap rel 0", "grow"));
    Highscore highscore = gameInfo.getHighscore();
    JLabel label = new JLabel("Parsed Highscores:");
    label.setFont(font);
    top.add(label, "wrap");

    List<Score> scores = highscore.getScores();
    for (Score score : scores) {
      label = new JLabel(score.getPosition() + ".");
      top.add(label, "width 10:20:40");
      label = new JLabel(score.getUserInitials());
      top.add(label);
      label = new JLabel(score.getScore());
      top.add(label, "wrap");
    }

    this.add(top, BorderLayout.NORTH);

    JPanel root = new JPanel();
    root.setLayout(new MigLayout("gap rel 0", "grow"));
    label = new JLabel("Raw Value");
    label.setFont(font);
    root.add(label, "wrap");
    JTextArea textArea = new JTextArea(highscore.getRaw());
    textArea.setColumns(30);
    textArea.setRows(20);
    textArea.setLineWrap(true);
    textArea.setEditable(false);
    textArea.setWrapStyleWord(true);
    textArea.setSize(textArea.getPreferredSize().width, textArea.getPreferredSize().height);
    JScrollPane jScrollPane = new JScrollPane(textArea);

    root.add(jScrollPane);

    this.add(root, BorderLayout.CENTER);

    JToolBar toolBar = new JToolBar("Overview Toolbar");
    toolBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    toolBar.setLayout(new BorderLayout());
    toolBar.setFloatable(false);
    JButton close = new JButton("Close");
    close.addActionListener(this);
    toolBar.add(close, BorderLayout.EAST);
    this.add(toolBar, BorderLayout.SOUTH);

    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.setVisible(false);
  }
}
