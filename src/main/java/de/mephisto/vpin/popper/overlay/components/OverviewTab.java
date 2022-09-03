package de.mephisto.vpin.popper.overlay.components;

import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class OverviewTab extends JPanel implements ActionListener {

  public OverviewTab(GameRepository repository) {
    super(new BorderLayout());

    JToolBar toolBar = new JToolBar("Still draggable");
    this.addButtons(toolBar);
    this.add(toolBar, BorderLayout.NORTH);

    // Initializing the JTable
    JTable j = new GamesTable(new GameTableModel(repository), new GameTableColumnModel());
    // adding it to JScrollPane
    JScrollPane sp = new JScrollPane(j);
    this.add(sp, BorderLayout.CENTER);
  }

  private void addButtons(JToolBar toolBar) {
    JButton button = null;

    //first button
    button = makeNavigationButton("Back24", "previ",
        "Back to previous something-or-other",
        "Previous");
    toolBar.add(button);

  }

  private JButton makeNavigationButton(String imageName,
                                         String actionCommand,
                                         String toolTipText,
                                         String altText) {
    String imageURL = ResourceLoader.getResource("logo.png");
    Icon icon = UIManager.getIcon("FileView.directoryIcon");

    //Create and initialize the button.
    JButton button = new JButton();
    button.setActionCommand(actionCommand);
    button.setToolTipText(toolTipText);
    button.addActionListener(this);

    if (imageURL != null) {                      //image found
      button.setIcon(icon);
    } else {                                     //no image found
      button.setText(altText);
      System.err.println("Resource not found: " + imageURL);
    }

    return button;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
