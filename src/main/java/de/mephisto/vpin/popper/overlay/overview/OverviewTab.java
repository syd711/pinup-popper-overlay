package de.mephisto.vpin.popper.overlay.overview;

import de.mephisto.vpin.games.GameRepository;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.popper.overlay.resources.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
    //Create and initialize the button.
    JButton button = new JButton();
    button.setActionCommand(actionCommand);
    button.setToolTipText(toolTipText);
    button.addActionListener(this);
    button.setIcon(new ImageIcon(ResourceLoader.getResource("logo-small.png")));

    return button;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    rescanTables();
  }

  private void rescanTables() {
    final JDialog progressDialog = new JDialog(ConfigWindow.getInstance(), "Progress Dialog", true);
    progressDialog.setSize(400, 200);
    progressDialog.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);
    gbc.gridx = 0;
    gbc.gridy = 0;

    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    progressDialog.add(progressBar, gbc);
    gbc.gridy++;

    JLabel jl = new JLabel("Progress...");
    progressDialog.add(jl, gbc);
    gbc.gridy++;

    progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setLocationRelativeTo(ConfigWindow.getInstance());

    ProgressWorker pw = new ProgressWorker();
    pw.addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if (name.equals("progress")) {
          int progress = (int) evt.getNewValue();
          progressBar.setValue(progress);
          jl.setText("Progress: " + progress);
          repaint();
        }
        else if (name.equals("state")) {
          SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
          switch (state) {
            case DONE:
              progressDialog.setVisible(false);
              break;
          }
        }
      }

    });
    pw.execute();

    progressDialog.setVisible(true);
  }

  public class ProgressWorker extends SwingWorker<Object, Object> {

    @Override
    protected Object doInBackground() throws Exception {

      for (int i = 0; i < 100; i++) {
        setProgress(i);
        try {
          Thread.sleep(25);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      return null;
    }
  }
}
