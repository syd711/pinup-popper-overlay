package de.mephisto.vpin.popper.overlay.table;

import de.mephisto.vpin.GameInfo;
import de.mephisto.vpin.VPinService;
import de.mephisto.vpin.popper.overlay.ConfigWindow;
import de.mephisto.vpin.roms.RomScanListener;
import de.mephisto.vpin.roms.RomScannedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TablesTabActionListener implements ActionListener {
  private final static Logger LOG = LoggerFactory.getLogger(TablesTabActionListener.class);

  private final VPinService service;
  private final TablesTab tablesTab;
  private ProgressWorker progressWorker;
  private JLabel statusLabel;

  public TablesTabActionListener(VPinService service, TablesTab tablesTab) {
    this.service = service;
    this.tablesTab = tablesTab;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("tableRescan")) {
      GameInfo selection = tablesTab.getGamesTable().getSelection();
      if(selection != null) {
        tablesTab.scanButton.setEnabled(false);
        service.rescanRom(selection);
        tablesTab.scanButton.setEnabled(true);
      }
    }
    else if (e.getActionCommand().equals("cancelScan")) {
      this.progressWorker.cancel(true);
      this.progressWorker.destroy();
    }
    else if (e.getActionCommand().equals("tableHighscore")) {
      List<GameInfo> gameInfos = service.getGameInfos();
      GamesTable gamesTable = tablesTab.getGamesTable();
      int selectedRow = gamesTable.getSelectedRow();
      GameInfo gameInfo = gameInfos.get(selectedRow);
      if (gameInfo.resolveHighscore() != null) {
        new HighscoreDialog(this.tablesTab.configWindow, gameInfo, "Highscore for " + gameInfo.getGameDisplayName());
      }
    }
  }

  private void rescanTables() {
    int input = JOptionPane.showConfirmDialog(ConfigWindow.getInstance(),
        "Rescan all tables? (This may take a while.)", "Table Rescan", JOptionPane.OK_CANCEL_OPTION);
    if (input != 0) {
      return;
    }

    List<GameInfo> gameInfos = service.getGameInfos();
    final JDialog progressDialog = new JDialog(ConfigWindow.getInstance(), "Table Scanner", true);
    progressDialog.setSize(400, 200);
    progressDialog.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);
    gbc.gridx = 0;
    gbc.gridy = 0;

    JProgressBar progressBar = new JProgressBar(0, gameInfos.size());
    progressBar.setValue(0);
    progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    progressDialog.add(progressBar, gbc);
    gbc.gridy++;

    statusLabel = new JLabel(" ");
    progressDialog.add(statusLabel, gbc);
    gbc.gridy++;

    JLabel progressLabel = new JLabel("Progress...");
    progressDialog.add(progressLabel, gbc);
    gbc.gridy++;

    JButton cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("cancelScan");
    cancelButton.addActionListener(this);
    progressDialog.add(cancelButton, gbc);
    gbc.gridy++;

    progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setLocationRelativeTo(ConfigWindow.getInstance());

    progressWorker = new ProgressWorker(service);
    progressWorker.addPropertyChangeListener(evt -> {
      String name = evt.getPropertyName();
      System.out.println(name);
      if (name.equals("progress")) {
        int progress = (int) evt.getNewValue();
        progressBar.setValue(progress);
        progressLabel.setText("Progress: " + progress + "%");
        progressLabel.repaint();
      }
      else if (name.equals("state")) {
        SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
        switch (state) {
          case DONE:
            progressWorker.destroy();
            progressDialog.setVisible(false);
            break;
        }
      }
    });
    progressWorker.execute();

    new Thread(new Runnable() {
      @Override
      public void run() {
        service.rescanAllTables();
      }
    }).start();


    progressDialog.setVisible(true);
  }

  public class ProgressWorker extends SwingWorker<Object, Object> implements RomScanListener {
    private VPinService service;
    private int tablesToScan = 0;
    private int total = 0;
    private GameInfo latestScan;
    private GameInfo latestPublish;

    ProgressWorker(VPinService service) {
      this.service = service;
      this.tablesToScan = service.getGameInfos().size();
      this.total = service.getGameInfos().size();
      this.service.addRomScannedListener(this);
    }

    @Override
    protected Object doInBackground() throws Exception {
      while (true) {
        int progress = getProgress();
        int calcProgress = total - tablesToScan;
        int percentage = calcProgress * 100 / total;
        if (progress != percentage) {
          setProgress(percentage);
        }

        if (latestScan == null || latestScan.getId() != latestPublish.getId()) {
          this.latestPublish = latestScan;
          publish(latestScan);
        }

        else {
          Thread.sleep(50);
        }

        if (this.tablesToScan == 0) {
          break;
        }
      }

      return null;
    }

    @Override
    protected void process(List<Object> chunks) {
      super.process(chunks);
      GameInfo o = (GameInfo) chunks.get(0);
      if (o != null) {
        statusLabel.setText(o.getGameDisplayName());
      }
    }

    public void destroy() {
      this.service.removeRomScannedListener(this);
    }

    @Override
    public void romScanned(RomScannedEvent e) {
      latestScan = e.getGameInfo();
      tablesToScan--;
    }
  }
}
